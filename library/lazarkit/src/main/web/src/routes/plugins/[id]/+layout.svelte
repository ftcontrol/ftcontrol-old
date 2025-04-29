<script lang="ts">
  import { info } from "$lib"
  import type { LayoutProps } from "./$types"

  let { data, children }: LayoutProps = $props()

  let plugin = $derived(info.plugins.find((it) => it.id === data.id))
</script>

{#if plugin == undefined}
  <p>Plugin with id {data.id} is not loaded.</p>
{:else}
  {@render children?.()}
{/if}
