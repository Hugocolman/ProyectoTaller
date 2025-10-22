# Repository Guidelines

## Project Structure & Module Organization
- `src/main/java/com/springData/`: app code — `controller/`, `servicio/`, `domain/`, `config/`, `dto/`, repositories (`*Repository`).
- `src/main/resources/`: Spring resources — `templates/` (Thymeleaf views), `static/` (assets), `application.properties`.
- `src/test/java/`: unit and Spring Boot integration tests.
- `docs/`: project notes and flows; `scripts/`: helper scripts.
- `pom.xml`: Maven build; `target/`: build output (do not commit).

## Build, Test, and Development Commands
- `mvn clean package`: compile, run tests, and build the JAR in `target/`.
- `mvn spring-boot:run`: start locally with live reload.
- `mvn test`: run the test suite.
- `java -jar target/<artifact>.jar`: run the packaged app.
Example: `mvn clean package -DskipTests` for faster local builds.

## Coding Style & Naming Conventions
- Java style: 4 spaces, no tabs; braces on same line; max ~120 cols.
- Packages lowercase (`com.springData.controller`); classes `PascalCase`; methods/fields `camelCase`.
- Controllers in `controller/`, services in `servicio/`, entities in `domain/`, DTOs in `dto/`.
- Keep controllers thin; move business logic into `servicio`.
- Templates mirror features (e.g., `templates/ventas/`); reuse partials (`navbar.html`, `footer.html`).

## Testing Guidelines
- Frameworks: JUnit + Spring Boot Test.
- Location: `src/test/java/...`; name classes `*Tests.java` (e.g., `PruebaSpringThymeleafApplicationTests`).
- Write unit tests for services/repos; use `@SpringBootTest` for integration paths.
- Run via `mvn test`; aim for meaningful coverage on critical flows.

## Commit & Pull Request Guidelines
- Commits: imperative subject (≤50 chars) + brief body when needed.
- Suggested types: `feat`, `fix`, `refactor`, `docs`, `test`, `chore` (e.g., `feat: agregar CRUD de productos`).
- PRs: clear description, linked issues, screenshots for UI, and verification steps.
- Ensure builds/tests pass and avoid unrelated diffs.

## Security & Configuration Tips
- Copy `src/main/resources/application.properties.example` to `application.properties`; set DB creds and locales.
- Do not commit secrets; prefer environment variables/local overrides.
- Review `SecurityConfig.java` and controller annotations when adding routes.

