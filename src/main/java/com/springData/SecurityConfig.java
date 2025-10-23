package com.springData;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("admin").password(passwordEncoder().encode("admin123")).roles("ADMIN")
            .and()
            .withUser("user").password(passwordEncoder().encode("user123")).roles("USER")
            .and()
            .withUser("viewer").password(passwordEncoder().encode("viewer123")).roles("VIEWER");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/login", "/resources/**", "/css/**", "/js/**", "/images/**").permitAll()
                .antMatchers("/", "/index").permitAll()
                .antMatchers(
                        "/productos/**", "/clientes/**",
                        "/categorias/**", "/marcas/**",
                        "/ventas/**", "/presupuestos/**", "/pedidos/**", "/remitos/**", "/facturas/**"
                ).hasAnyRole("ADMIN","USER","VIEWER")
                .antMatchers(HttpMethod.GET,  "/**/nuevo", "/**/editar/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/**/guardar", "/**/eliminar/**", "/**/anular/**", "/facturas/emitir/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            .and()
            .formLogin().loginPage("/login").permitAll()
            .and()
            .logout().permitAll()
            .and()
            .exceptionHandling().accessDeniedHandler(accessDeniedHandler)
            .and()
            .csrf().disable();
    }
}
