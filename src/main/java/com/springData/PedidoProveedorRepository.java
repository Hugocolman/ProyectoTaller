package com.springData;

import com.springData.domain.PedidoProveedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoProveedorRepository extends JpaRepository<PedidoProveedor, Long> {
    Page<PedidoProveedor> findByProveedor_NombreContainingIgnoreCaseOrNumeroContainingIgnoreCase(String proveedor, String numero, Pageable pageable);
}

