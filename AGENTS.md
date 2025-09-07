# Repository Guidelines

## Project Structure & Module Organization
- `src/main/java/com/springData/`: application code (controllers, services, repositories, domain, config, DTOs).
- `src/main/resources/`: Spring resources — `templates/` (Thymeleaf views), `static/` (assets like CSS), `application.properties`.
- `src/test/java/`: tests (unit and Spring Boot integration).
- `pom.xml`: Maven build; dependencies and plugins.
- `target/`: build output (generated; do not commit).

## Build, Test, and Development Commands
- `mvn clean package`: compile, run tests, and build the JAR in `target/`.
- `mvn spring-boot:run`: run the app in dev mode with live recompilation.
- `mvn test`: execute the full test suite.
- `java -jar target/<artifact>.jar`: run the packaged application.

## Coding Style & Naming Conventions
- Java conventions: 4-space indentation, braces on same line, meaningful names.
- Packages: lowercase (e.g., `com.springData.controller`). Classes: `PascalCase`. Methods/fields: `camelCase`.
- Controllers in `controller/`, services in `servicio/`, repositories `*Repository` in root, JPA entities in `domain/`.
- Thymeleaf templates mirror feature modules (e.g., `templates/ventas/`), partials like `navbar.html`, `footer.html`.
- Keep controllers thin; move business logic into `servicio` layer.

## Testing Guidelines
- Framework: JUnit with Spring Boot testing utilities.
- Location: `src/test/java/...`. Name test classes `*Tests.java` (e.g., `PruebaSpringThymeleafApplicationTests`).
- Unit tests for services/repositories; use `@SpringBootTest` for integration as needed.
- Run via `mvn test`; target critical paths and edge cases.

## Commit & Pull Request Guidelines
- Commits: concise imperative subject (50 chars max) + brief body when helpful.
- Suggested types: `feat`, `fix`, `refactor`, `docs`, `test`, `chore` (e.g., `feat: agregar CRUD de productos`).
- PRs: clear description, linked issue ID if applicable, screenshots for UI changes, and steps to verify.
- Ensure branch builds, tests pass, and no unrelated changes.

## Security & Configuration Tips
- Copy `src/main/resources/application.properties.example` to `application.properties` and set DB credentials, locale, and security settings.
- Never commit secrets; prefer environment variables or local overrides.
- Review `SecurityConfig.java` and controller access annotations when adding routes; verify 403 page rendering.

