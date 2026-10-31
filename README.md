# RPG Realms

Mod RPG para **Minecraft 1.20.1 (Forge 47.3.0)**.

## Contenido actual
- **Cría de Leviatán** (~26 bloques): pacífica y muy rápida; 160 de vida y 14 de daño. Aparece en océanos profundos. Solo se defiende si la golpeas.
- **Leviatán** (~64 bloques): colosal, lento y de aspecto propio (cuerpo negro de obsidiana con venas cian, cabeza enorme con boca abierta y colmillos, coraza de placas, vela dorsal alta, anzuelo luminoso). 1500 de vida, barra de jefe. Solo con huevo generador por ahora (la dimensión se rehará como planeta).
  - Con más del 50 % de vida va de frente a morder al jugador.
  - Con 50 % o menos se enfurece (barra roja) y prefiere el rayo; bajo el 25 % casi solo lo usa.
  - Ataques: orbes abisales con explosión en área y rayo cargado con haz luminoso.
- **Efectos:** ambos dejan un rastro de partículas místicas por todo el cuerpo al moverse (más denso al nadar rápido; rojo al enfurecerse). La bioluminiscencia es un patrón ordenado (línea lateral, franjas, bordes de aletas) que brilla poco de día y se enciende de noche, en cuevas y al atacar.
- Huevos generadores de ambos y pestaña creativa propia.

## Cómo obtener el .jar con GitHub
1. Sube todo este proyecto a tu repositorio.
2. Ve a la pestaña **Actions** y abre la ejecución de **Build**.
3. Al terminar, descarga el artefacto **rpgrealms-jar** (dentro está el `.jar`).
4. Pon el `.jar` en la carpeta `mods` de tu Minecraft 1.20.1 con Forge instalado.

## Ajustes rápidos
- `LeviathanEntity.PROVOKED_ONLY`: `true` = el adulto solo ataca si lo hieren; `false` = ataca a los jugadores cercanos.
- Escala de los modelos: `LeviathanCubRenderer` (2.6) y `LeviathanRenderer` (6.5); hitboxes en `ModEntities`.
- Velocidad de nado: `cruiseSpeed()` en cada entidad; giro en el constructor (`super(type, level, grados_por_tick)`).

## Compilar en local
Requiere JDK 17: `gradle build` (el jar queda en `build/libs/`).
