<script lang="ts">
  import { page } from "$app/state"
  import { settings } from "$lib/settings.svelte"
  import WidgetEditor from "$ui/grid/WidgetEditor.svelte"
  import { onMount } from "svelte"

  onMount(() => {
    const presetID = page.url.searchParams.get("preset")
    if (presetID == null) return
    settings.selectedManagerID = presetID
  })
</script>

<WidgetEditor gridManager={settings.currentGrid} />
