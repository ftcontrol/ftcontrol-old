<script lang="ts">
  import { gamepads, notifications } from "$lib"
  import { onMount } from "svelte"

  function plugIn(e: GamepadEvent) {
    console.log("Gamepad connected:", e.gamepad)
    notifications.add(`Gamepad connected: ${e.gamepad.id}`)
    gamepads.updateGamepads()
  }

  function unPlug(e: GamepadEvent) {
    console.log("Gamepad disconnected:", e.gamepad)
    notifications.add(`Gamepad disconnected: ${e.gamepad.id}`)
    gamepads.updateGamepads()
    gamepads.current = null
    //send empty
  }

  function pollGamepads() {
    gamepads.updateGamepads()
    requestAnimationFrame(pollGamepads)
  }

  onMount(() => {
    pollGamepads()
  })
</script>

<svelte:window on:gamepadconnected={plugIn} on:gamepaddisconnected={unPlug} />
