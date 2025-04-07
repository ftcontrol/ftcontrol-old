<script>
  import { socket } from "$lib"
  import Gamepads from "$lib/ui/Gamepads.svelte"
  import Notifications from "$lib/ui/Notifications.svelte"
  import Sidebar from "$lib/ui/Sidebar.svelte"
  let { children } = $props()

  import "./global.css"
</script>

{#await socket.init()}
  <p>Loading...</p>
{:then}
  <Gamepads />
  <Notifications />
  <div>
    <Sidebar />
    <section>
      {@render children()}
    </section>
  </div>
{:catch error}
  <p style="color: red;">WebSocket connection failed. Try refreshing.</p>
{/await}

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
