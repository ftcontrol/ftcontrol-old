<script lang="ts">
  import BaseWidget from "./BaseWidget.svelte"
  import type { PresetManager } from "./logic/preset.svelte"
  import ResizingOverlay from "./ResizingOverlay.svelte"

  let { gridManager }: { gridManager: PresetManager } = $props()
</script>

<section style="--cellsX:{gridManager.cellsX} ;--cellsY: {gridManager.cellsY};">
  {#each gridManager.widgets as w}
    <div
      class="widget"
      data-id={w.id}
      style="
        grid-column: {w.start.x} / span {w.sizes.x};
        grid-row: {w.start.y} / span {w.sizes.y};
        "
    >
      <div class="content">
        <BaseWidget m={w} {gridManager}></BaseWidget>
      </div>
    </div>
  {/each}

  <ResizingOverlay {gridManager} />
</section>

<style>
  section {
    --cellsX: 12;
    --cellsY: 12;
    display: grid;
    grid-template-columns: repeat(var(--cellsX), 1fr);
    grid-template-rows: repeat(var(--cellsY), 1fr);
    height: 100%;
    width: 100%;
    padding: 0.5rem;
    position: relative;
  }
  .widget {
    position: relative;
    border-radius: 16px;
    overflow: hidden;
    transition: background-color var(--d3);
    height: 100%;
    padding: 0.25rem;
  }
  .content {
    height: 100%;
  }
</style>
