package com.springData;

import com.springData.domain.OrdenCompra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Long> {
    Page<OrdenCompra> findByProveedor_NombreContainingIgnoreCaseOrNumeroContainingIgnoreCase(String proveedor, String numero, Pageable pageable);
    long countByEstadoIn(java.util.Collection<String> estados);
    long countByPedido_Id(Long pedidoId);
}
