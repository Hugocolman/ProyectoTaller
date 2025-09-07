package com.springData.servicio;

import com.springData.domain.Marca;
import com.springData.MarcaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MarcaServicioImp implements MarcaServicio {

    @Autowired
    private MarcaRepository marcaRepository;

    @Override
    @Transactional(readOnly = true)
    public Iterable<Marca> listar() {
        return marcaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Marca buscarPorId(Long id) {
        return marcaRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void guardar(Marca marca) {
        marcaRepository.save(marca); // Método ejecuta la operación y no retorna nada
    }

    @Override
    @Transactional
    public void eliminar(Marca marca) {
        marcaRepository.delete(marca);  // Eliminamos la marca que se pasa como parámetro
    }

    // Método de eliminación por ID
    @Transactional
    public void eliminar(Long id) {
        Marca marca = marcaRepository.findById(id).orElse(null);
        if (marca != null) {
            marcaRepository.delete(marca); // Elimina la marca si existe
        } else {
            throw new IllegalArgumentException("Marca no encontrada"); // Lanza una excepción si la marca no existe
        }
    }
}
