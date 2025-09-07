
package com.springData;

import com.springData.domain.Ciudad;
import com.springData.dto.CiudadPaisDTO;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface ciudadDAO extends CrudRepository<Ciudad, Long> {
    @EntityGraph(attributePaths = {"MostrarPais"}) 
    @Query("SELECT new com.springData.dto.CiudadPaisDTO(c.ciudad,p.paDes,c.ciuEs)FROM Ciudad c LEFT JOIN c.idPais p")
    List<CiudadPaisDTO> ObtenerCiudadesYPaises();
}
