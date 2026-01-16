package com.springData.servicio;

import com.springData.NotaCompraRepository;
import com.springData.CuentaPagarRepository;
import com.springData.MovimientoCompraRepository;
import com.springData.ProductoRepository;
import com.springData.domain.NotaCompra;
import com.springData.domain.CuentaPagar;
import com.springData.domain.MovimientoCompra;
import com.springData.domain.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class NotaCompraServicioImp implements NotaCompraServicio {

    @Autowired private NotaCompraRepository repo;
    @Autowired private CuentaPagarRepository cxpRepo;
    @Autowired private MovimientoCompraRepository compraRepo;
    @Autowired private ProductoRepository productoRepo;

    @Override
    @Transactional(readOnly = true)
    public Page<NotaCompra> listar(String q, LocalDate desde, LocalDate hasta, Pageable pageable) {
        if (desde == null) desde = LocalDate.now().withDayOfMonth(1);
        if (hasta == null) hasta = LocalDate.now();
        if (q != null && !q.isBlank()) {
            return repo.findByCompra_Proveedor_NombreContainingIgnoreCaseAndFechaBetween(q, desde, hasta, pageable);
        }
        return repo.findByFechaBetween(desde, hasta, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public NotaCompra buscarPorId(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void aplicar(NotaCompra nota) {
        if (nota.getFecha() == null) nota.setFecha(LocalDate.now());
        MovimientoCompra compra = compraRepo.findById(nota.getCompra().getId()).orElse(null);
        if (compra == null) return;
        BigDecimal monto = nota.getMonto() != null ? nota.getMonto() : BigDecimal.ZERO;
        if (monto.compareTo(BigDecimal.ZERO) <= 0) return;

        if ("CREDITO".equalsIgnoreCase(nota.getTipo())) {
            // Reduce total compra y saldo CxP
            compra.setTotal(compra.getTotal() != null ? compra.getTotal().subtract(monto) : BigDecimal.ZERO);
            if (compra.getTotal().compareTo(BigDecimal.ZERO) < 0) compra.setTotal(BigDecimal.ZERO);
            compraRepo.save(compra);
            List<CuentaPagar> cxps = cxpRepo.findByCompra_Id(compra.getId());
            for (CuentaPagar cp : cxps) {
                BigDecimal nuevo = cp.getSaldo().subtract(monto);
                if (nuevo.compareTo(BigDecimal.ZERO) <= 0) {
                    cp.setSaldo(BigDecimal.ZERO);
                    cp.setEstado("PAGADA");
                } else {
                    cp.setSaldo(nuevo);
                }
                cxpRepo.save(cp);
            }
            // Ajuste de stock (salida)
            if (nota.isAfectaStock() && nota.getProducto() != null && nota.getCantidad() != null && nota.getCantidad() > 0) {
                Producto p = productoRepo.findById(nota.getProducto().getId()).orElse(null);
                if (p != null) {
                    p.setCantidad(Math.max(0, p.getCantidad() - nota.getCantidad()));
                    productoRepo.save(p);
                }
            }
        } else if ("DEBITO".equalsIgnoreCase(nota.getTipo())) {
            // Aumenta total compra y saldo CxP
            compra.setTotal(compra.getTotal() != null ? compra.getTotal().add(monto) : monto);
            compraRepo.save(compra);
            List<CuentaPagar> cxps = cxpRepo.findByCompra_Id(compra.getId());
            for (CuentaPagar cp : cxps) {
                cp.setSaldo(cp.getSaldo().add(monto));
                cp.setEstado("PENDIENTE");
                cxpRepo.save(cp);
            }
            // Ajuste de stock (entrada) opcional
            if (nota.isAfectaStock() && nota.getProducto() != null && nota.getCantidad() != null && nota.getCantidad() > 0) {
                Producto p = productoRepo.findById(nota.getProducto().getId()).orElse(null);
                if (p != null) {
                    p.setCantidad(p.getCantidad() + nota.getCantidad());
                    productoRepo.save(p);
                }
            }
        }
        repo.save(nota);
    }
}

