package com.springData;

import com.springData.domain.DetalleCompra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Long> {
    java.util.List<com.springData.domain.DetalleCompra> findByCompra_FechaBetween(java.time.LocalDate desde, java.time.LocalDate hasta);
}
