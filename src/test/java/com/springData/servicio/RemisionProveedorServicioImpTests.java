package com.springData.servicio;

import com.springData.DetalleRemisionProveedorRepository;
import com.springData.ProductoRepository;
import com.springData.RemisionProveedorRepository;
import com.springData.domain.RemisionProveedor;
import com.springData.domain.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RemisionProveedorServicioImpTests {

    private RemisionProveedorRepository remisionRepo;
    private DetalleRemisionProveedorRepository detalleRepo;
    private ProductoRepository productoRepo;
    private RemisionProveedorServicioImp servicio;

    @BeforeEach
    void setup() {
        remisionRepo = Mockito.mock(RemisionProveedorRepository.class);
        detalleRepo = Mockito.mock(DetalleRemisionProveedorRepository.class);
        productoRepo = Mockito.mock(ProductoRepository.class);
        servicio = new RemisionProveedorServicioImp();
        try {
            var f1 = RemisionProveedorServicioImp.class.getDeclaredField("repo"); f1.setAccessible(true); f1.set(servicio, remisionRepo);
            var f2 = RemisionProveedorServicioImp.class.getDeclaredField("detalleRepo"); f2.setAccessible(true); f2.set(servicio, detalleRepo);
            var f3 = RemisionProveedorServicioImp.class.getDeclaredField("productoRepo"); f3.setAccessible(true); f3.set(servicio, productoRepo);
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    @Test
    void registrar_actualizaStock_yGuarda() {
        RemisionProveedor r = new RemisionProveedor();
        var prov = new com.springData.domain.Proveedor(); prov.setId(1L); r.setProveedor(prov);
        when(remisionRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Producto p = new Producto(); p.setId(10L); p.setCantidad(5);
        when(productoRepo.findById(10L)).thenReturn(java.util.Optional.of(p));

        Long id = servicio.registrar(r, new Long[]{10L}, new Integer[]{3}, new BigDecimal[]{new BigDecimal("1.00")});
        assertThat(p.getCantidad()).isEqualTo(8);
        verify(remisionRepo, times(1)).save(any());
        assertThat(id).isNotNull();
    }
}

