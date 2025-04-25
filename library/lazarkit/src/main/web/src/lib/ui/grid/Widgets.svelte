<script lang="ts">
  import { gamepads, notifications } from "$lib"
  import GamepadDrawing from "$lib/ui/GamepadDrawing.svelte"
  import { allWidgetTypes, gridManager, WidgetTypes } from "./grid.svelte"
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
</script>

<div class="container">
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
  </section>
</div>

<style>
  .container {
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
