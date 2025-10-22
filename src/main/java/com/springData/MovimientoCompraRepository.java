package com.springData;

import com.springData.domain.MovimientoCompra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientoCompraRepository extends JpaRepository<MovimientoCompra, Long> {
    Page<MovimientoCompra> findByNroFacturaContainingIgnoreCase(String nro, Pageable pageable);
    Page<MovimientoCompra> findByProveedor_NombreContainingIgnoreCase(String nombre, Pageable pageable);
}
