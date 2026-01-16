
package com.springData;

import com.springData.domain.Persona;
import com.springData.dto.PersonaCiudadDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PersonaRepository extends JpaRepository<Persona, Long> {
    @EntityGraph(attributePaths = {"ciudad"})
    @Query("SELECT new com.springData.dto.PersonaCiudadDTO(p.nombre,p.apellido,p.edad,p.telefono,"
            + "p.correo,p.direccion,c.ciudad,p.idPais)"
            + "FROM Persona p LEFT JOIN p.ciudad c")
    List<PersonaCiudadDTO> ObtenerPersonasYCiudades();

    List<Persona> findByEsClienteTrue();
    Page<Persona> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(String nombre, String apellido, Pageable pageable);
}
