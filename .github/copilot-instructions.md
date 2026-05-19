# Instrucciones de Copilot para el repositorio agent-lab-java

## Objetivo del proyecto
Soc Ops es una aplicación Java Spring Boot que implementa un juego de Social Bingo para encuentros presenciales.
- Backend ligero en `socops/src/main/java/com/socops`
- Frontend servido desde `socops/src/main/resources/templates/game.html`
- Tablero generado por `socops/src/main/java/com/socops/service/BoardAssembler.java`
- API REST disponible en `/api/bingo/fresh-board`

## Comandos principales
- `cd socops && ./mvnw spring-boot:run`
- `cd socops && ./mvnw clean package`
- `cd socops && ./mvnw test`

## Qué debe hacer el agente
- Responder en español siempre que el usuario pida instrucciones en español.
- Priorizar cambios dentro de `socops/` y mantener la aplicación pequeña y simple.
- Evitar agregar dependencias nuevas sin necesidad.
- Seguir las prácticas de Spring Boot y Java modernas, usando clases, registros y pruebas unitarias existentes.
- Revisar `game.html` y `app.css` antes de proponer cambios de estilo frontend.

## Estructura relevante
- `socops/src/main/java/com/socops/SocOpsApplication.java`
- `socops/src/main/java/com/socops/web/BingoRestController.java`
- `socops/src/main/java/com/socops/service/BoardAssembler.java`
- `socops/src/main/java/com/socops/data/IcebreakerPrompts.java`
- `socops/src/main/java/com/socops/model/`
- `socops/src/main/resources/templates/game.html`
- `socops/src/main/resources/static/css/app.css`
- `socops/src/test/java/com/socops/service/BoardAssemblerTests.java`

## Convenciones útiles
- El proyecto usa Java 21 y Spring Boot 3.x.
- El frontend es HTML/JS sin frameworks de JavaScript.
- El código ya incluye pruebas unitarias para la lógica del bingo; mantener y ampliar esas pruebas cuando sea posible.
- No reformular el diseño del juego sin motivo; mejorar la experiencia dentro del estilo existente es preferible.

## Documentación
- `README.es.md` para instrucciones generales de uso y ejecución.
- `workshop/es/GUIDE.md` para el flujo del laboratorio en español.

## Nota
Este archivo es la guía principal para agentes de Copilot en este repositorio. No es necesario crear nuevos archivos de configuración de agente a menos que se requiera un flujo de trabajo específico.

## Lineamientos de diseño
- Crea un estilo consistente y coherente con el tema elegido para la aplicación.
- Usa variables CSS para colores, tipografías y sombras cuando sea posible.
- Prefiere animaciones sutiles y envolventes en lugar de efectos llamativos.
- Conserva la simpleza del frontend actual: HTML y CSS nativos, sin dependencias nuevas.
- Revisa `game.html` y `app.css` antes de proponer cambios de estilo.
