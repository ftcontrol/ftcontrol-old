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
  let { m, gridManager }: { m: Module; gridManager: Grid } = $props()
</script>

<button
  onmousedown={() => {
    gridManager.startMoving(m.id)
  }}
>
  <MoveIcon />
</button>
<button
  onclick={() => {
    gridManager.remove(m.id)
  }}
>
  <Remove />
</button>
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

<SelectInput
  type={""}
  startValue={m.types[m.activeType].type}
  bind:currentValue={m.types[m.activeType].type}
  value={m.types[m.activeType].type}
  possibleValues={allWidgetTypes}
  isValid={true}
  alwaysValid={true}
></SelectInput>
{#if m.types[m.activeType].type == WidgetTypes.CUSTOM}
  <SelectInput
    type={""}
    startValue={m.types[m.activeType].pluginID}
    bind:currentValue={m.types[m.activeType].pluginID}
    value={m.types[m.activeType].pluginID}
    possibleValues={[...info.plugins.map((it) => it.id), "none"]}
    isValid={true}
    alwaysValid={true}
  ></SelectInput>
  {#if m.types[m.activeType].pluginID != "none"}
    <SelectInput
      type={""}
      startValue={m.types[m.activeType].pageID}
      bind:currentValue={m.types[m.activeType].pageID}
      value={m.types[m.activeType].pageID}
      possibleValues={[
        ...info.plugins
          .find((it) => it.id == m.types[m.activeType].pluginID)
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
