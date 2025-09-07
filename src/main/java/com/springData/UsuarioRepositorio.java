package com.springData;

import com.springData.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {

    
    List<Usuario> findByRol_Id(Long rolId);  // Agregado para buscar por rol

}
