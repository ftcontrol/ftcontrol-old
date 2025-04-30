<script lang="ts">
  import { goto } from "$app/navigation"
  import { info } from "$lib"
  import type { Plugin } from "$lib/socket.svelte"
  import { onMount } from "svelte"
  import type { LayoutProps } from "./$types"

  let { data, children }: LayoutProps = $props()

  let plugin = $derived(info.plugins.find((it) => it.id === data.id)) as Plugin
  let page = $derived(plugin.pages.find((it) => it.id === data.page))

  onMount(() => {
    if (plugin == undefined || page == undefined) goto("/plugins")
  })
</script>

{#if page == undefined}
  <p>Page with id {data.page} does not exist.</p>
{:else}
  {@render children?.()}
{/if}
