<script lang="ts">
  import { info, notifications } from "$lib"
  import HorizontalIcon from "$ui/icons/HorizontalIcon.svelte"
  import HorizontalReversed from "$ui/icons/HorizontalReversed.svelte"
  import MoveIcon from "$ui/icons/MoveIcon.svelte"
  import Remove from "$ui/icons/Remove.svelte"
  import VerticalIcon from "$ui/icons/VerticalIcon.svelte"
  import VerticalReversed from "$ui/icons/VerticalReversed.svelte"
  import SelectInput from "$ui/primitives/SelectInput.svelte"
  import {
    allWidgetTypes,
    WidgetTypes,
    type Grid,
    type Module,
  } from "./grid.svelte"
  let { w, gridManager }: { w: Module; gridManager: Grid } = $props()
</script>

<button
  onmousedown={() => {
    gridManager.startMoving(w.id)
  }}
>
  <MoveIcon />
</button>
<button
  onclick={() => {
    gridManager.remove(w.id)
  }}
>
  <Remove />
</button>
<button
  onclick={() => {
    if (gridManager.canExpandRight(w)) {
      w.sizes.x++
      return
    }

    if (gridManager.canExpandLeft(w)) {
      w.sizes.x++
      w.start.x--
      return
    }

    notifications.add("Not enough space to expand horizontally.")
  }}
>
  <HorizontalIcon />
</button>
<button
  onclick={() => {
    if (w.sizes.x <= 2) {
      notifications.add("Cannot make this small.")
    } else {
      w.sizes.x--
    }
  }}
>
  <HorizontalReversed />
</button>
<button
  onclick={() => {
    if (gridManager.canExpandDown(w)) {
      w.sizes.y++
      return
    }

    if (gridManager.canExpandUp(w)) {
      w.sizes.y++
      w.start.y--
      return
    }

    notifications.add("Not enough space to expand vertically.")
  }}
>
  <VerticalIcon />
</button>
<button
  onclick={() => {
    if (w.sizes.y <= 2) {
      notifications.add("Cannot make this small.")
    } else {
      w.sizes.y--
    }
  }}
>
  <VerticalReversed />
</button>

<SelectInput
  type={""}
  startValue={w.types[w.activeType].type}
  bind:currentValue={w.types[w.activeType].type}
  value={w.types[w.activeType].type}
  possibleValues={allWidgetTypes}
  isValid={true}
  alwaysValid={true}
></SelectInput>
{#if w.types[w.activeType].type == WidgetTypes.CUSTOM}
  <SelectInput
    type={""}
    startValue={w.types[w.activeType].pluginID}
    bind:currentValue={w.types[w.activeType].pluginID}
    value={w.types[w.activeType].pluginID}
    possibleValues={[...info.plugins.map((it) => it.id), "none"]}
    isValid={true}
    alwaysValid={true}
  ></SelectInput>
  {#if w.types[w.activeType].pluginID != "none"}
    <SelectInput
      type={""}
      startValue={w.types[w.activeType].pageID}
      bind:currentValue={w.types[w.activeType].pageID}
      value={w.types[w.activeType].pageID}
      possibleValues={[
        ...info.plugins
          .find((it) => it.id == w.types[w.activeType].pluginID)
          .pages.map((it) => it.id),
        "none",
      ]}
      isValid={true}
      alwaysValid={true}
    ></SelectInput>
  {/if}
{/if}

<style>
  button {
    background-color: transparent;
    border: none;
    cursor: pointer;
    user-select: none;
    display: flex;
    justify-content: center;
    align-items: center;
    color: inherit;
  }
</style>
