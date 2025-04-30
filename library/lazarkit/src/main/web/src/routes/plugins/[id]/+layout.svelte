<script lang="ts">
  import { info } from "$lib"
  import { onMount } from "svelte"
  import type { LayoutProps } from "./$types"
  import { goto } from "$app/navigation"

  let { data, children }: LayoutProps = $props()

  let plugin = $derived(info.plugins.find((it) => it.id === data.id))

  onMount(() => {
    if (plugin == undefined) goto("/plugins")
  })
</script>

{#if plugin == undefined}
  <p>Plugin with id {data.id} is not loaded.</p>
{:else}
  {@render children?.()}
{/if}
