package com.springData.servicio;

import com.springData.domain.Producto;  // Cambiar a Producto
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import com.springData.ProductoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class ProductoServicioImp implements ProductoServicio {  // Cambiar a ProductoServicio

    @Autowired
    private ProductoRepository productoRepository;  // Cambiar a ProductoRepository

    @Override
    public List<Producto> listar() {  // Cambiar a Producto
        return (List<Producto>) productoRepository.findAll();  // Cambiar a Producto
    }

    @Override
    public Page<Producto> listar(Pageable pageable) {
        return productoRepository.findAll(pageable);
    }

    @Override
    public Page<Producto> buscarPorNombre(String q, Pageable pageable) {
        return productoRepository.findByNombreContainingIgnoreCase(q, pageable);
    }

    @Override
    public void guardar(Producto producto) {  // Cambiar a Producto
        productoRepository.save(producto);  // Cambiar a Producto
    }

    @Override
    public void eliminar(Producto producto) {  // Cambiar a Producto
        productoRepository.delete(producto);  // Cambiar a Producto
    }

    @Override
    public Producto buscarPorId(Long id) {  // Cambiar a Producto
        return productoRepository.findById(id).orElse(null);  // Cambiar a Producto
    }

    @Override
    public void actualizar(Producto producto) {  // Cambiar a Producto
        productoRepository.save(producto);  // Utiliza el mismo método de guardar para actualizar
    }

    public void eliminar(Long id) {
        if (id != null) {
            productoRepository.deleteById(id);
        }
    }
}

