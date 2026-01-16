package com.springData.servicio;

import com.springData.PedidoProveedorRepository;
import com.springData.OrdenCompraRepository;
import com.springData.domain.PedidoProveedor;
import com.springData.domain.DetallePedidoProveedor;
import com.springData.domain.OrdenCompra;
import com.springData.domain.DetalleOrdenCompra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
public class PedidoProveedorServicioImp implements PedidoProveedorServicio {

    @Autowired
    private PedidoProveedorRepository repo;

    @Autowired
    private OrdenCompraRepository ocRepo;

    @Override
    public Page<PedidoProveedor> listar(String q, Pageable pageable) {
        if (q != null && !q.isBlank()) {
            return repo.findByProveedor_NombreContainingIgnoreCaseOrNumeroContainingIgnoreCase(q, q, pageable);
        }
        return repo.findAll(pageable);
    }

    @Override
    public PedidoProveedor buscarPorId(Long id) {
        PedidoProveedor p = repo.findById(id).orElse(null);
        if (p != null && p.getDetalles() != null) {
            p.getDetalles().size();
        }
        return p;
    }

    @Override
    public void guardar(PedidoProveedor p) {
        boolean numeroVacio = (p.getNumero() == null || p.getNumero().isBlank());
        if (numeroVacio) {
            // guardar primero para obtener ID
            p = repo.save(p);
            String numero = String.format("PED-%1$tY%1$tm%1$td-%2$06d", java.time.LocalDate.now(), p.getId());
            p.setNumero(numero);
        }
        repo.save(p);
    }

    @Override
    public void eliminar(Long id) {
        if (id != null) repo.deleteById(id);
    }

    @Override
    public Long convertirAOrdenCompra(Long pedidoId) {
        PedidoProveedor p = repo.findById(pedidoId).orElse(null);
        if (p == null) return null;
        OrdenCompra oc = new OrdenCompra();
        oc.setProveedor(p.getProveedor());
        oc.setFecha(p.getFecha());
        oc.setNumero("OC-" + (p.getNumero() != null ? p.getNumero() : ("PED" + p.getId())));
        oc.setPedido(p);
        if (p.getDetalles() != null) {
            final OrdenCompra ocRef = oc;
            var detalles = p.getDetalles().stream().map(d -> {
                DetalleOrdenCompra nd = new DetalleOrdenCompra();
                nd.setOrden(ocRef);
                nd.setProducto(d.getProducto());
                nd.setCantidad(d.getCantidad());
                nd.setCostoUnitario(d.getCostoUnitario());
                return nd;
            }).collect(Collectors.toList());
            oc.setDetalles(detalles);
            BigDecimal total = p.getDetalles().stream()
                    .map(d -> d.getCostoUnitario().multiply(new BigDecimal(d.getCantidad())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            oc.setTotal(total);
        }
        oc.setEstado("EMITIDA");
        oc = ocRepo.save(oc);
        return oc.getId();
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public boolean autorizar(Long id) {
        PedidoProveedor p = repo.findById(id).orElse(null);
        if (p == null) return false;
        if (!"PENDIENTE".equalsIgnoreCase(p.getEstado())) return false;
        p.setEstado("AUTORIZADO");
        repo.save(p);
        return true;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public boolean desautorizar(Long id) {
        PedidoProveedor p = repo.findById(id).orElse(null);
        if (p == null) return false;
        if (!"AUTORIZADO".equalsIgnoreCase(p.getEstado())) return false;
        p.setEstado("PENDIENTE");
        repo.save(p);
        return true;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public boolean anular(Long id) {
        PedidoProveedor p = repo.findById(id).orElse(null);
        if (p == null) return false;
        if (!"PENDIENTE".equalsIgnoreCase(p.getEstado())) return false; // permitir si añadimos DESAUTORIZADO como estado distinto
        // No debe existir OC asociada
        long countOc = ocRepo.countByPedido_Id(p.getId());
        if (countOc > 0) return false;
        p.setEstado("ANULADO");
        repo.save(p);
        return true;
    }
}
