<script lang="ts">
  import GamepadButtons from "$lib/ui/GamepadButtons.svelte"
  import GamepadDrawing from "$lib/ui/GamepadDrawing.svelte"
  import Section from "$ui/primitives/Section.svelte"
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

  onMount(() => {
    pollGamepads()
  })
</script>

<svelte:window on:gamepadconnected={plugIn} on:gamepaddisconnected={unPlug} />

<h2>Connected Gamepads</h2>
{#each gamepads as gamepad}
  <Section title={gamepad.id}>
    <div class="gamepad">
      <p>Index: {gamepad.index}</p>
      <GamepadDrawing {gamepad} />

      <GamepadButtons {gamepad} />
    </div>
  </Section>
{/each}

<style>
  .gamepad {
    border: 1px solid #ddd;
    padding: 10px;
    margin: 10px 0;
  }
</style>
