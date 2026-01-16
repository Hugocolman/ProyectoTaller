package com.springData.repository;

import com.springData.ProductoRepository;
import com.springData.domain.Producto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductoRepositoryTests {

    @Autowired
    private ProductoRepository productoRepository;

    @Test
    void findByNombreContainingIgnoreCase_devuelveCoincidencias() {
        Producto p1 = new Producto();
        p1.setNombre("Notebook Gamer");
        p1.setPrecio(new BigDecimal("1000.00"));
        p1.setCantidad(5);
        productoRepository.save(p1);

        Producto p2 = new Producto();
        p2.setNombre("Mouse Inalámbrico");
        p2.setPrecio(new BigDecimal("20.00"));
        p2.setCantidad(50);
        productoRepository.save(p2);

        Page<Producto> page = productoRepository.findByNombreContainingIgnoreCase("note", PageRequest.of(0, 10));
        assertThat(page.getTotalElements()).isGreaterThanOrEqualTo(1);
        assertThat(page.getContent()).extracting(Producto::getNombre).anyMatch(n -> n.toLowerCase().contains("note"));
    }
}

