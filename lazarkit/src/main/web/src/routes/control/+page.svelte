<script lang="ts">
  import { socket, info, gamepads } from "$lib"
  import { type OpMode } from "$lib/socket.svelte"
  import GamepadDrawing from "$lib/ui/GamepadDrawing.svelte"
  import OpModeList from "$lib/ui/OpModeList.svelte"
  import Field from "$lib/ui/Field.svelte"

  let modalOpened = $state<"" | "autos" | "teleops">("")

  function toggle(name: "autos" | "teleops") {
    if (modalOpened != name) {
      modalOpened = name
    } else {
      modalOpened = ""
    }
  }

  function selectedOpMode(opMode: OpMode) {
    currentOpMode = opMode
    modalOpened = ""
  }

  $effect(() => {
    console.log(info.activeOpMode)
    if (info.activeOpMode == " ") {
      return
    }
    console.log("Length: ", info.opModes.length)

    let found = null

    for (const item of info.opModes) {
      if (item.name == info.activeOpMode) {
        found = item
      }
    }

    if (found != null) {
      currentOpMode = found
    }
  })

  let currentOpMode = $state<OpMode>({
    name: "",
    group: "",
    flavour: "AUTONOMOUS",
  })
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

<section>
  <div class="flex">
    <button onclick={() => toggle("autos")}>Autos</button>
    <h3>OpMode Control</h3>
    <button onclick={() => toggle("teleops")}>TeleOps</button>
  </div>

  {#if modalOpened == "autos"}
    <div class="modal autos">
      <OpModeList flavour="AUTONOMOUS" onselect={selectedOpMode} />
    </div>
  {/if}
  {#if modalOpened == "teleops"}
    <div class="modal teleops">
      <OpModeList flavour="TELEOP" onselect={selectedOpMode} />
    </div>
  {/if}
  {#if currentOpMode.name == ""}
    <p class="title">Nothing selected</p>
  {:else}
    <p class="title">
      <span class="title_small">{currentOpMode.flavour}</span>
      {currentOpMode.name}
    </p>
  {/if}

  <div class="flex">
    <button
      disabled={currentOpMode.name == "" ||
        (info.activeOpModeStatus == "running" &&
          info.activeOpMode != "$Stop$Robot$") ||
        (info.activeOpModeStatus == "stopped" &&
          info.activeOpMode != "$Stop$Robot$") ||
        (currentOpMode.name != "" && info.activeOpModeStatus == "init")}
      onclick={() => {
        socket.sendMessage({
          kind: "initOpMode",
          opModeName: currentOpMode.name,
        })
      }}
      >Initialize
    </button>
    <button
      disabled={info.activeOpMode == "$Stop$Robot$" ||
        currentOpMode.name == "" ||
        info.activeOpModeStatus != "init"}
      onclick={() => {
        socket.sendMessage({ kind: "startActiveOpMode" })
      }}
      >Start
    </button>
    <button
      disabled={info.activeOpMode == "$Stop$Robot$" ||
        currentOpMode.name == "" ||
        info.activeOpModeStatus == "stopped"}
      onclick={() => {
        socket.sendMessage({ kind: "stopActiveOpMode" })
      }}
      >Stop
    </button>
  </div>
</section>

<section>
  <h3>Telemetry</h3>
  {#each info.telemetry as line}
    <p>{line}</p>
  {/each}
</section>
<section>
  <h3>Variables</h3>
  {#each info.jvmFields as line}
    <div class="item" style={line.type == "UNKNOWN" ? "opacity: 0.3;" : ""}>
      <p>{line.className.split("." + line.className.split(".").at(-1))[0]}</p>

      <p>{line.className.split(".").at(-1)} {line.fieldName} {line.type}</p>
      <p>
        {line.valueString}
      </p>
      {#if line.type == "ENUM"}
        <p>{JSON.stringify(line.possibleValues)}</p>
      {/if}
      {#if line.type == "CUSTOM"}
        <p>{JSON.stringify(line.customValues)}</p>
      {/if}
      {#if line.type == "ARRAY"}
        <p>{JSON.stringify(line.arrayValues)}</p>
      {/if}
      {JSON.stringify(line)}
    </div>
  {/each}
</section>

{#if gamepads.current != null}
  <GamepadDrawing gamepad={gamepads.gamepads[0]} />
{/if}

<style>
  .item {
    border: 2px solid black;
  }
  h3 {
    text-align: center;
  }
  div.flex {
    display: flex;
    gap: 1rem;
    justify-content: space-between;
  }
  button {
    border: none;
    background-color: transparent;
  }
  button:disabled {
    opacity: 0.5;
  }
  section {
    width: fit-content;
    min-width: 600px;
    position: relative;
    border: 1px solid black;
  }
  div.modal {
    border: 1px solid black;
    width: fit-content;
    position: absolute;
    background-color: white;
    z-index: 100;
  }
  .autos {
    left: 0;
  }
  .teleops {
    right: 0;
  }

  .title {
    font-size: 2rem;
    text-align: center;
    position: relative;
  }

  .title_small {
    font-size: 1rem;
    text-align: center;
    position: absolute;
    left: 50%;
    transform: translate(-50%, 0);
    bottom: -1rem;
  }
</style>
