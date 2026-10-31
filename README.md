# RPG Realms

Mod RPG para **Minecraft 1.20.1 (Forge 47.3.0)**.

## Contenido actual
- **Cría de Leviatán** (~18 bloques): pacífica y muy rápida; 70 de vida y poco daño. Aparece en océanos profundos. Solo se defiende si la golpeas.
- **Leviatán** (~44 bloques): colosal, lento y de aspecto propio (cuerpo negro de obsidiana con venas cian, cabeza enorme con boca abierta y colmillos, coraza de placas, vela dorsal alta, anzuelo luminoso). 800 de vida, barra de jefe. Solo con huevo generador por ahora (la dimensión se rehará como planeta).
  - Con más del 50 % de vida va de frente a morder al jugador.
  - Con 50 % o menos se enfurece (barra roja) y prefiere el rayo; bajo el 25 % casi solo lo usa.
  - Ataques: orbes abisales con explosión en área y rayo cargado con haz luminoso.
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
