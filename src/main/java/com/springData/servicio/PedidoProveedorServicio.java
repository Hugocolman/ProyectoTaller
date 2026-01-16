package com.springData.servicio;

import com.springData.domain.PedidoProveedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PedidoProveedorServicio {
    Page<PedidoProveedor> listar(String q, Pageable pageable);
    PedidoProveedor buscarPorId(Long id);
    void guardar(PedidoProveedor p);
    void eliminar(Long id);
    Long convertirAOrdenCompra(Long pedidoId);
    default boolean autorizar(Long id) { return false; }
    default boolean desautorizar(Long id) { return false; }
    default boolean anular(Long id) { return false; }
}
