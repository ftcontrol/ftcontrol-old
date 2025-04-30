<script lang="ts">
  import { dev } from "$app/environment"
  import { info } from "$lib"
  import Render from "$ui/Render.svelte"

  function getAPIEndpoint() {
    if (dev) {
      return "http://localhost:8001"
    }
    return window.location.origin
  }

  async function getHTML(path: string) {
    const response = await fetch(path)
    const data = await response.text()
    return data
  }
</script>

<h3>Plugins</h3>
<p>
  Found {info.plugins.length} plugin{info.plugins.length > 1 ? "s" : ""}.
</p>
{#each info.plugins as plugin}
  <div class="flex">
    <h4>{plugin.name}</h4>
    <p>
      {plugin.id}
    </p>
  </div>
  <a href="/plugins/{plugin.id}">Go to page</a>
  {#await getHTML(`${getAPIEndpoint()}/plugins/${plugin.id}/html`)}
    <p>Fetching</p>
  {:then html}
    <Render {html} />
  {/await}
{/each}

<style>
  .flex {
    display: flex;
    gap: 1rem;
    align-items: center;
    justify-content: space-between;
  }
</style>
