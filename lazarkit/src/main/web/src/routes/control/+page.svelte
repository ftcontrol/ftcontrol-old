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
          fieldName: "customData",
          type: "CUSTOM",
          valueString:
            "org.firstinspires.ftc.teamcode.TestingAuto$Companion$CustomData@d6d801a",
          customValues: [
            {
              className: "org.firstinspires.ftc.teamcode.TestingAuto",
              fieldName: "intValue",
              type: "INT",
              valueString: (Math.random() * 100).toString(),
            },
            {
              className: "org.firstinspires.ftc.teamcode.TestingAuto",
              fieldName: "stringValue",
              type: "STRING",
              valueString: "test",
            },
          ],
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
