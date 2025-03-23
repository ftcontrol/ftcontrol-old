<script lang="ts">
  import { socket, info, gamepads } from "$lib"
  import type { OpMode } from "$lib/socket.svelte"
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
    console.log(info.currentOpMode)
    if (info.currentOpMode == "$Stop$Robot$") {
      return
    }
    console.log("Length: ", info.opModes.length)

    let found = null

    for (const item of info.opModes) {
      if (item.name == info.currentOpMode) {
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
    flavor: "AUTONOMOUS",
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
      <span class="title_small">{currentOpMode.flavor}</span>
      {currentOpMode.name}
    </p>
  {/if}

  <div class="flex">
    <button
      disabled={currentOpMode.name == "" ||
        (info.currentOpModeStatus == "running" &&
          info.currentOpMode != "$Stop$Robot$") ||
        (info.currentOpModeStatus == "stopped" &&
          info.currentOpMode != "$Stop$Robot$") ||
        (currentOpMode.name != "" && info.currentOpModeStatus == "init")}
      onclick={() => {
        socket.sendMessage("init_opmode", [currentOpMode.name])
      }}
      >Initialize
    </button>
    <button
      disabled={info.currentOpMode == "$Stop$Robot$" ||
        currentOpMode.name == "" ||
        info.currentOpModeStatus != "init"}
      onclick={() => {
        socket.sendMessage("start_opmode")
      }}
      >Start
    </button>
    <button
      disabled={info.currentOpMode == "$Stop$Robot$" ||
        currentOpMode.name == "" ||
        info.currentOpModeStatus == "stopped"}
      onclick={() => {
        socket.sendMessage("stop_opmode")
      }}
      >Stop
    </button>
  </div>
</section>

{#if gamepads.current != null}
  <GamepadDrawing gamepad={gamepads.gamepads[0]} />
{/if}

<style>
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
