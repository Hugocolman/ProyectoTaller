package com.springData;

import com.springData.domain.Presupuesto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PresupuestoRepository extends JpaRepository<Presupuesto, Long> {
    Page<Presupuesto> findByClienteContainingIgnoreCase(String q, Pageable pageable);
}

