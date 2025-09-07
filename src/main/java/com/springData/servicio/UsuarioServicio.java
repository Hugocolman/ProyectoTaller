package com.springData.servicio;

import com.springData.domain.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioServicio {
    List<Usuario> listarTodos();
    Optional<Usuario> buscarPorId(Long id);
    void guardar(Usuario usuario);
    void eliminar(Long id);
    

    public List<Usuario> listar();

    
}
