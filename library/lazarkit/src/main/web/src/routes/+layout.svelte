<script lang="ts">
  import Gamepads from "$lib/ui/Gamepads.svelte"
  import Notifications from "$lib/ui/Notifications.svelte"
  import Sidebar from "$lib/ui/Sidebar.svelte"
  import Settings from "$ui/Settings.svelte"
  import { info, notifications, socket } from "$lib"
  import { onDestroy, onMount } from "svelte"
  let { children } = $props()

  import "./global.css"
  import { modular } from "$ui/grid/logic/modular"
  import Portal from "svelte-portal"
  import { settings } from "$lib/settings.svelte"
  import { PANELS_VERSION } from "$lib/globals"

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

  $effect(() => {
    if (settings.presets) settings.savePresets()
  })

  async function detectSWUpdate() {
    const registration = await navigator.serviceWorker.ready

    registration.addEventListener("updatefound", () => {
      const newSW = registration.installing
      newSW?.addEventListener("statechange", () => {
        if (newSW.state == "installed") {
          if (confirm("New update available! Ready to update?")) {
            newSW.postMessage({ type: "SKIP_WAITING" })
            window.location.reload()
          }
        }
      })
    })
  }

  async function getVersion() {
    try {
      const response = await fetch(
        "https://mymaven.bylazar.com/api/maven/latest/version/releases/com/bylazar/ftcontrol"
      )
      if (!response.ok) throw new Error("Failed to fetch")
      const text = await response.json()
      return text.version
    } catch (err) {
      throw err
    }
  }

  let interval: ReturnType<typeof setInterval>

  let toldTimestamp = 0

  function registerInterval() {
    interval = setInterval(async () => {
      try {
        const version = await getVersion()
        console.log(version, Date.now(), toldTimestamp)
        if (
          version != PANELS_VERSION &&
          Date.now() - toldTimestamp > 1000 * 36
        ) {
          notifications.add("New version of Panels available")
          toldTimestamp = Date.now()
          clearInterval(interval)
          setTimeout(() => {
            detectSWUpdate()
          }, 3600)
        }
      } catch {
        console.log("Failed to fetch latest version")
      }
    }, 1000)
  }

  onMount(() => {
    detectSWUpdate()
    registerInterval()
  })

  onDestroy(() => {
    clearInterval(interval)
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

  {#if modular.sidebarTabs.wasStartedMoving && modular.sidebarTabs.movingIndex != null}
    <button
      class="overlay"
      class:selected={settings.selectedManagerID ==
        settings.allIDs[modular.sidebarTabs.movingIndex]}
      style="top: {modular.tabs.mouseY}px;left: {modular.tabs.mouseX}px;"
    >
      {settings.presets[modular.sidebarTabs.movingIndex].name}
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
