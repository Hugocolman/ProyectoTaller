package com.springData.servicio;

import com.springData.UsuarioRepositorio;
import com.springData.domain.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServicioImp implements UsuarioServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Override
    public List<Usuario> listarTodos() {
        return usuarioRepositorio.findAll();
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepositorio.findById(id); // Devuelve un Optional
    }

    @Override
    public void guardar(Usuario usuario) {
        usuarioRepositorio.save(usuario);
    }

    @Override
    public void eliminar(Long id) {
        usuarioRepositorio.deleteById(id);
    }

    @Override
    public List<Usuario> listar() {
        // Implementado para devolver la lista de todos los usuarios
        return listarTodos();
    }
}
