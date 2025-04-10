<script lang="ts">
  import { gamepads } from "$lib"
  import GamepadDrawing from "$lib/ui/GamepadDrawing.svelte"
  import GameField from "$ui/widgets/fields/GameField.svelte"
  import Flows from "$ui/widgets/Flows.svelte"
  import { OpModeControl, Telemetry, Configurables } from "$widgets"

  let scrollContainer: HTMLElement | null = null
  function handleWheel(event: WheelEvent) {
    if (scrollContainer) {
      event.preventDefault()
      scrollContainer.scrollLeft += event.deltaY * 4
    }
  }
</script>

<section bind:this={scrollContainer}>
  <div class="scroller" onwheel={handleWheel} />
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
    scroll-behavior: smooth;
    height: 100%;
    padding: 0.5rem;
  }
  .scroller {
    margin: -0.5rem;
    position: absolute;
    width: 100%;
    height: 100%;
  }
</style>
