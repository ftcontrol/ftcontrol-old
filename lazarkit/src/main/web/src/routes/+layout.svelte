<script>
  import { socket } from "$lib"
  import Gamepads from "$lib/ui/Gamepads.svelte"
  import Notifications from "$lib/ui/Notifications.svelte"
  import Sidebar from "$lib/ui/Sidebar.svelte"
  let { children } = $props()

  import "./global.css"
</script>

<!-- TODO: predictive UI updates -->
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
    padding: 0.5rem 1rem 1rem 1rem;
  }
</style>
