<script lang="ts">
  import Gamepads from "$lib/ui/Gamepads.svelte"
  import Notifications from "$lib/ui/Notifications.svelte"
  import Sidebar from "$lib/ui/Sidebar.svelte"
  import Settings from "$ui/Settings.svelte"
  import { info, socket } from "$lib"
  import { onMount } from "svelte"
  let { children } = $props()

  import "./global.css"
  import { modular } from "$ui/grid/logic/modular"
  import Portal from "svelte-portal"
  import { settings } from "$lib/settings.svelte"

  onMount(() => {
    socket.init()

    const interval = setInterval(() => {
      info.loop()
    }, 100)

    modular.init()

    return () => {
      clearInterval(interval)
      modular.destroy()
    }
  })
</script>

<Portal>
  {#if modular.tabs.wasStartedMoving && modular.tabs.movingIndex != null}
    <button
      class="overlay"
      class:selected={modular.tabs.movingIndex ==
        settings.currentGrid.widgets.find(
          (it) => it.id == modular.tabs.movingID
        )?.activeWidgetID}
      style="top: {modular.tabs.mouseY}px;left: {modular.tabs.mouseX}px;"
    >
      {settings.currentGrid.widgets.find((it) => it.id == modular.tabs.movingID)
        ?.widgets[modular.tabs.movingIndex].type}
    </button>
  {/if}
</Portal>

<Gamepads />
<Notifications />
<div>
  <Sidebar />
  <section>
    {@render children()}
  </section>
</div>

<Settings />

<style>
  .overlay {
    all: unset;
    background-color: var(--card);
    outline: 1px solid var(--text);
    position: absolute;
    z-index: 1000;
    top: 0;
    left: 0;
    transform: translateX(-50%) translateY(-50%);
    pointer-events: none;
    padding: 0.25rem 0.5rem;
    opacity: 0.5;
  }
  .overlay.selected {
    opacity: 1;
  }

  div {
    display: grid;
    grid-template-columns: auto 1fr;
    height: 100vh;
  }
  section {
    overflow-y: auto;
  }
</style>
