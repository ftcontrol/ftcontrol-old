<script>
  import { socket } from "$lib"
  import Gamepads from "$lib/ui/Gamepads.svelte"
  import Notifications from "$lib/ui/Notifications.svelte"
  import Sidebar from "$lib/ui/Sidebar.svelte"
  let { children } = $props()

  import "./global.css"
</script>

<Gamepads />
<Notifications />
<div>
  <Sidebar />
  <section>
    {#await socket.init()}
      <p>Connecting to server...</p>
    {:then}
      {@render children()}
    {:catch error}
      <p style="color: red;">WebSocket connection failed. Try refreshing.</p>
    {/await}
  </section>
</div>

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
