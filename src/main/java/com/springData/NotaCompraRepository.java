package com.springData;

import com.springData.domain.NotaCompra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface NotaCompraRepository extends JpaRepository<NotaCompra, Long> {
    Page<NotaCompra> findByCompra_Proveedor_NombreContainingIgnoreCase(String q, Pageable pageable);
    Page<NotaCompra> findByFechaBetween(LocalDate desde, LocalDate hasta, Pageable pageable);
    Page<NotaCompra> findByCompra_Proveedor_NombreContainingIgnoreCaseAndFechaBetween(String q, LocalDate desde, LocalDate hasta, Pageable pageable);
}

