package com.springData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails admin = User
                .withUsername("admin")
                .password(encoder.encode("admin123"))
                .roles("ADMIN")
                .build();
        UserDetails user = User
                .withUsername("user")
                .password(encoder.encode("user123"))
                .roles("USER")
                .build();
        UserDetails viewer = User
                .withUsername("viewer")
                .password(encoder.encode("viewer123"))
                .roles("VIEWER")
                .build();
        return new InMemoryUserDetailsManager(admin, user, viewer);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests(auth -> auth
                .antMatchers("/login", "/resources/**", "/css/**", "/js/**", "/images/**").permitAll()
                .antMatchers("/", "/index").permitAll()
                .antMatchers(
                        "/productos/**", "/clientes/**",
                        "/categorias/**", "/marcas/**", "/proveedores/**",
                        "/ventas/**", "/presupuestos/**", "/pedidos/**", "/remitos/**", "/facturas/**"
                ).hasAnyRole("ADMIN","USER","VIEWER")
                .antMatchers(HttpMethod.GET,  "/**/nuevo", "/**/editar/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/**/guardar", "/**/eliminar/**", "/**/anular/**", "/facturas/emitir/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(login -> login.loginPage("/login").permitAll())
            .logout(logout -> logout.permitAll())
            .exceptionHandling(ex -> ex.accessDeniedHandler(accessDeniedHandler))
            .csrf(csrf -> csrf.disable());

        return http.build();
    }
}

