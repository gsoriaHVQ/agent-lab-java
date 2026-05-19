<!-- l10n-sync: source-file="README.md" -->
# Soc Ops

## 🎉 Juego de Social Bingo para encuentros presenciales

Soc Ops convierte tus reuniones en una experiencia interactiva: crea un tablero con preguntas de rompehielos, busca coincidencias con otras personas y marca casillas hasta lograr un bingo.

- ✅ Tablero 5×5 con casilla libre preseleccionada
- ✅ Generación de preguntas aleatorias y dinámicas
- ✅ Interfaz ligera con HTML, CSS y JavaScript
- ✅ Backend Java Spring Boot simple y fácil de extender

---

## 🚀 ¿Qué puedes hacer aquí?

- Jugar un bingo social desde el navegador
- Crear un nuevo tablero con preguntas distintas cada vez
- Marcar casillas al encontrar personas que coincidan con las preguntas
- Ver el aviso de BINGO cuando completes una línea

---

## 🧠 Cómo jugar

1. Ejecuta la aplicación localmente.
2. Abre el navegador en `http://localhost:8080`.
3. Presiona **Start Game** para generar un tablero.
4. Toca una casilla cada vez que encuentres a alguien que cumpla esa condición.
5. Consigue 5 casillas seguidas en fila, columna o diagonal para ganar.

---

## 🛠️ Ejecución rápida

```bash
cd socops
./mvnw spring-boot:run
```

Luego, abre:

```text
http://localhost:8080
```

---

## 📦 Compilar y pruebas

```bash
cd socops
./mvnw clean package
./mvnw test
```

---

## 📁 Estructura clave

- `socops/src/main/java/com/socops/SocOpsApplication.java` — punto de entrada de Spring Boot
- `socops/src/main/java/com/socops/web/BingoRestController.java` — controlador HTTP y API REST
- `socops/src/main/java/com/socops/service/BoardAssembler.java` — lógica de armado de tablero y detección de victoria
- `socops/src/main/resources/templates/game.html` — interfaz del juego
- `socops/src/main/resources/static/css/app.css` — estilos del juego
- `socops/src/test/java/com/socops/service/BoardAssemblerTests.java` — pruebas unitarias de la lógica de bingo

---

## 📚 Aprende más

- Guía completa del laboratorio: [workshop/es/GUIDE.md](workshop/es/GUIDE.md)
- Documentación del proyecto: `README.es.md`

---

## 🙌 Contribuye

Este proyecto está diseñado para ser pequeño y fácil de extender.
Si deseas mejorar la experiencia, enfócate en:

- mejorar el tablero y los estilos visuales
- agregar más prompts de rompehielos
- reforzar las pruebas de la lógica del juego

---

## 🌐 Deploy

Se despliega automáticamente a GitHub Pages con cada push a la rama `main`.
