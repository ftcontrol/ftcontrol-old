<script lang="ts">
  import { gamepads } from "$lib"
  import GamepadDrawing from "$lib/ui/GamepadDrawing.svelte"
  import GameField from "$ui/widgets/fields/GameField.svelte"
  import Flows from "$ui/widgets/Flows.svelte"
  import Graph from "$ui/widgets/Graph.svelte"
  import PlaybackHistory from "$ui/widgets/PlaybackHistory.svelte"
  import { OpModeControl, Telemetry, Configurables } from "$widgets"
</script>

<section>
  <div>
    <OpModeControl />
    <PlaybackHistory />
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
  </div>
</section>

<style>
  section {
    display: grid;
    grid-template-columns: 1fr 1fr 1fr 1fr;
    overflow-x: auto;
    scroll-behavior: smooth;
    height: 100%;
    padding: 0.5rem;
  }
</style>
