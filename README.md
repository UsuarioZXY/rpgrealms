# Blue Lock Mod (Forge 1.20.1)

Primera versión: balón de fútbol + porterías con detección de gol.

## Cómo jugar
- **Balón de fútbol** (pestaña creativa "Blue Lock"): click derecho para soltarlo frente a ti.
  Receta: 4 cuero + 1 bola de slime (forma de cruz).
- **Dribleo automático**: si te acercas, el balón se pega delante de ti y te sigue mientras caminas
  (más lejos si corres). **Agáchate (shift)** para soltarlo.
- **Patear**: click izquierdo sobre el balón, o la tecla **R** (configurable en Controles > Blue Lock).
  Con R es una **patada cargada**: toque rápido = suave, mantenerla ~1 segundo = máxima potencia
  (sale una barra sobre la hotbar). Corriendo es más fuerte; mirar hacia arriba la hace más alta.
- **Levantar el balón**: salta mientras lo llevas y lo levantas suave hacia adelante (no es un tiro).
- **Quitar el balón**: acércate más al balón que el jugador que lo lleva.
- **Balón en 3D**: es una esfera que gira según rueda. Tras un gol reaparece en el punto donde lo soltaste.
- **Recoger el balón**: shift + click derecho sobre él.
- **Portería**: item "Portería", click derecho en el suelo. Construye una portería de 5x3 mirando hacia ti.
  Cuando el balón entra en la red sale **¡GOL!** en pantalla, con sonido y partículas.
  También hay bloques sueltos: Poste de portería y Red de portería.

## Compilar con GitHub
1. Crea un repositorio en GitHub y sube TODO el contenido de esta carpeta (incluida la carpeta `.github`).
2. Ve a la pestaña **Actions** > "Build mod" (se ejecuta solo al hacer push).
3. Al terminar, entra a la ejecución y descarga el artefacto **bluelock-jar**: ahí está el `.jar`.
4. Copia el `.jar` a la carpeta `mods` de Minecraft 1.20.1 con Forge 47.x.

Requisitos: Java 17 (solo para compilar en tu PC; en GitHub ya está configurado).
