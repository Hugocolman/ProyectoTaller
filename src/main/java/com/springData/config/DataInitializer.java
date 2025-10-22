package com.springData.config;

import com.springData.RolRepository;
import com.springData.domain.Rol;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedRoles(RolRepository rolRepository) {
        return args -> {
            if (rolRepository.count() == 0) {
                String[] nombres = {"ADMIN", "USER", "VIEWER"};
                for (String n : nombres) {
                    Rol r = new Rol();
                    r.setNombre(n);
                    rolRepository.save(r);
                }
            }
        };
    }
}

