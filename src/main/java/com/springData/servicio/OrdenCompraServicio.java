package com.springData.servicio;

import com.springData.domain.OrdenCompra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrdenCompraServicio {
    Page<OrdenCompra> listar(String q, Pageable pageable);
    OrdenCompra buscarPorId(Long id);
    void guardar(OrdenCompra ordenCompra);
    void eliminar(Long id);
    Long recibir(Long ordenId);
}
