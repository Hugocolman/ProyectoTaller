
package com.springData;

import com.springData.domain.Pais;
import org.springframework.data.repository.CrudRepository;


/**
 *
 * @author Huguito
 */
public interface paisDAO extends CrudRepository<Pais, Long>{
    
}
