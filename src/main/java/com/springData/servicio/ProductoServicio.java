package com.springData.servicio;

import com.springData.domain.Producto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductoServicio {
    List<Producto> listar();
    Page<Producto> listar(Pageable pageable);
    Page<Producto> buscarPorNombre(String q, Pageable pageable);
    void guardar(Producto producto);
    void eliminar(Producto producto);
    void eliminar(Long id);
    Producto buscarPorId(Long id);
    void actualizar(Producto producto);
}

