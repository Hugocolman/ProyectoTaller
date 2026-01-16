# Guía de Recuperación Rápida

Esta guía te ayuda a restaurar vistas/estáticos desde `target/`, verificar ejecución, y detectar código compilado sin fuente.

## 1) Sincronizar recursos (plantillas y static)
- Vista previa (no copia): `pwsh scripts/sync-from-target.ps1` (WhatIf=true por defecto)
- Copiar realmente: `pwsh scripts/sync-from-target.ps1 -WhatIf false`
- Revisa `src/main/resources/templates` y `src/main/resources/static` tras la copia.

## 2) Ejecutar la aplicación
- Requisitos: MySQL activo y base `bd_proyectofinal` accesible.
- Comando: `mvn clean spring-boot:run`
- Rutas a validar: `/login`, `/`, `/ventas`, `/productos`, `/compras`, `/clientes`, `/proveedores`, `/usuarios`.

## 3) Restaurar fuente desde clases compiladas (si falta)
- Abrir `.class` de `target/classes` en el IDE (decompilador integrado) y recrear `.java` en `src/main/java/...`.
- Alternativa CLI (bytecode): `javap -p -c -classpath target/classes com.springData.servicio.ProductoServicioImp`

## 4) Seguridad y configuración
- Copiar `src/main/resources/application.properties.example` a `application.properties` y ajustar credenciales.
- Revisar `SecurityConfig.java` y accesos de controladores; validar página 403.

## 5) Control de versiones
- Inicializa Git si no está: `git init`
- Ignora artefactos: ver `.gitignore` incluido.
- Primer commit: `git add . && git commit -m "chore: base de recuperación"`

***
Consejo: tras cada recuperación, corre `mvn test` para detectar roturas tempranas.
