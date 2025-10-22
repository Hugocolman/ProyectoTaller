package com.springData;

import com.springData.domain.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    // Puedes agregar métodos personalizados si los necesitas, por ejemplo:
    // List<Rol> findByNombre(String nombre);
}
