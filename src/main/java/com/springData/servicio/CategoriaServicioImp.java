package com.springData.servicio;

import com.springData.domain.Categoria;
import com.springData.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoriaServicioImp implements CategoriaServicio {

    // Inyectamos el repositorio de Categoria
    @Autowired
    private CategoriaRepository categoriaRepository;

    // Listar todas las categorías
    @Override
    public Iterable<Categoria> listar() {
        return categoriaRepository.findAll();
    }

    // Buscar categoría por ID
    @Override
    public Optional<Categoria> buscarPorId(Long id) {
        return categoriaRepository.findById(id);
    }

    // Guardar o actualizar una categoría
    @Override
    public void guardar(Categoria categoria) {
        categoriaRepository.save(categoria);
    }

    // Eliminar una categoría
    @Override
    public void eliminar(Long id) {
        categoriaRepository.deleteById(id);
    }
}
