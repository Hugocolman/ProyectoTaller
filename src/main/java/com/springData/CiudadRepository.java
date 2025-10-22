
package com.springData;

import com.springData.domain.Ciudad;
import com.springData.domain.Pais;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("ciudadRepository")
public interface CiudadRepository extends CrudRepository<Ciudad, Long> {
    List<Ciudad> findByIdPais(Pais pais);
}
