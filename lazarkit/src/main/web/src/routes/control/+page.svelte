<script lang="ts">
  import { gamepads } from "$lib"
  import GamepadDrawing from "$lib/ui/GamepadDrawing.svelte"
  import GameField from "$ui/widgets/fields/GameField.svelte"
  import Flows from "$ui/widgets/Flows.svelte"
  import { OpModeControl, Telemetry, Configurables } from "$widgets"
</script>

<section>
  <div>
    <OpModeControl />
    <Telemetry />
    {#if gamepads.current != null}
      <GamepadDrawing gamepad={gamepads.gamepads[0]} />
    {/if}
  </div>
  <div>
    <Configurables />
  </div>
  <div>
    <GameField />
  </div>
  <div>
    <Flows />
  </div>
</section>

<style>
  section {
    display: grid;
    grid-template-columns: 1fr 1fr 1fr 1fr;
    overflow-x: auto;
    height: 100%;
    padding: 0.5rem;
  }
</style>
