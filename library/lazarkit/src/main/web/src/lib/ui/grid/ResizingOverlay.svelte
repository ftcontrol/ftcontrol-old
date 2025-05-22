<script lang="ts">
  import type { Grid } from "./grid.svelte"
  import { hover } from "./hover.svelte"
  let { gridManager }: { gridManager: Grid } = $props()
</script>

{#each Array.from( { length: gridManager.cellsX * gridManager.cellsY } ) as _, index}
  {@const y = Math.floor(index / gridManager.cellsX) + 1}
  {@const x = (index % gridManager.cellsX) + 1}
  {@const id = gridManager.modulesMap[y][x]}
  {#if id == null || hover.resizingModule?.id == id || hover.movingModule?.id == id}
    <div
      class="overlay-item"
      class:isShown={hover.showWidget(x, y)}
      class:isEmpty={id == null && !hover.isMov && !hover.isResizing}
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
  }
  .color {
    width: 100%;
    height: 100%;
    border: 1px solid var(--primary);
    background-color: var(--primary);
    border-radius: 16px;
  }
  .overlay-item:hover .color {
    background-color: var(--primary);
  }
  .overlay-item.isShown .color {
    background-color: transparent;
  }
  .overlay-item.isEmpty {
    display: none;
  }
</style>
