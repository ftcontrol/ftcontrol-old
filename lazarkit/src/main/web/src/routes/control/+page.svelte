<script lang="ts">
  import { socket, info, gamepads } from "$lib"
  import GamepadDrawing from "$lib/ui/GamepadDrawing.svelte"
  import Configurables from "$ui/widgets/Configurables.svelte"
  import { OpModeControl, Telemetry } from "$widgets"
</script>

<button
  onclick={() => {
    socket.sendMessage({
      kind: "jvmFields",
      fields: [
        {
          className: "org.firstinspires.ftc.teamcode.TestingAuto",
          fieldName: "number",
          type: "INT",
          valueString: "21",
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
