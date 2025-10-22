package com.springData.servicio;

import com.springData.domain.Rol;
import com.springData.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RolServicioImp implements RolServicio {

    @Autowired
    private RolRepository rolRepository;

    @Override
    @Transactional(readOnly = true)
    public Iterable<Rol> listar() {
        return rolRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Rol buscarPorId(Long id) {
        return rolRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void guardar(Rol rol) {
        rolRepository.save(rol);
    }

    @Override
    @Transactional
    public void eliminar(Rol rol) {
        rolRepository.delete(rol);
    }
}
