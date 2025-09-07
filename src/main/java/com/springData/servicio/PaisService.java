package com.springData.servicio;

import com.springData.domain.Pais;

import java.util.List;
import java.util.Optional;

public interface PaisService {
    // Método para obtener todos los países
    List<Pais> obtenerTodos();

    // Método para obtener un país por su ID
    Optional<Pais> obtenerPorId(Long id);

    // Método para guardar un país
    Pais guardar(Pais pais);

    // Método para eliminar un país por su ID
    void eliminar(Long id);
}
