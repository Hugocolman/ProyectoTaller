package com.springData;

import com.springData.domain.MovimientoVenta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientoVentaRepository extends JpaRepository<MovimientoVenta, Long> {
    Page<MovimientoVenta> findByClienteContainingIgnoreCase(String q, Pageable pageable);
}

