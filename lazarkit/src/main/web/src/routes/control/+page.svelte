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
          currentValueString: (Math.random() * 100).toString(),
        },
        {
          className: "org.firstinspires.ftc.teamcode.TestClass",
          fieldName: "testEnum",
          type: "ENUM",
          currentValueString: "VALUE2",
          possibleValues: ["VALUE1", "VALUE2", "VALUE3"],
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
