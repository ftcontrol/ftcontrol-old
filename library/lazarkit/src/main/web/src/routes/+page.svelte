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
  resizingID: {hover.resizingID}<br />
  startX: {hover.rstartX}<br />
  startY: {hover.rstartY}<br />
</p>

<WidgetEditor gridManager={settings.currentGrid} />
