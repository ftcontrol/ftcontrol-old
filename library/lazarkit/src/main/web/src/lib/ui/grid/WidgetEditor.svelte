<script lang="ts">
  import { info } from "$lib"
  import { Grid } from "./grid.svelte"
  import Section from "$ui/primitives/Section.svelte"
  import Plus from "$ui/icons/Plus.svelte"
  import BaseWidget from "./BaseWidget.svelte"
  import ResizingOverlay from "./ResizingOverlay.svelte"

  let { gridManager }: { gridManager: Grid } = $props()
</script>

<div
  class="container"
  style="--cellsX:{gridManager.cellsX} ;--cellsY: {gridManager.cellsY};"
>
  <section>
    {#each gridManager.modules as w}
      <div
        class="item"
        class:isOverlay={gridManager.selectedWidgetId == w.id}
        style="
        grid-column: {w.start.x} / span {w.sizes.x};
        grid-row: {w.start.y} / span {w.sizes.y};
        "
      >
        <BaseWidget m={w} {gridManager}></BaseWidget>
      </div>
    {/each}
    {#if gridManager.selectedWidget != null}
      <div
        class="item"
        style="
  grid-column: {gridManager.selectedCellX} / span {gridManager.selectedWidget
          .sizes.x};
  grid-row: {gridManager.selectedCellY} / span {gridManager.selectedWidget.sizes
          .y};
  "
      >
        <Section isPrimary={true}></Section>
      </div>
    {/if}

    {#each Array.from( { length: gridManager.cellsX * gridManager.cellsY } ) as _, index}
      {@const id =
        gridManager.modulesMap[Math.floor(index / gridManager.cellsX) + 1][
          (index % gridManager.cellsX) + 1
        ]}
      <button
        style={id != null ? "display: none;" : ""}
        class:shown={info.showEdit && !gridManager.isMoving}
        class="add"
        onclick={() => {
          gridManager.addNew(
            (index % gridManager.cellsX) + 1,
            Math.floor(index / gridManager.cellsX) + 1
          )
        }}
      >
        <Plus />
      </button>
    {/each}
    <ResizingOverlay {gridManager} />
  </section>
</div>

<style>
  .add {
    transition: opacity var(--d3);
    pointer-events: none;
    display: grid;
    place-items: center;
    opacity: 0;
  }

  .add.shown {
    opacity: 1;
    pointer-events: all;
  }

  button {
    background-color: transparent;
    border: none;
    cursor: pointer;
    user-select: none;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .container {
    --cellsX: 12;
    --cellsY: 12;
    width: 100%;
    height: 100%;
    position: relative;
  }
  section {
    position: absolute;
    display: grid;
    grid-template-columns: repeat(var(--cellsX), 1fr);
    grid-template-rows: repeat(var(--cellsY), 1fr);
    height: 100%;
    width: 100%;
    padding: 0.5rem;
    gap: 0.5rem;
  }
  div.item {
    position: relative;
    background-color: var(--card);
    border-radius: 16px;
    overflow: hidden;
    transition: background-color var(--d3);
    height: 100%;
  }
  div.isOverlay {
    display: none;
    opacity: 0.5;
  }
</style>
