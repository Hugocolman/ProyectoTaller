
package com.springData.servicio;

import com.springData.CiudadRepository;
import com.springData.domain.Ciudad;
import com.springData.domain.Pais;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ciudadServiceImp implements ciudadServicio{
    
    @Autowired 
    private CiudadRepository ciudadRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Ciudad> listaCiudades() {
        return (List<Ciudad>) ciudadRepository.findAll();
    }

    @Override
    @Transactional
    public void guardar(Ciudad ciuServ) {
        ciudadRepository.save(ciuServ);
    }

    @Override
    @Transactional
    public void eliminar(Ciudad ciuServ) {
        ciudadRepository.delete(ciuServ);
    }

    @Override
    @Transactional(readOnly = true)
    public Ciudad buscarPorId(Ciudad ciuServ) {
        return ciudadRepository.findById(ciuServ.getIdCiudad()).orElse(ciuServ);
    }

    @Override
    public List<Ciudad> buscarPorPais(Pais pais) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
