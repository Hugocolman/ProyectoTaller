package com.springData;

import com.springData.domain.Factura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacturaRepository extends JpaRepository<Factura, Long> {
    Page<Factura> findByClienteContainingIgnoreCase(String q, Pageable pageable);
    Optional<Factura> findByRemito_Id(Long remitoId);
}

