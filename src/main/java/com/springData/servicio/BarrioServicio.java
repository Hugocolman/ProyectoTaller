package com.springData.servicio;

import com.springData.domain.Barrio;

import java.util.List;

public interface BarrioServicio {
    List<Barrio> listar();
    Barrio buscarPorId(Long id);
    void guardar(Barrio barrio);
    void eliminar(Long id);
}
