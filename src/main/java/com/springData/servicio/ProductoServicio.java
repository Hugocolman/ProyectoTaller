package com.springData.servicio;

import com.springData.domain.Producto;

import java.util.List;

public interface ProductoServicio {
    List<Producto> listar();  // Método para listar todas las noticias
    void guardar(Producto noticia);  // Método para guardar una noticia
    void eliminar(Producto noticia);  // Método para eliminar una noticia
    Producto buscarPorId(Long id);  // Método para buscar una noticia por su ID
    void actualizar(Producto noticia);  // Método para actualizar una noticia
}
