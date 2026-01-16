package com.springData.config;

import com.springData.RolRepository;
import com.springData.ProveedorRepository;
import com.springData.domain.Rol;
import com.springData.domain.Proveedor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedData(RolRepository rolRepository, ProveedorRepository proveedorRepository) {
        return args -> {
            // Roles
            if (rolRepository.count() == 0) {
                String[] nombres = {"ADMIN", "USER", "VIEWER"};
                for (String n : nombres) {
                    Rol r = new Rol();
                    r.setNombre(n);
                    rolRepository.save(r);
                }
            }
            // Proveedor de ejemplo
            if (proveedorRepository.count() == 0) {
                Proveedor p = new Proveedor();
                p.setNombre("Proveedor Demo");
                p.setContacto("Contacto Demo");
                p.setTelefono("0000-0000");
                p.setEmail("proveedor@demo.local");
                p.setDireccion("Calle Falsa 123");
                proveedorRepository.save(p);
            }
        };
    }
}

