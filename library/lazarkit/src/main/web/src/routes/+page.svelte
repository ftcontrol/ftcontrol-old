<script lang="ts">
  import { info } from "$lib"
  import { allKeys, gridManagers } from "$ui/grid"
  import WidgetEditor from "$ui/grid/WidgetEditor.svelte"
  $effect(() => {
    if (!allKeys.includes(info.selectedManager))
      info.selectedManager = allKeys[0]
  })
</script>

{#if allKeys.includes(info.selectedManager)}
  <WidgetEditor gridManager={gridManagers[info.selectedManager]} />
{:else}
  <WidgetEditor gridManager={gridManagers[allKeys[0]]} />
{/if}
