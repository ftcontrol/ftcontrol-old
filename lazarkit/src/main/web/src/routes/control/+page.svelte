<script lang="ts">
  import { socket, info, gamepads } from "$lib"
  import GamepadDrawing from "$lib/ui/GamepadDrawing.svelte"
  import Configurables from "$ui/widgets/Configurables.svelte"
  import { OpModeControl, Telemetry } from "$widgets"
</script>

<button
  onclick={() => {
    socket.sendMessage({
      kind: "updatedJvmFields",
      fields: [
        {
          id: "ed0bcc0b-a4c1-4159-83bc-218b12974474",
          newValueString: (Math.random() * 100).toString(),
        },
      ],
    })
  }}
>
  Test Fields Update
</button>
<OpModeControl />
<Telemetry />
<Configurables />

{#if gamepads.current != null}
  <GamepadDrawing gamepad={gamepads.gamepads[0]} />
{/if}
