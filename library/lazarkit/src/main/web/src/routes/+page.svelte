<script lang="ts">
  import { settings } from "$lib/settings.svelte"
  import WidgetEditor from "$ui/grid/WidgetEditor.svelte"
</script>

<WidgetEditor gridManager={settings.currentGrid} />
