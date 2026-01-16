package com.springData.servicio;

import com.springData.ProductoRepository;
import com.springData.domain.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProductoServicioImpTests {

    private ProductoRepository productoRepository;
    private ProductoServicioImp servicio;

    @BeforeEach
    void setup() {
        productoRepository = Mockito.mock(ProductoRepository.class);
        servicio = new ProductoServicioImp();
        // Inyección manual del mock (field package-private no; usamos reflexión simple)
        try {
            var f = ProductoServicioImp.class.getDeclaredField("productoRepository");
            f.setAccessible(true);
            f.set(servicio, productoRepository);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void listarPaginado_devuelvePageRepository() {
        PageRequest pr = PageRequest.of(0, 5);
        when(productoRepository.findAll(pr)).thenReturn(new PageImpl<>(Collections.emptyList(), pr, 0));
        Page<Producto> page = servicio.listar(pr);
        assertThat(page).isNotNull();
        verify(productoRepository, times(1)).findAll(pr);
    }

    @Test
    void buscarPorNombre_delegaEnRepositorio() {
        PageRequest pr = PageRequest.of(0, 5);
        when(productoRepository.findByNombreContainingIgnoreCase("abc", pr))
                .thenReturn(new PageImpl<>(Collections.emptyList(), pr, 0));
        servicio.buscarPorNombre("abc", pr);
        verify(productoRepository).findByNombreContainingIgnoreCase("abc", pr);
    }

    @Test
    void guardar_llamaSaveConEntidad() {
        Producto p = new Producto();
        p.setNombre("Teclado");
        p.setPrecio(new BigDecimal("10.00"));
        servicio.guardar(p);
        ArgumentCaptor<Producto> captor = ArgumentCaptor.forClass(Producto.class);
        verify(productoRepository).save(captor.capture());
        assertThat(captor.getValue().getNombre()).isEqualTo("Teclado");
    }
}

