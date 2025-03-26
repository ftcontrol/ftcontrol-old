<script lang="ts">
  import { socket, info, gamepads } from "$lib"
  import { SocketManager, type OpMode } from "$lib/socket.svelte"
  import GamepadDrawing from "$lib/ui/GamepadDrawing.svelte"
  import OpModeList from "$lib/ui/OpModeList.svelte"

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
    <p>{line.className} | {line.kind}</p>
    <p>{line.currentValue}</p>
  {/each}
</section>

{#if gamepads.current != null}
  <GamepadDrawing gamepad={gamepads.gamepads[0]} />
{/if}

<button
  onclick={() => {
    socket.sendMessage({
      kind: "jvmFields",
      fields: [
        {
          kind: "int",
          className: "org.firstinspires.ftc.teamcode.TestingAuto",
          fieldName: "number",
          currentValue: (Math.random() * 100).toFixed(),
        },
      ],
    })
  }}
>
  Test Fields Update
</button>

<style>
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
