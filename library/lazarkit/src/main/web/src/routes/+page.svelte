<script lang="ts">
  import { gamepads, notifications } from "$lib"
  import GamepadDrawing from "$lib/ui/GamepadDrawing.svelte"
  import { gridManager, type Module } from "$ui/grid/grid.svelte"
  import HorizontalIcon from "$ui/icons/HorizontalIcon.svelte"
  import HorizontalReversed from "$ui/icons/HorizontalReversed.svelte"
  import MoveIcon from "$ui/icons/MoveIcon.svelte"
  import VerticalIcon from "$ui/icons/VerticalIcon.svelte"
  import VerticalReversed from "$ui/icons/VerticalReversed.svelte"
  import GameField from "$ui/widgets/fields/GameField.svelte"
  import Flows from "$ui/widgets/Flows.svelte"
  import Graph from "$ui/widgets/Graph.svelte"
  import PlaybackHistory from "$ui/widgets/PlaybackHistory.svelte"
  import { OpModeControl, Telemetry, Configurables } from "$widgets"
  import { onMount } from "svelte"

  function canExpand(w: Module, dx: number, dy: number): boolean {
    const { x: startX, y: startY } = w.start
    const { x: width, y: height } = w.sizes

    if (dx === 1) {
      // Right
      for (let dyOffset = 0; dyOffset < height; dyOffset++) {
        const y = startY + dyOffset
        const x = startX + width
        if (gridManager.modulesMap[y]?.[x] !== null) return false
      }
    } else if (dx === -1) {
      // Left
      for (let dyOffset = 0; dyOffset < height; dyOffset++) {
        const y = startY + dyOffset
        const x = startX - 1
        if (gridManager.modulesMap[y]?.[x] !== null) return false
      }
    } else if (dy === 1) {
      // Down
      for (let dxOffset = 0; dxOffset < width; dxOffset++) {
        const x = startX + dxOffset
        const y = startY + height
        if (gridManager.modulesMap[y]?.[x] !== null) return false
      }
    } else if (dy === -1) {
      // Up
      for (let dxOffset = 0; dxOffset < width; dxOffset++) {
        const x = startX + dxOffset
        const y = startY - 1
        if (gridManager.modulesMap[y]?.[x] !== null) return false
      }
    }

    return true
  }

  function canExpandRight(w: Module) {
    return canExpand(w, 1, 0)
  }

  function canExpandLeft(w: Module) {
    return canExpand(w, -1, 0)
  }

  function canExpandDown(w: Module) {
    return canExpand(w, 0, 1)
  }

  function canExpandUp(w: Module) {
    return canExpand(w, 0, -1)
  }
</script>

<div class="container">
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
        <div class="controls">
          <button
            onmousedown={() => {
              gridManager.startMoving(w.id)
            }}
          >
            <MoveIcon />
          </button>
          <button
            onclick={() => {
              if (!canExpandRight(w)) {
                notifications.add("Not enough space to expand right.")
              } else {
                w.sizes.x++
                return
              }

              if (!canExpandLeft(w)) {
                notifications.add("Not enough space to expand left.")
              } else {
                w.sizes.x++
                w.start.x--
              }
            }}
          >
            <HorizontalIcon />
          </button>
          <button
            onclick={() => {
              if (w.sizes.x <= 1) {
                notifications.add("Cannot make this small.")
              } else {
                w.sizes.x--
              }
            }}
          >
            <HorizontalReversed />
          </button>
          <button
            onclick={() => {
              if (!canExpandDown(w)) {
                notifications.add("Not enough space to expand down.")
              } else {
                w.sizes.y++
                return
              }

              if (!canExpandUp(w)) {
                notifications.add("Not enough space to expand up.")
              } else {
                w.sizes.y++
                w.start.y--
              }
            }}
          >
            <VerticalIcon />
          </button>
          <button
            onclick={() => {
              if (w.sizes.y <= 1) {
                notifications.add("Cannot make this small.")
              } else {
                w.sizes.y--
              }
            }}
          >
            <VerticalReversed />
          </button>
        </div>
        {#if w.type == "controls"}
          <OpModeControl />
        {:else}
          <p>Unknown type</p>
        {/if}

        <button
          onclick={() => {
            if (w.sizes.x <= 1) {
              notifications.add("Cannot make this small.")
              return
            }
            w.sizes.x--
          }}>Small Right</button
        >

        <button
          onclick={() => {
            if (w.sizes.x <= 1) {
              notifications.add("Cannot make this small.")
              return
            }
            w.sizes.x--
            w.start.x++
          }}>Small Left</button
        >
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
        <p>Moving {gridManager.selectedCell}</p>
      </div>
    {/if}

    {#each Array.from({ length: 12 * 12 - gridManager.countCells }) as _, index}
      <!-- <p class="m"></p> -->
    {/each}

    <!-- <div>
      <OpModeControl />
      {#if gamepads.current != null}
        <GamepadDrawing gamepad={gamepads.gamepads[0]} />
      {/if}
    </div>
    <div>
      <GameField />
      <Telemetry />
    </div>
    <div>
      <Configurables />
    </div>
    <div>
      <Graph />
    </div> -->
  </section>

  {#if gridManager.isMoving}
    <section
      role="button"
      tabindex="0"
      onmouseup={() => {
        if (gridManager.selectedWidget == null) {
          return
        }

        if (!gridManager.canPlace) {
          gridManager.stopMoving("Cannot move there.")
          return
        }

        gridManager.performMove()
        gridManager.stopMoving(null)
      }}
    >
      {#each Array.from({ length: 12 * 12 }) as _, index}
        <p
          class="m"
          onfocus={() => {}}
          onmouseover={() => {
            console.log(index)
            if (gridManager.selectedWidget == null) {
              return
            }

            if (!gridManager.checkIndex(index)) {
              return
            }

            gridManager.selectedCell = index
          }}
        >
          <!-- {index} X: {index % 12} Y: {Math.floor(index / 12)} -->
        </p>
      {/each}
    </section>
  {/if}
</div>

<style>
  .controls {
    display: flex;
    gap: 0.5rem;
    align-items: center;
    padding: 0.25rem;
  }

  .m {
    border: 1px solid green;
  }
  p {
    border: 1px solid green;
    margin: 0;
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
    background-color: black;
    width: 100%;
    height: 100%;
    position: relative;
  }
  section {
    position: absolute;
    display: grid;
    grid-template-columns: repeat(12, 1fr);
    grid-template-rows: repeat(12, 1fr);
    height: 100%;
    width: 100%;
    padding: 0.5rem;
    gap: 0.5rem;
  }
  div.item {
    background-color: var(--card);
    border-radius: 16px;
    overflow: auto;
    border: 2px solid var(--bg);
    transition: background-color var(--d3);

    border: 2px solid red;
  }
  div.item.isOverlay {
    opacity: 0.5;
  }
</style>
