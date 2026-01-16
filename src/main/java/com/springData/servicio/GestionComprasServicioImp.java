package com.springData.servicio;

import com.springData.MovimientoCompraRepository;
import com.springData.CuentaPagarRepository;
import com.springData.ProductoRepository;
import com.springData.domain.MovimientoCompra;
import com.springData.domain.DetalleCompra;
import com.springData.domain.CuentaPagar;
import com.springData.domain.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class GestionComprasServicioImp implements GestionComprasServicio {

    @Autowired private MovimientoCompraRepository compraRepo;
    @Autowired private CuentaPagarRepository cxpRepo;
    @Autowired private ProductoRepository productoRepo;

    @Override
    @Transactional
    public void anularCompra(Long compraId) {
        MovimientoCompra c = compraRepo.findById(compraId).orElse(null);
        if (c == null) return;
        if ("ANULADA".equalsIgnoreCase(c.getEstado())) return;

        if (c.getDetalles() != null) {
            for (DetalleCompra d : c.getDetalles()) {
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

        List<CuentaPagar> cxps = cxpRepo.findByCompra_Id(c.getId());
        for (CuentaPagar cp : cxps) {
            cp.setEstado("ANULADA");
            cp.setSaldo(BigDecimal.ZERO);
            cxpRepo.save(cp);
        }

        c.setEstado("ANULADA");
        compraRepo.save(c);
    }
}
