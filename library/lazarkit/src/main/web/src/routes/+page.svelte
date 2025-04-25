<script lang="ts">
  import { info } from "$lib"
  import WidgetEditor from "$ui/grid/WidgetEditor.svelte"
  import Widgets from "$ui/grid/Widgets.svelte"
</script>

{#if info.showEdit}
  <WidgetEditor />
{:else}
  <Widgets />
{/if}
