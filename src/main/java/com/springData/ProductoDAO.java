package com.springData;

import com.springData.domain.Producto;  // Cambiar a Producto
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository  // Mantener la anotación Repository para marcarla como un bean
public interface ProductoDAO extends CrudRepository<Producto, Long> {  // Cambiar a Producto
    // Aquí puedes añadir métodos personalizados si lo necesitas
}
