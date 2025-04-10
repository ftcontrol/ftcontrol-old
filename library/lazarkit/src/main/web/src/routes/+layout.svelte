<script lang="ts">
  import { info, socket } from "$lib"
  import Gamepads from "$lib/ui/Gamepads.svelte"
  import Notifications from "$lib/ui/Notifications.svelte"
  import Sidebar from "$lib/ui/Sidebar.svelte"
  import { onMount } from "svelte"
  let { children } = $props()

  import "./global.css"
  import Settings from "$ui/Settings.svelte"

  onMount(() => {
    socket.init()

    const interval = setInterval(() => {
      info.loop()
    }, 100)

    return () => {
      clearInterval(interval)
    }
  })
</script>

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
  div {
    display: grid;
    grid-template-columns: auto 1fr;
    height: 100vh;
  }
  section {
    overflow-y: auto;
  }
  p {
    margin: 1rem;
    text-align: center;
  }
</style>
