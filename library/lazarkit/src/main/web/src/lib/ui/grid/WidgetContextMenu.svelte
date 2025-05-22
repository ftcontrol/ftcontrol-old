<script lang="ts">
  import { notifications } from "$lib"
  import HorizontalIcon from "$ui/icons/HorizontalIcon.svelte"
  import HorizontalReversed from "$ui/icons/HorizontalReversed.svelte"
  import VerticalIcon from "$ui/icons/VerticalIcon.svelte"
  import VerticalReversed from "$ui/icons/VerticalReversed.svelte"
  import ContextMenu from "./ContextMenu.svelte"
  import { WidgetTypes, type Grid, type Module } from "./grid.svelte"
  import { hover } from "./hover.svelte"

  let { m, gridManager }: { m: Module; gridManager: Grid } = $props()
</script>

<ContextMenu id={m.id}>
  <button
    class="button"
    onclick={() => {
      gridManager.remove(m.id)
    }}
  >
    Remove Widget
  </button>
  <button
    class="button"
    onclick={() => {
      m.types.push({
        pluginID: "none",
        pageID: "none",
        type: WidgetTypes.TEST,
      })
      console.log(m)
      hover.closeContextMenu()
    }}>Add tab</button
  >

  <button
    class="button"
    onclick={() => {
      gridManager.addNewAnywhere()
      hover.closeContextMenu()
    }}>Add widget</button
  >

  <button
    onclick={() => {
      if (gridManager.canExpandRight(m)) {
        m.sizes.x++
        return
      }

      if (gridManager.canExpandLeft(m)) {
        m.sizes.x++
        m.start.x--
        return
      }

      notifications.add("Not enough space to expand horizontally.")
    }}
  >
    <HorizontalIcon />
  </button>
  <button
    onclick={() => {
      if (m.sizes.x <= 2) {
        notifications.add("Cannot make this small.")
      } else {
        m.sizes.x--
      }
    }}
  >
    <HorizontalReversed />
  </button>
  <button
    onclick={() => {
      if (gridManager.canExpandDown(m)) {
        m.sizes.y++
        return
      }

      if (gridManager.canExpandUp(m)) {
        m.sizes.y++
        m.start.y--
        return
      }

      notifications.add("Not enough space to expand vertically.")
    }}
  >
    <VerticalIcon />
  </button>
  <button
    onclick={() => {
      if (m.sizes.y <= 2) {
        notifications.add("Cannot make this small.")
      } else {
        m.sizes.y--
      }
    }}
  >
    <VerticalReversed />
  </button>
</ContextMenu>
