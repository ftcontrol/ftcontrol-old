<script lang="ts">
  import { notifications } from "$lib"
  import HorizontalIcon from "$ui/icons/HorizontalIcon.svelte"
  import HorizontalReversed from "$ui/icons/HorizontalReversed.svelte"
  import VerticalIcon from "$ui/icons/VerticalIcon.svelte"
  import VerticalReversed from "$ui/icons/VerticalReversed.svelte"
  import ContextMenu from "./ContextMenu.svelte"
  import { modular } from "./logic/modular"
  import type { PresetManager } from "./logic/preset.svelte"
  import { WidgetTypes, type WidgetGroup } from "./logic/types"

  let { m, gridManager }: { m: WidgetGroup; gridManager: PresetManager } =
    $props()
</script>

<ContextMenu id={m.id}>
  <button
    class="button"
    onclick={() => {
      gridManager.remove(m.id)
    }}
  >
    Remove Widget Group
  </button>
  <button
    class="button"
    onclick={() => {
      m.widgets.push({
        type: WidgetTypes.TEST,
      })
      console.log(m)
      modular.context.closeContextMenu()
    }}>Add tab</button
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
    <HorizontalIcon /> Expand horizontally
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
    <HorizontalReversed /> Shrink horizontally
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
    <VerticalIcon /> Expand vertically
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
    <VerticalReversed /> Shrink vertically
  </button>
</ContextMenu>

<style>
  button {
    background-color: transparent;
    border: none;
    cursor: pointer;
    user-select: none;
    color: inherit;
    outline: 1px solid var(--text);
    display: flex;
    align-items: center;
    gap: 0.25rem;
    min-height: 24px;
  }
</style>
