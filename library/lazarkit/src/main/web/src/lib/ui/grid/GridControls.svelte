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
  let activeType = $derived(w.types[w.activeType])
</script>

<div class="controls" class:shown={info.showEdit}>
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
    startValue={activeType.type}
    bind:currentValue={activeType.type}
    value={activeType.type}
    possibleValues={allWidgetTypes}
    isValid={true}
    alwaysValid={true}
  ></SelectInput>
  {#if activeType.type == WidgetTypes.CUSTOM}
    <SelectInput
      type={""}
      startValue={activeType.pluginID}
      bind:currentValue={activeType.pluginID}
      value={activeType.pluginID}
      possibleValues={[...info.plugins.map((it) => it.id), "none"]}
      isValid={true}
      alwaysValid={true}
    ></SelectInput>
    {#if activeType.pluginID != "none"}
      <SelectInput
        type={""}
        startValue={activeType.pageID}
        bind:currentValue={activeType.pageID}
        value={activeType.pageID}
        possibleValues={[
          ...info.plugins
            .find((it) => it.id == activeType.pluginID)
            .pages.map((it) => it.id),
          "none",
        ]}
        isValid={true}
        alwaysValid={true}
      ></SelectInput>
    {/if}
  {/if}
</div>

<style>
  .controls {
    display: flex;
    gap: 0.25rem;
    align-items: center;
    padding: 0.25rem;
    margin: 0.25rem;
    flex-wrap: wrap;
    position: absolute;
    z-index: 100;
    top: 0;

    max-height: calc(100% - 0.5rem);
    max-width: calc(100% - 0.5rem);

    border-radius: 0.75rem;
    padding-inline: 0.5rem;
    background: var(--cardTransparent);
    border: 2px solid var(--bg);
    transition:
      background-color var(--d3),
      opacity var(--d3);
    box-shadow: 0 4px 30px rgba(0, 0, 0, 0.1);
    backdrop-filter: blur(10px);
    -webkit-backdrop-filter: blur(10px);
    opacity: 0;
    pointer-events: none;
  }
  .controls.shown {
    opacity: 1;
    pointer-events: all;
  }
  button {
    background-color: transparent;
    border: none;
    cursor: pointer;
    user-select: none;
    display: flex;
    justify-content: center;
    align-items: center;
  }
</style>
