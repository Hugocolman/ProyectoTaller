
package com.springData.servicio;


import com.springData.domain.Ciudad;
import com.springData.domain.Pais;
import java.util.List;


public interface ciudadServicio {
    
    public List<Ciudad> listaCiudades();
    
    public void guardar(Ciudad ciuServ);
    
    public void eliminar(Ciudad ciuServ);
    
    public Ciudad buscarPorId(Ciudad ciuServ);

    public List<Ciudad> buscarPorPais(Pais pais);
}
