<script lang="ts">
  import { page } from "$app/state"
  import { settings } from "$lib/settings.svelte"
  import { hover } from "$ui/grid/hover.svelte"
  import WidgetEditor from "$ui/grid/WidgetEditor.svelte"
  import { onMount } from "svelte"

  onMount(() => {
    const presetID = page.url.searchParams.get("preset")
    if (presetID == null) return
    settings.selectedManagerID = presetID
  })
</script>

<p>
  movingID: {hover.movingID}
  movingIndex: {hover.movingIndex}
</p>

<WidgetEditor gridManager={settings.currentGrid} />
