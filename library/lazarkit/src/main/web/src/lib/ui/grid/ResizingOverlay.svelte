<script lang="ts">
  import { modular } from "./logic/modular"
  import type { PresetManager } from "./logic/preset.svelte"

  let { gridManager }: { gridManager: PresetManager } = $props()

  function getClosest(x: number, y: number) {
    if (modular.showWidget(x, y)) return { x, y }

    let minDist = Infinity
    let closest = { x, y }

    for (let iy = 1; iy <= gridManager.cellsY; iy++) {
      for (let ix = 1; ix <= gridManager.cellsX; ix++) {
        if (modular.showWidget(ix, iy)) {
          const dist = Math.abs(ix - x) + Math.abs(iy - y)
          if (dist < minDist) {
            minDist = dist
            closest = { x: ix, y: iy }
          }
        }
      }
    }

    return closest
  }
</script>

{#each Array.from( { length: gridManager.cellsX * gridManager.cellsY } ) as _, index}
  {@const y = Math.floor(index / gridManager.cellsX) + 1}
  {@const x = (index % gridManager.cellsX) + 1}
  {@const id = gridManager.widgetsMap[y][x]}
  {#if id == null || modular.resizing.resizingModule?.id == id || modular.moving.movingModule?.id == id || false}
    {@const isShown = modular.showWidget(x, y)}
    <div
      class="overlay-item"
      class:isShown
      data-x={x}
      data-y={y}
      style="grid-row: {y} / span 1; grid-column: {x} / span 1;"
    >
      <div class="color"></div>
    </div>
  {/if}
{/each}

<style>
  .overlay-item {
    z-index: 100;
    padding: 0.25rem;
    display: none;
  }
  .overlay-item.isShown {
    display: block;
    opacity: 0.25;
  }
  .color {
    width: 100%;
    height: 100%;
    border: 1px solid var(--primary);
    background-color: var(--primary);
    border-radius: 4px;
  }
</style>
