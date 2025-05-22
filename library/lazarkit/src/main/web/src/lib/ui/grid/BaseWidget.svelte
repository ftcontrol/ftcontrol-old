<script lang="ts">
  import { gamepads, info, notifications } from "$lib"
  import GamepadDrawing from "$ui/GamepadDrawing.svelte"
  import PluginPage from "$ui/PluginPage.svelte"
  import Section from "$ui/primitives/Section.svelte"
  import Configurables from "$ui/widgets/configurables/Configurables.svelte"
  import GameField from "$ui/widgets/fields/GameField.svelte"
  import Graph from "$ui/widgets/Graph.svelte"
  import OpModeControl from "$ui/widgets/OpModeControl.svelte"
  import PlaybackHistory from "$ui/widgets/PlaybackHistory.svelte"
  import Telemetry from "$ui/widgets/Telemetry.svelte"
  import { Grid, WidgetTypes, type Module } from "./grid.svelte"
  import GridControls from "./GridControls.svelte"
  import Portal from "svelte-portal"
  import { hover } from "./hover.svelte"

  let { m, gridManager }: { m: Module; gridManager: Grid } = $props()

  let isMoving = $derived(hover.movingID == m.id && hover.isMoving)
</script>

<Portal>
  <button class="hover">{m.types[m.activeType].type}</button>
</Portal>
<Section>
  <p>
    isMoving: {isMoving}
  </p>
  <div class="bar">
    <div class="controls">
      {#each m.types as t, index}
        {#if !hover.isMoving || hover.movingID != m.id || (hover.movingID == m.id && hover.movingIndex != index)}
          <button
            class="base"
            class:selected={index == m.activeType}
            onclick={() => {
              m.activeType = index
            }}
            onmousedown={(event: MouseEvent) => {
              hover.startMoving(event.clientX, event.clientY, index, m.id)
            }}
            >{t.type}
          </button>
        {/if}
        {#if info.showEdit && m.types.length > 1}
          <button
            onclick={() => {
              if (m.types.length <= 1) {
                notifications.add("Cannot remove last widget.")
                return
              }
              m.types = m.types.filter((_, i) => i !== index)
              m.activeType--
              if (m.activeType < 0) m.activeType = 0
            }}>x</button
          >
        {/if}
        {#if index < m.types.length - 1}
          {#if hover.isMoving && (hover.movingID != m.id || (hover.movingID == m.id && hover.movingIndex != index))}
            <div class="extra-small" data-id={m.id} data-index={index} />
          {/if}
        {/if}
      {/each}
      {#if hover.isMoving && (hover.movingID != m.id || (hover.movingID == m.id && hover.movingIndex != m.types.length))}
        <div class="extra" data-id={m.id} data-index={m.types.length} />
      {/if}
      {#if info.showEdit}
        <button
          onclick={() => {
            m.types.push({
              pluginID: "none",
              pageID: "none",
              type: WidgetTypes.TEST,
            })
            console.log(m)
          }}>+</button
        >
      {/if}
    </div>
    {#if info.showEdit}
      <div class="controls">
        <GridControls {m} {gridManager} />
      </div>
    {/if}
  </div>

  {#each m.types as item, index}
    <div class="content" class:shown={index == m.activeType}>
      {#if item.type == WidgetTypes.CONTROLS}
        <OpModeControl />
      {:else if item.type == WidgetTypes.GAMEPAD}
        <GamepadDrawing gamepad={gamepads.gamepads[0]} />
      {:else if item.type == WidgetTypes.FIELD}
        <GameField />
      {:else if item.type == WidgetTypes.TELEMETRY}
        <Telemetry />
      {:else if item.type == WidgetTypes.CONFIGURABLES}
        <Configurables />
      {:else if item.type == WidgetTypes.GRAPH}
        <Graph />
      {:else if item.type == WidgetTypes.CAPTURE}
        <PlaybackHistory />
      {:else if item.type == WidgetTypes.CUSTOM}
        {#if item.pluginID && item.pageID}
          <PluginPage pluginID={item.pluginID} pageID={item.pageID} />
        {:else}
          <p style="padding: 1rem;">CUSTOM not valid</p>
        {/if}
      {:else}
        <p style="padding: 1rem;">
          This is an unknown widget of type "{item.type}"
        </p>
      {/if}
    </div>
  {/each}

  <button
    class="resizer"
    onmousedown={() => {
      info.showEdit = true
    }}
    onmouseup={() => {
      info.showEdit = false
    }}
  >
  </button>
</Section>

<style>
  .hover {
    position: absolute;
    top: 0;
    left: 0;
  }
  .extra {
    flex-grow: 1;
    background-color: blue;
  }
  .extra-small {
    background-color: blue;
    width: 16px;
  }
  .resizer {
    background-color: red;
    cursor: grabbing;
    width: 64px;
    height: 64px;
    position: absolute;
    right: 0;
    bottom: 0;
  }
  .bar {
    margin-bottom: 1rem;
  }
  .content {
    display: none;
    overflow-y: auto;
  }
  .content.shown {
    display: block;
  }
  .controls {
    width: 100%;
    display: flex;
    flex-wrap: wrap;
    padding: 1rem;
    padding-bottom: 0;
    gap: 0.5rem;
  }

  button {
    background-color: transparent;
    border: none;
    cursor: pointer;
    user-select: none;
    display: flex;
    justify-content: center;
    align-items: center;
    color: inherit;
  }
  button.base {
    outline: 1px solid var(--text);
    opacity: 0.5;
    padding: 0.25rem 0.5rem;
  }
  button.base.selected {
    opacity: 1;
  }
</style>
