package com.springData.servicio;

import com.springData.domain.Marca;

public interface MarcaServicio {
    Iterable<Marca> listar();

    Marca buscarPorId(Long id);

    void guardar(Marca marca); // Retorno void

    void eliminar(Marca marca);

    void eliminar(Long id); // Método de eliminación por ID
}
