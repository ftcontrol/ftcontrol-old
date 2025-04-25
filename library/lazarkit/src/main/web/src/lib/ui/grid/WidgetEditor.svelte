<script lang="ts">
  import { gamepads, info, notifications } from "$lib"
  import GamepadDrawing from "$lib/ui/GamepadDrawing.svelte"
  import { allWidgetTypes, Grid, WidgetTypes } from "./grid.svelte"
  import HorizontalIcon from "$ui/icons/HorizontalIcon.svelte"
  import HorizontalReversed from "$ui/icons/HorizontalReversed.svelte"
  import MoveIcon from "$ui/icons/MoveIcon.svelte"
  import VerticalIcon from "$ui/icons/VerticalIcon.svelte"
  import VerticalReversed from "$ui/icons/VerticalReversed.svelte"
  import Section from "$ui/primitives/Section.svelte"
  import SelectInput from "$ui/primitives/SelectInput.svelte"
  import GameField from "$ui/widgets/fields/GameField.svelte"
  import Graph from "$ui/widgets/Graph.svelte"
  import PlaybackHistory from "$ui/widgets/PlaybackHistory.svelte"
  import { OpModeControl, Telemetry, Configurables } from "$widgets"
  import Limelight from "$ui/widgets/LimelightDashboard.svelte"
  import LimelightDashboard from "$ui/widgets/LimelightDashboard.svelte"
  import LimelightFeed from "$ui/widgets/LimelightFeed.svelte"
  import Plus from "$ui/icons/Plus.svelte"

  let { gridManager }: { gridManager: Grid } = $props()
</script>

<div
  class="container"
  style="--cellsX:{gridManager.cellsX} ;--cellsY: {gridManager.cellsY};"
>
  <section>
    {#each gridManager.modules as w}
      <div
        class="item-wrapper"
        class:isOverlay={gridManager.selectedWidgetId == w.id}
        style="
  grid-column: {w.start.x} / span {w.sizes.x};
  grid-row: {w.start.y} / span {w.sizes.y};
  "
      >
        <div class="controls" class:shown={info.showEdit}>
          <button
            onmousedown={() => {
              gridManager.startMoving(w.id)
            }}
          >
            <MoveIcon />
          </button>
          <button
            onclick={() => {
              if (!gridManager.canExpandRight(w)) {
                notifications.add("Not enough space to expand right.")
              } else {
                w.sizes.x++
                return
              }

              if (!gridManager.canExpandLeft(w)) {
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
              if (!gridManager.canExpandDown(w)) {
                notifications.add("Not enough space to expand down.")
              } else {
                w.sizes.y++
                return
              }

              if (!gridManager.canExpandUp(w)) {
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

          <SelectInput
            startValue={w.type}
            bind:currentValue={w.type}
            value={w.type}
            possibleValues={allWidgetTypes}
            isValid={true}
            alwaysValid={true}
          ></SelectInput>
        </div>
        <div class="item">
          {#if w.type == WidgetTypes.CONTROLS}
            <OpModeControl />
          {:else if w.type == WidgetTypes.GAMEPAD}
            <GamepadDrawing gamepad={gamepads.gamepads[0]} />
          {:else if w.type == WidgetTypes.FIELD}
            <GameField />
          {:else if w.type == WidgetTypes.TELEMETRY}
            <Telemetry />
          {:else if w.type == WidgetTypes.CONFIGURABLES}
            <Configurables />
          {:else if w.type == WidgetTypes.GRAPH}
            <Graph />
          {:else if w.type == WidgetTypes.LIMELIGHT_DASH}
            <LimelightDashboard />
          {:else if w.type == WidgetTypes.LIMELIGHT_FEED}
            <LimelightFeed />
          {:else if w.type == WidgetTypes.CAPTURE}
            <PlaybackHistory />
          {:else}
            <Section title="Unknown Type"
              >This is an unknown widget of type "{w.type}"</Section
            >
          {/if}
        </div>
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
        <p>
          Moving X: {gridManager.selectedCellX} Y: {gridManager.selectedCellY}
        </p>
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
      {#each Array.from( { length: gridManager.cellsX * gridManager.cellsY } ) as _, index}
        {@const id =
          gridManager.modulesMap[Math.floor(index / gridManager.cellsX) + 1][
            (index % gridManager.cellsX) + 1
          ]}
        <p
          class="overlay-item"
          class:isEmpty={id != null && id != gridManager.selectedWidgetId}
          onfocus={() => {}}
          onmouseover={() => {
            console.log(index)
            if (gridManager.selectedWidget == null) {
              return
            }

            const selected = gridManager.selectedWidget

            const newX = (index % gridManager.cellsX) + 1
            const newY = Math.floor(index / gridManager.cellsX) + 1

            const canPlaceBoth = gridManager.coreCheckPlace(
              newX,
              newY,
              selected
            )
            const canPlaceX = gridManager.coreCheckPlace(
              newX,
              gridManager.selectedCellY,
              selected
            )
            const canPlaceY = gridManager.coreCheckPlace(
              gridManager.selectedCellX,
              newY,
              selected
            )

            console.log("canPlaceBoth", canPlaceBoth)

            console.log("canPlaceX", canPlaceX)

            console.log("canPlaceY", canPlaceY)

            if (canPlaceX || canPlaceBoth) {
              gridManager.selectedCellX = newX
            }

            if (canPlaceY || canPlaceBoth) {
              gridManager.selectedCellY = newY
            }
          }}
        ></p>
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
    margin: 0.5rem;
    flex-wrap: wrap;
    position: absolute;
    z-index: 100;
    top: 0;

    border-radius: 0.75rem;
    padding-inline: 0.5rem;
    background: var(--cardTransparent);
    border: 2px solid var(--bg);
    transition:
      background-color var(--d3),
      opacity var(--d3);
    box-shadow: 0 4px 30px rgba(0, 0, 0, 0.1);
    backdrop-filter: blur(10px);
    -webkit-backdrop-filter: blur(10px);
    opacity: 0;
    pointer-events: none;
    animation: show var(--d3) forwards;
  }

  .add {
    transition: opacity var(--d3);
    pointer-events: none;
    display: grid;
    place-items: center;
    opacity: 0;
  }

  .controls.shown,
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

  .overlay-item {
    border: 1px solid var(--primary);
    margin: 0;
  }
  .overlay-item.isEmpty {
    opacity: 0;
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
  .item-wrapper {
    position: relative;
  }
  div.item {
    background-color: var(--card);
    border-radius: 16px;
    overflow: auto;
    border: 2px solid var(--bg);
    transition: background-color var(--d3);
    height: 100%;
  }
  div.isOverlay {
    opacity: 0.5;
  }
</style>
