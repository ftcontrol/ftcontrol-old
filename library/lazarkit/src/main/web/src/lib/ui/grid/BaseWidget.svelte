<script lang="ts">
  import { gamepads, info, notifications } from "$lib"
  import GamepadDrawing from "$ui/GamepadDrawing.svelte"
  import PluginPage from "$ui/PluginPage.svelte"
  import Header from "$ui/primitives/Header.svelte"
  import Section from "$ui/primitives/Section.svelte"
  import Title from "$ui/primitives/Title.svelte"
  import Configurables from "$ui/widgets/configurables/Configurables.svelte"
  import GameField from "$ui/widgets/fields/GameField.svelte"
  import Graph from "$ui/widgets/Graph.svelte"
  import OpModeControl from "$ui/widgets/OpModeControl.svelte"
  import PlaybackHistory from "$ui/widgets/PlaybackHistory.svelte"
  import Telemetry from "$ui/widgets/Telemetry.svelte"
  import { Grid, WidgetTypes, type Module } from "./grid.svelte"
  import GridControls from "./GridControls.svelte"
  let { m, gridManager }: { m: Module; gridManager: Grid } = $props()
</script>

<Section>
  <div class="bar">
    <div class="controls">
      {#each m.types as t, index}
        <button
          class="base"
          class:selected={index == m.activeType}
          onclick={() => {
            m.activeType = index
          }}
          >{t.type}
        </button>
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
      {/each}
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
</Section>

<style>
  .bar {
    margin-bottom: 1rem;
  }
  .content {
    display: none;
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
