
package com.springData.servicio;

import com.springData.domain.Persona;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Huguito
 */
public interface personaServicio {
    
    //CRUD
    //Recibe la clase persona del domain
    //Convencion: Mayuscula clase y variable o instancia en minuscula
    
    public List<Persona> listar();
    Page<Persona> listar(Pageable pageable);
    
    public void guardar(Persona per);
    
    public void eliminar(Persona per);
    
    public Persona buscarPorId(Persona per);

    public Persona buscarPorId(Long idPersona);

    public List<Persona> listarClientes();
    Page<Persona> buscarPorNombreOApellido(String q, Pageable pageable);
    
}
