# Proyecto Final – Spring Boot + Thymeleaf

Aplicación web de gestión (Personas, Productos, Ventas, etc.) construida con Spring Boot 2.7.x, Thymeleaf, JPA y MySQL.

## Requisitos
- Java 17 (`java -version`)
- Maven 3.8+ (`mvn -v`)
- MySQL 8 (o compatible)
- Git (para clonar/subir a GitHub)

## Configuración
1. Copia el archivo de ejemplo y completa tus credenciales locales:
   - `cp src/main/resources/application.properties.example src/main/resources/application.properties`
   - Edita `src/main/resources/application.properties` y ajusta:
     - `spring.datasource.url`
     - `spring.datasource.username`
     - `spring.datasource.password`
2. (Opcional) Cambia el puerto en `server.port` si 8080 está ocupado.

## Ejecución en desarrollo
- `mvn spring-boot:run`
- Abre: `http://localhost:8080`

## Build del JAR
- `mvn clean package`
- Ejecuta el artefacto:
  - `java -jar target/pruebaSpringThymeleaf-0.0.1-SNAPSHOT.jar`

## Base de datos
- Propiedades relevantes en `application.properties`:
  - `spring.datasource.url=jdbc:mysql://localhost:3306/bd_proyectofinal`
  - `spring.jpa.hibernate.ddl-auto=update`
  - Dialecto: `org.hibernate.dialect.MySQL8Dialect`

## Seguridad
- Se incluye Spring Security. Ajusta reglas/usuarios según lo que tengas configurado en el proyecto (controladores y configuración no incluidos en este README).

## Estilos y fragmentos
- Navbar: `templates/navbar.html` (fragmento `navbar`).
- Footer global (fijo): `templates/footer.html` (fragmento `footer`).
  - Todas las vistas incluyen `th:replace="footer :: footer"` para un pie consistente.

## Buenas prácticas
- No subas credenciales: `src/main/resources/application.properties` ya está en `.gitignore`.
- Usa `application.properties.example` para compartir configuración de referencia.
- Activa logs según necesidad (`logging.level.*`).

## Subir a GitHub (resumen)
- `git init`
- `git add .`
- `git commit -m "Inicial: proyecto Spring Boot"`
- `git branch -M main`
- `git remote add origin https://github.com/TU_USUARIO/TU_REPO.git`
- `git push -u origin main`

## Problemas comunes
- Error formateo fechas en Thymeleaf: usar `LocalDate#format(T(java.time.format.DateTimeFormatter).ofPattern('dd/MM/yyyy'))` o añadir `thymeleaf-extras-java8time` y `#temporals`.
- Footer solapando contenido: el fragmento incluye `padding-bottom` CSS para evitarlo. Ajusta si lo necesitas.

---
Cualquier mejora o duda, crea un issue o PR en el repositorio.
