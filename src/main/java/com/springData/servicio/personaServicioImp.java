
package com.springData.servicio;

import com.springData.PersonaRepository;
import com.springData.domain.Persona;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Huguito
 */

//IMPLEMENTACION
@Service

public class personaServicioImp implements personaServicio{
    
    @Autowired //Manera de automatizar la dependencia que necesita de otra dependencia
    private PersonaRepository perdao;
    
    @Override
    @Transactional(readOnly = true) 
    /* @Transactional: Propiedad de poder o no editar el contenido de la bd, 
    puede reverir la accion si hay algun error (Rollback)*/
    
    public List<Persona> listar() {
        //Retorna valor tipo list
        //Casteo: Antepone el fomrato al cual debemos retornar delante de la funcion
        return (List<Persona>) perdao.findAll();// Retorna la lista completa de la tabla persona
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Persona> listar(Pageable pageable) {
        return perdao.findAll(pageable);
    }

    @Override
    @Transactional
    public void guardar(Persona per) {
        perdao.save(per);
    }

    @Override
    @Transactional
    public void eliminar(Persona per) {
        perdao.delete(per);
    }
    
    @Override
    @Transactional(readOnly = true) 
    public Persona buscarPorId(Persona per) {
        
        return perdao.findById(per.getId_persona()).orElse(per);
    }

    @Override
    public Persona buscarPorId(Long idPersona) {
        return perdao.findById(idPersona).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Persona> listarClientes() {
        return perdao.findByEsClienteTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Persona> buscarPorNombreOApellido(String q, Pageable pageable) {
        return perdao.findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(q, q, pageable);
    }
    
}
