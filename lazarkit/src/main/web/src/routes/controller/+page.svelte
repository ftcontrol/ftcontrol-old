<script lang="ts">
  import GamepadButtons from "$lib/ui/GamepadButtons.svelte"
  import GamepadDrawing from "$lib/ui/GamepadDrawing.svelte"
  import { onMount } from "svelte"

  let gamepads: Gamepad[] = []

  function plugIn(e: GamepadEvent) {
    console.log("Gamepad connected:", e.gamepad)
    updateGamepads()
  }

  function unPlug(e: GamepadEvent) {
    console.log("Gamepad disconnected:", e.gamepad)
    updateGamepads()
  }

  function updateGamepads() {
    gamepads = navigator.getGamepads
      ? (Array.from(navigator.getGamepads()).filter((g) => g) as Gamepad[])
      : []
  }

  function pollGamepads() {
    updateGamepads()
    requestAnimationFrame(pollGamepads)
  }

  function getPS4ButtonLabel(index: number): string {
    const labels: Record<number, string> = {
      0: "✕",
      1: "◯",
      2: "◻",
      3: "△",
      4: "L1",
      5: "R1",
      6: "L2",
      7: "R2",
      8: "Share",
      9: "Options",
      10: "L3",
      11: "R3",
      12: "D-Pad Up",
      13: "D-Pad Down",
      14: "D-Pad Left",
      15: "D-Pad Right",
      16: "PS",
      17: "Touchpad",
    }
    return labels[index] || `B${index}`
  }

  onMount(() => {
    pollGamepads()
  })
</script>

<svelte:window on:gamepadconnected={plugIn} on:gamepaddisconnected={unPlug} />

<h2>Connected Gamepads</h2>
{#each gamepads as gamepad}
  <div class="gamepad">
    <h3>{gamepad.id}</h3>
    <p>Index: {gamepad.index}</p>
    <GamepadDrawing {gamepad} />

    <GamepadButtons {gamepad} />
  </div>
{/each}

<style>
  .gamepad {
    border: 1px solid #ddd;
    padding: 10px;
    margin: 10px 0;
  }
</style>
