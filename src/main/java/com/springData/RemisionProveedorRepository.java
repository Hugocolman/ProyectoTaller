package com.springData;

import com.springData.domain.RemisionProveedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface RemisionProveedorRepository extends JpaRepository<RemisionProveedor, Long> {
    Page<RemisionProveedor> findByProveedor_NombreContainingIgnoreCase(String q, Pageable pageable);
    Page<RemisionProveedor> findByFechaBetween(LocalDate desde, LocalDate hasta, Pageable pageable);
    Page<RemisionProveedor> findByProveedor_NombreContainingIgnoreCaseAndFechaBetween(String q, LocalDate desde, LocalDate hasta, Pageable pageable);
}

