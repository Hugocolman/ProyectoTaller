    package com.springData;

import com.springData.domain.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long> {
    // No es necesario escribir nada aquí por ahora. JpaRepository ya proporciona
    // métodos como save(), findAll(), findById(), delete(), etc.
}
