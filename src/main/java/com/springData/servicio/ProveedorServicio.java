package com.springData.servicio;

import com.springData.domain.Proveedor;
import java.util.Optional;

public interface ProveedorServicio {

    // Listar todos los proveedores
    Iterable<Proveedor> listar();

    // Buscar proveedor por ID
    Optional<Proveedor> buscarPorId(Long id);

    // Guardar o actualizar un proveedor
    void guardar(Proveedor proveedor);

    // Eliminar un proveedor
    void eliminar(Long id);
}
