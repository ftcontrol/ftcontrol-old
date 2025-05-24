<script lang="ts">
  import { modular } from "./logic/modular"
  import type { PresetManager } from "./logic/preset.svelte"

  let { gridManager }: { gridManager: PresetManager } = $props()
</script>

{#each Array.from( { length: gridManager.cellsX * gridManager.cellsY } ) as _, index}
  {@const y = Math.floor(index / gridManager.cellsX) + 1}
  {@const x = (index % gridManager.cellsX) + 1}
  {@const id = gridManager.widgetsMap[y][x]}
  {#if id == null || modular.resizing.resizingModule?.id == id || modular.moving.movingModule?.id == id}
    <div
      class="overlay-item"
      class:isShown={modular.showWidget(x, y)}
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
    opacity: 0.5;
  }
  .color {
    width: 100%;
    height: 100%;
    border: 1px solid var(--primary);
    background-color: var(--primary);
    border-radius: 4px;
  }
</style>
