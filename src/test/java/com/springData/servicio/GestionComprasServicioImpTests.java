package com.springData.servicio;

import com.springData.CuentaPagarRepository;
import com.springData.MovimientoCompraRepository;
import com.springData.ProductoRepository;
import com.springData.domain.CuentaPagar;
import com.springData.domain.DetalleCompra;
import com.springData.domain.MovimientoCompra;
import com.springData.domain.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GestionComprasServicioImpTests {
    private MovimientoCompraRepository compraRepo;
    private com.springData.DetalleCompraRepository detalleRepo;
    private CuentaPagarRepository cxpRepo;
    private ProductoRepository productoRepo;
    private GestionComprasServicioImp servicio;

    @BeforeEach
    void setup() {
        compraRepo = Mockito.mock(MovimientoCompraRepository.class);
        detalleRepo = Mockito.mock(com.springData.DetalleCompraRepository.class);
        cxpRepo = Mockito.mock(CuentaPagarRepository.class);
        productoRepo = Mockito.mock(ProductoRepository.class);
        servicio = new GestionComprasServicioImp();
        try {
            var f1 = GestionComprasServicioImp.class.getDeclaredField("compraRepo"); f1.setAccessible(true); f1.set(servicio, compraRepo);
            var f2 = GestionComprasServicioImp.class.getDeclaredField("detalleRepo"); f2.setAccessible(true); f2.set(servicio, detalleRepo);
            var f3 = GestionComprasServicioImp.class.getDeclaredField("cxpRepo"); f3.setAccessible(true); f3.set(servicio, cxpRepo);
            var f4 = GestionComprasServicioImp.class.getDeclaredField("productoRepo"); f4.setAccessible(true); f4.set(servicio, productoRepo);
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    @Test
    void anularCompra_revierteStock_yAnulaCxP() {
        Producto prod = new Producto(); prod.setId(1L); prod.setCantidad(10);
        DetalleCompra det = new DetalleCompra(); det.setProducto(prod); det.setCantidad(4);
        MovimientoCompra comp = new MovimientoCompra(); comp.setId(5L); comp.setEstado("REGISTRADA"); comp.setDetalles(java.util.Arrays.asList(det));
        when(compraRepo.findById(5L)).thenReturn(java.util.Optional.of(comp));
        when(productoRepo.findById(1L)).thenReturn(java.util.Optional.of(prod));
        CuentaPagar cp = new CuentaPagar(); cp.setSaldo(new BigDecimal("100.00")); cp.setEstado("PENDIENTE");
        when(cxpRepo.findByCompra_Id(5L)).thenReturn(List.of(cp));

        servicio.anularCompra(5L);

        assertThat(prod.getCantidad()).isEqualTo(6);
        assertThat(cp.getSaldo()).isEqualTo(BigDecimal.ZERO);
        assertThat(cp.getEstado()).isEqualTo("ANULADA");
        assertThat(comp.getEstado()).isEqualTo("ANULADA");
    }
}

