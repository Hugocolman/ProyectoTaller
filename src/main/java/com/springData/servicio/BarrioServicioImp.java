package com.springData.servicio;

import com.springData.domain.Barrio;
import com.springData.BarrioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BarrioServicioImp implements BarrioServicio {

    @Autowired
    private BarrioRepositorio barrioRepositorio;

    @Override
    public List<Barrio> listar() {
        return (List<Barrio>) barrioRepositorio.findAll();
    }

    @Override
    public Barrio buscarPorId(Long id) {
        return barrioRepositorio.findById(id).orElse(null);
    }

    @Override
    public void guardar(Barrio barrio) {
        barrioRepositorio.save(barrio);
    }

    @Override
    public void eliminar(Long id) {
        barrioRepositorio.deleteById(id);
    }
}
