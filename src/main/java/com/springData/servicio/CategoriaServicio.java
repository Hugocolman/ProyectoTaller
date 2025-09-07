package com.springData.servicio;

import com.springData.domain.Categoria;
import java.util.Optional;

public interface CategoriaServicio {

    // Listar todas las categorías
    Iterable<Categoria> listar();

    // Buscar categoría por ID
    Optional<Categoria> buscarPorId(Long id);

    // Guardar o actualizar una categoría
    void guardar(Categoria categoria);

    // Eliminar una categoría
    void eliminar(Long id);
}
