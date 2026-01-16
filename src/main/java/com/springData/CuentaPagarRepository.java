package com.springData;

import com.springData.domain.CuentaPagar;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CuentaPagarRepository extends JpaRepository<CuentaPagar, Long> {
    Page<CuentaPagar> findByProveedor_NombreContainingIgnoreCaseOrNumeroFacturaContainingIgnoreCase(String proveedor, String numero, Pageable pageable);
    java.util.List<com.springData.domain.CuentaPagar> findByCompra_Id(Long compraId);
}
