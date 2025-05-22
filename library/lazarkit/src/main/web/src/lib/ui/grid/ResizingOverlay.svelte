<script lang="ts">
  import type { Grid } from "./grid.svelte"
  import { hover } from "./hover.svelte"
  let { gridManager }: { gridManager: Grid } = $props()
</script>

{#each Array.from( { length: gridManager.cellsX * gridManager.cellsY } ) as _, index}
  {@const y = Math.floor(index / gridManager.cellsX) + 1}
  {@const x = (index % gridManager.cellsX) + 1}
  {@const id = gridManager.modulesMap[y][x]}
  {#if id == null || hover.resizingModule?.id == id}
    <p
      class="overlay-item"
      data-x={x}
      data-y={y}
      style="grid-row: {y} / span 1; grid-column: {x} / span 1;"
    ></p>
  {/if}
{/each}

<style>
  .overlay-item {
    border: 1px solid var(--primary);
    border-radius: 16px;
    margin: 0;
    z-index: 100;
  }
</style>
