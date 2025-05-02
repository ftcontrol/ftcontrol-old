<script lang="ts">
  import { info } from "$lib"
  import type { Plugin } from "$lib/socket.svelte"
  import type { PageProps } from "./$types"

  let { data }: PageProps = $props()

  let plugin = $derived(info.plugins.find((it) => it.id === data.id)) as Plugin
</script>

<h1>Hi!</h1>

<h1>{plugin.name}</h1>

<p>{plugin.id}</p>

{#each plugin.pages as page}
  <a href="/plugins/{plugin.id}/pages/{page.id}">{page.title}</a><br />
{/each}
