# RPG Realms

Mod RPG para **Minecraft 1.20.1 (Forge 47.3.0)**.

## Contenido actual
- **Cría de Leviatán** (~18 bloques): pacífica y muy rápida; 70 de vida y poco daño. Aparece en océanos profundos y en el Abismo. Solo se defiende si la golpeas.
- **Leviatán** (~44 bloques): colosal y lento; 800 de vida, barra de jefe. Vive en los lagos del Abismo. Mordisco, ráfaga de orbes abisales (con estela y explosión en área) y un rayo cargado con haz luminoso real, destello de carga y retroceso del cuerpo. Anzuelo luminoso, bioluminiscencia (cian en calma, roja al enfadarse, blanca al disparar el rayo).
- **El Abismo**: dimensión de cuevas gigantes y oscuras con su propio bioma (*Abismo Luminoso*), suelo caminable, lagos, Piedra Lumen y Cristales Lumen.
- **Perla Abisal**: se craftea con perla de ender + corazón del mar + saco de tinta luminoso. Úsala para entrar al Abismo y, dentro, para volver.
- Huevos generadores de ambos y pestaña creativa propia.

## Cómo obtener el .jar con GitHub
1. Sube todo este proyecto a tu repositorio.
2. Ve a la pestaña **Actions** y abre la ejecución de **Build**.
3. Al terminar, descarga el artefacto **rpgrealms-jar** (dentro está el `.jar`).
4. Pon el `.jar` en la carpeta `mods` de tu Minecraft 1.20.1 con Forge instalado.

## Ajustes rápidos
- `LeviathanEntity.PROVOKED_ONLY`: `true` = el adulto solo ataca si lo hieren; `false` = ataca a los jugadores cercanos.
- Escala de los modelos: `LeviathanCubRenderer` (1.8) y `LeviathanRenderer` (4.5).

## Compilar en local
Requiere JDK 17: `gradle build` (el jar queda en `build/libs/`).
