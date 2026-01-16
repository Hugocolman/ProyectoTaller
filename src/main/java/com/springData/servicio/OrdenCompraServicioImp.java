package com.springData.servicio;

import com.springData.OrdenCompraRepository;
import com.springData.domain.OrdenCompra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrdenCompraServicioImp implements OrdenCompraServicio {

    @Autowired
    private OrdenCompraRepository ordenCompraRepository;

    @Autowired
    private com.springData.MovimientoCompraRepository movimientoCompraRepository;
    @Autowired
    private com.springData.DetalleCompraRepository detalleCompraRepository;
    @Autowired
    private com.springData.CuentaPagarRepository cuentaPagarRepository;
    @Autowired
    private com.springData.ProductoRepository productoRepository;

    @Override
    public Page<OrdenCompra> listar(String q, Pageable pageable) {
        if (q != null && !q.isBlank()) {
            return ordenCompraRepository.findByProveedor_NombreContainingIgnoreCaseOrNumeroContainingIgnoreCase(q, q, pageable);
        }
        return ordenCompraRepository.findAll(pageable);
    }

    @Override
    public OrdenCompra buscarPorId(Long id) {
        OrdenCompra oc = ordenCompraRepository.findById(id).orElse(null);
        if (oc != null && oc.getDetalles() != null) {
            oc.getDetalles().size();
        }
        return oc;
    }

    @Override
    public void guardar(OrdenCompra ordenCompra) {
        boolean numeroVacio = (ordenCompra.getNumero() == null || ordenCompra.getNumero().isBlank());
        if (numeroVacio) {
            ordenCompra = ordenCompraRepository.save(ordenCompra);
            String numero = String.format("OC-%1$tY%1$tm%1$td-%2$06d", java.time.LocalDate.now(), ordenCompra.getId());
            ordenCompra.setNumero(numero);
        }
        ordenCompraRepository.save(ordenCompra);
    }

    @Override
    public void eliminar(Long id) {
        if (id != null) {
            ordenCompraRepository.deleteById(id);
        }
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public Long recibir(Long ordenId) {
        var oc = ordenCompraRepository.findById(ordenId).orElse(null);
        if (oc == null) return null;
        // Crear movimiento de compra
        com.springData.domain.MovimientoCompra mc = new com.springData.domain.MovimientoCompra();
        mc.setProveedor(oc.getProveedor());
        mc.setFecha(java.time.LocalDate.now());
        mc.setMetodoPago("CTA CTE");
        mc.setNroFactura(null);
        mc.setObservacion("Generado desde OC " + oc.getNumero());
        mc.setEstado("REGISTRADA");
        mc.setTotal(java.math.BigDecimal.ZERO);
        mc = movimientoCompraRepository.save(mc);

        java.math.BigDecimal total = java.math.BigDecimal.ZERO;
        if (oc.getDetalles() != null) {
            for (var dOc : oc.getDetalles()) {
                com.springData.domain.DetalleCompra dc = new com.springData.domain.DetalleCompra();
                dc.setCompra(mc);
                dc.setProducto(dOc.getProducto());
                dc.setCantidad(dOc.getCantidad());
                dc.setCostoUnitario(dOc.getCostoUnitario());
                detalleCompraRepository.save(dc);
                // Actualizar stock
                var prod = dOc.getProducto();
                if (prod != null) {
                    var p = productoRepository.findById(prod.getId()).orElse(null);
                    if (p != null) {
                        p.setCantidad(p.getCantidad() + dOc.getCantidad());
                        productoRepository.save(p);
                    }
                }
                total = total.add(dOc.getCostoUnitario().multiply(new java.math.BigDecimal(dOc.getCantidad())));
            }
        }
        mc.setTotal(total);
        movimientoCompraRepository.save(mc);

        // Crear cuenta por pagar
        com.springData.domain.CuentaPagar cp = new com.springData.domain.CuentaPagar();
        cp.setProveedor(oc.getProveedor());
        cp.setCompra(mc);
        cp.setFechaEmision(java.time.LocalDate.now());
        cp.setFechaVencimiento(java.time.LocalDate.now().plusDays(30));
        cp.setNumeroFactura(oc.getNumero());
        cp.setMonto(total);
        cp.setSaldo(total);
        cp.setEstado("PENDIENTE");
        cuentaPagarRepository.save(cp);

        // Opcional: marcar OC
        oc.setEstado("RECIBIDA");
        ordenCompraRepository.save(oc);
        return mc.getId();
    }
}
