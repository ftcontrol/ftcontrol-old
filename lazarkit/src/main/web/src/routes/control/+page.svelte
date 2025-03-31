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
          fieldName: "customNestedData",
          type: "CUSTOM",
          valueString:
            "CustomNestedData(customData=CustomData(intValue=5, stringValue='test'), stringValue='test')",
          customValues: [
            {
              className: "org.firstinspires.ftc.teamcode.TestingAuto",
              fieldName: "customData",
              type: "CUSTOM",
              valueString: "CustomData(intValue=5, stringValue='test')",
              customValues: [
                {
                  className: "org.firstinspires.ftc.teamcode.TestingAuto",
                  fieldName: "stringValue",
                  type: "STRING",
                  valueString: "test" + (Math.random() * 100).toString(),
                },
              ],
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
