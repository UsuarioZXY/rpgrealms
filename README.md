# RPG Realms

Mod RPG para **Minecraft 1.20.1 (Forge 47.3.0)**.

## Contenido actual
- **Cría de Leviatán** (~30 bloques, diseño de ballena): pacífica y muy rápida; 160 de vida y 14 de daño. Aparece en océanos profundos. Solo se defiende si la golpeas.
- **Leviatán** (~65 bloques): colosal, lento y con diseño de ballena (lomo oscuro, vientre claro con pliegues, cabeza con melón, aletas pectorales largas, cola ancha, anzuelo luminoso y boca con dos hileras de dientes pequeños). 1500 de vida, barra de jefe. Solo con huevo generador por ahora (la dimensión se rehará como planeta).
  - Con más del 50 % de vida va de frente a morder al jugador.
  - Con 50 % o menos se enfurece (barra roja) y prefiere el rayo; bajo el 25 % casi solo lo usa.
  - Ataques: orbes abisales con explosión en área y rayo cargado con haz luminoso.
- **Efectos:** ambos dejan un rastro de partículas místicas por todo el cuerpo al moverse (más denso al nadar rápido; rojo al enfurecerse). La bioluminiscencia es un patrón ordenado (línea lateral, franjas, bordes de aletas) que brilla poco de día y se enciende de noche, en cuevas y al atacar.
- **Huevo de Leviatán** (objeto): se coloca como entidad (en tierra o agua), se mece y a los 3 min eclosiona en una Cría; golpearlo lo recoge. La Cría crece y a los 20 min se convierte en Leviatán adulto (si hay espacio). El adulto lo suelta con 50 % de probabilidad. Tiempos: `LeviathanEggEntity.HATCH_TICKS` y `LeviathanCubEntity.GROW_TICKS`.
- **Vida propia:** el adulto parpadea y tiene iris luminosos con pupila vertical; en tierra ambos dan saltos de pez (hacia el agua más cercana) y tiemblan; las crías saltan fuera del agua como mini delfines. Los orbes salen en abanico (3, o 5 herido), se curvan hacia el objetivo y explotan en ondas de choque.
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

## Tres etapas: Cría → Adolescente → Adulto
Diseño tipo ballena + tiburón + un toque de mosasaurio (hocico largo, aleta dorsal alta, cola vertical, aletas pectorales largas).
- **Cría** (HP 160): crece en ~10 min y pasa a adolescente.
- **Adolescente** (HP 450, daño 26, pacífico, sin ataques a distancia): crece en ~20 min y pasa a adulto. Huevo generador en la pestaña creativa.
- **Adulto** (HP 1500): orbes y rayo.
Si no hay espacio para la siguiente etapa, reintenta cada pocos segundos.

## Rediseño realista (v3)
Cuerpo más ancho y redondeado con afinado suave hacia la cola, aleta dorsal baja tipo ballena y aletas pectorales anchas. Texturas naturales: contraluz (lomo oscuro, vientre claro), moteado, cicatrices, pliegues ventrales y ojos pequeños oscuros. El brillo bioluminiscente se mantiene pero más tenue.
