<script lang="ts">
  import { gamepads } from "$lib"
  import GamepadDrawing from "$ui/GamepadDrawing.svelte"
  import PluginPage from "$ui/PluginPage.svelte"
  import Configurables from "$ui/widgets/configurables/Configurables.svelte"
  import GameField from "$ui/widgets/fields/GameField.svelte"
  import Graph from "$ui/widgets/Graph.svelte"
  import OpModeControl from "$ui/widgets/OpModeControl.svelte"
  import PlaybackHistory from "$ui/widgets/PlaybackHistory.svelte"
  import Telemetry from "$ui/widgets/Telemetry.svelte"
  import { WidgetTypes, type ModuleType } from "./grid.svelte"

  let { item }: { item: ModuleType } = $props()
</script>

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

<style>
</style>
