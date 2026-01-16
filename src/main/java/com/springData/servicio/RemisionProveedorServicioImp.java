package com.springData.servicio;

import com.springData.ProductoRepository;
import com.springData.RemisionProveedorRepository;
import com.springData.domain.DetalleRemisionProveedor;
import com.springData.domain.Producto;
import com.springData.domain.RemisionProveedor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RemisionProveedorServicioImp implements RemisionProveedorServicio {

    @Autowired private RemisionProveedorRepository repo;
    @Autowired private ProductoRepository productoRepo;

    @Override
    @Transactional
    public Long registrar(RemisionProveedor remision, Long[] itemProductoId, Integer[] itemCantidad, BigDecimal[] itemCosto) {
        if (remision.getProveedor() == null || remision.getProveedor().getId() == null) {
            throw new IllegalArgumentException("Proveedor requerido");
        }
        if (remision.getFecha() == null) remision.setFecha(LocalDate.now());

        List<DetalleRemisionProveedor> detalles = new ArrayList<>();
        if (itemProductoId != null && itemCantidad != null && itemProductoId.length == itemCantidad.length) {
            for (int i = 0; i < itemProductoId.length; i++) {
                Long pid = itemProductoId[i];
                Integer cant = itemCantidad[i];
                BigDecimal costo = (itemCosto != null && itemCosto.length > i) ? itemCosto[i] : null;
                if (pid == null || cant == null || cant <= 0) continue;
                Producto p = productoRepo.findById(pid).orElse(null);
                if (p == null) continue;
                DetalleRemisionProveedor d = new DetalleRemisionProveedor();
                d.setRemision(remision);
                d.setProducto(p);
                d.setCantidad(cant);
                d.setCostoUnitario(costo);
                detalles.add(d);
                // Entrada de stock
                p.setCantidad(p.getCantidad() + cant);
                productoRepo.save(p);
            }
        }
        if (detalles.isEmpty()) {
            throw new IllegalArgumentException("Debe agregar al menos una línea válida");
        }
        remision.setDetalles(detalles);
        remision = repo.save(remision);
        if (remision.getNumero() == null || remision.getNumero().isBlank()) {
            String nro = String.format("REM-%1$tY%1$tm%1$td-%2$06d", LocalDate.now(), remision.getId());
            remision.setNumero(nro);
            repo.save(remision);
        }
        return remision.getId();
    }

    @Override
    @Transactional
    public void anular(Long remisionId) {
        RemisionProveedor r = repo.findById(remisionId).orElse(null);
        if (r == null) return;
        if ("ANULADA".equalsIgnoreCase(r.getEstado())) return;
        if (r.getDetalles() != null) {
            for (DetalleRemisionProveedor d : r.getDetalles()) {
                Producto p = d.getProducto();
                if (p == null) continue;
                Producto real = productoRepo.findById(p.getId()).orElse(null);
                if (real != null) {
                    int nuevo = real.getCantidad() - d.getCantidad();
                    real.setCantidad(Math.max(0, nuevo));
                    productoRepo.save(real);
                }
            }
        }
        r.setEstado("ANULADA");
        repo.save(r);
    }
}
