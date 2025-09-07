
package com.springData.servicio;

import com.springData.domain.Persona;
import java.util.List;

/**
 *
 * @author Huguito
 */
public interface personaServicio {
    
    //CRUD
    //Recibe la clase persona del domain
    //Convencion: Mayuscula clase y variable o instancia en minuscula
    
    public List<Persona> listar();
    
    public void guardar(Persona per);
    
    public void eliminar(Persona per);
    
    public Persona buscarPorId(Persona per);

    public Persona buscarPorId(Long idPersona);

    public List<Persona> listarClientes();
    
}
