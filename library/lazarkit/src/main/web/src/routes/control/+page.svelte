<script lang="ts">
  import { gamepads } from "$lib"
  import GamepadDrawing from "$lib/ui/GamepadDrawing.svelte"
  import GameField from "$ui/widgets/fields/GameField.svelte"
  import Flows from "$ui/widgets/Flows.svelte"
  import Graph from "$ui/widgets/Graph.svelte"
  import PlaybackHistory from "$ui/widgets/PlaybackHistory.svelte"
  import { OpModeControl, Telemetry, Configurables } from "$widgets"

  let scrollContainer: HTMLElement | null = null
  function handleWheel(event: WheelEvent) {
    if (!scrollContainer) return

    const isTrackpadGesture = Math.abs(event.deltaX) > 5

    event.preventDefault()
    if (isTrackpadGesture) {
      scrollContainer.scrollLeft += event.deltaX * 8
      return
    }
    scrollContainer.scrollLeft += event.deltaY * 4
  }
</script>

<section bind:this={scrollContainer}>
  <div class="scroller" onwheel={handleWheel}></div>
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
    <Graph />
  </div>
  <div>
    <Configurables />
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
