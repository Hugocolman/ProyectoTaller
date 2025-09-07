package com.springData.servicio;

import com.springData.domain.Pais;
import com.springData.PaisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaisServiceImp implements PaisService {

    @Autowired
    private PaisRepository paisRepository;

    @Override
    public List<Pais> obtenerTodos() {
        return paisRepository.findAll();  // Implementación del método
    }

    @Override
    public Optional<Pais> obtenerPorId(Long id) {
        return paisRepository.findById(id);  // Implementación del método
    }

    @Override
    public Pais guardar(Pais pais) {
        return paisRepository.save(pais);  // Implementación del método
    }

    @Override
    public void eliminar(Long id) {
        paisRepository.deleteById(id);  // Implementación del método
    }
}
