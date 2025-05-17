<script lang="ts">
  import { gamepads, info } from "$lib"
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
  import { WidgetTypes, type Module } from "./grid.svelte"
  let { m }: { m: Module } = $props()
  let activeType = m.types[m.activeType]
</script>

<Section>
  {#if activeType.type == WidgetTypes.CONTROLS}
    <OpModeControl />
  {:else if activeType.type == WidgetTypes.GAMEPAD}
    <GamepadDrawing gamepad={gamepads.gamepads[0]} />
  {:else if activeType.type == WidgetTypes.FIELD}
    <GameField />
  {:else if activeType.type == WidgetTypes.TELEMETRY}
    <Telemetry />
  {:else if activeType.type == WidgetTypes.CONFIGURABLES}
    <Configurables />
  {:else if activeType.type == WidgetTypes.GRAPH}
    <Graph />
  {:else if activeType.type == WidgetTypes.CAPTURE}
    <PlaybackHistory />
  {:else if activeType.type == WidgetTypes.CUSTOM}
    {#if activeType.pluginID && activeType.pageID}
      <PluginPage pluginID={activeType.pluginID} pageID={activeType.pageID} />
    {:else}
      <p style="padding: 1rem;">CUSTOM not valid</p>
    {/if}
  {:else}
    <Header>
      <Title>Unknown Type</Title>
    </Header>
    <p style="padding: 1rem;">
      This is an unknown widget of type "{activeType.type}"
    </p>
  {/if}
</Section>
