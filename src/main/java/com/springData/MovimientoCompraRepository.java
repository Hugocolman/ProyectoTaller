package com.springData;

import com.springData.domain.MovimientoCompra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientoCompraRepository extends JpaRepository<MovimientoCompra, Long> {
    Page<MovimientoCompra> findByProveedor_NombreContainingIgnoreCase(String q, Pageable pageable);
    java.util.List<com.springData.domain.MovimientoCompra> findByFechaBetween(java.time.LocalDate desde, java.time.LocalDate hasta);
    Page<com.springData.domain.MovimientoCompra> findByFechaBetween(java.time.LocalDate desde, java.time.LocalDate hasta, Pageable pageable);
    Page<com.springData.domain.MovimientoCompra> findByProveedor_NombreContainingIgnoreCaseAndFechaBetween(String q, java.time.LocalDate desde, java.time.LocalDate hasta, Pageable pageable);
}
