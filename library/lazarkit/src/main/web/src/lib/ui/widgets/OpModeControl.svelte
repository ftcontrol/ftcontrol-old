<script lang="ts">
  import { info, socket } from "$lib"
  import type { OpMode } from "$lib/socket.svelte"
  import { Button } from "$primitives"
  import Arrow from "$ui/icons/Arrow.svelte"
  import Content from "$ui/primitives/Content.svelte"
  import OpModeList from "../OpModeList.svelte"
  import Hiddable from "./configurables/Hiddable.svelte"

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

<Content>
  <div class="modal autos">
    <Hiddable isShown={modalOpened == "autos"}>
      <OpModeList flavour="AUTONOMOUS" onselect={selectedOpMode} />
    </Hiddable>
  </div>
  <div class="modal teleops">
    <Hiddable isShown={modalOpened == "teleops"}>
      <OpModeList flavour="TELEOP" onselect={selectedOpMode} />
    </Hiddable>
  </div>

  <div class="controls">
    <button onclick={() => toggle("autos")}>
      <span>
        Autos<Arrow isOpened={modalOpened == "autos"} />
      </span>
    </button>
    <button onclick={() => toggle("teleops")}>
      <span>
        TeleOps<Arrow isOpened={modalOpened == "teleops"} />
      </span>
    </button>
  </div>
  {#if currentOpMode.name == ""}
    <p class="title">Nothing selected</p>
  {:else}
    <p class="title">
      <span class="title_small">{currentOpMode.flavour}</span>
      {currentOpMode.name}
    </p>
  {/if}
  <div class="flex">
    <Button
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
    >
      Initialize
    </Button>
    <Button
      disabled={info.activeOpMode == "$Stop$Robot$" ||
        currentOpMode.name == "" ||
        info.activeOpModeStatus != "init"}
      onclick={() => {
        socket.sendMessage({ kind: "startActiveOpMode" })
      }}
    >
      Start
    </Button>
    <Button
      disabled={info.activeOpMode == "$Stop$Robot$" ||
        currentOpMode.name == "" ||
        info.activeOpModeStatus == "stopped"}
      onclick={() => {
        socket.sendMessage({ kind: "stopActiveOpMode" })
      }}
    >
      Stop
    </Button>
  </div>
</Content>

<style>
  .controls {
    display: flex;
    justify-content: space-between;
  }
  span {
    display: flex;
    align-items: center;
    gap: 0.3rem;
  }
  div.modal {
    position: absolute;
    background-color: var(--card);
    z-index: 100;
    max-height: 133px;
    overflow-y: auto;
    top: 4.5rem;
  }
  .flex {
    display: flex;
    gap: 1rem;
    justify-content: space-between;
  }
  .autos {
    left: 0;
  }
  .teleops {
    right: 0;
  }

  .title {
    font-size: 1.25rem;
    text-align: center;
    position: relative;
    margin: 0;
    margin-bottom: 1rem;
  }

  .title_small {
    font-size: 1rem;
    text-align: center;
    position: absolute;
    left: 50%;
    transform: translate(-50%, 0);
    bottom: -1rem;
  }

  button {
    border: none;
    background-color: transparent;
    margin: 0;
    color: inherit;
  }
</style>
