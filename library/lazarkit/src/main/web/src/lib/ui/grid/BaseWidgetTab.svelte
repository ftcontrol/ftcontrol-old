<script lang="ts">
  import ContextMenu from "./ContextMenu.svelte"

  import { info } from "$lib"
  import SelectInput from "$ui/primitives/SelectInput.svelte"
  import { allWidgetTypes, WidgetTypes, type Module } from "./grid.svelte"
  import { hover } from "./hover.svelte"

  let { m, index }: { m: Module; index: number } = $props()
  let t = $derived(m.types[index])

  function removeType() {
    hover.closeContextMenu()

    m.types.splice(index, 1)
    if (m.activeType == index) {
      if (m.activeType > 0) {
        m.activeType = m.activeType - 1
      } else {
        m.activeType = 0
      }
    }
  }
</script>

<div
  class="base"
  role="button"
  tabindex={index}
  oncontextmenu={(event: MouseEvent) => {
    event.preventDefault()
    hover.openContextMenu(m.id, index)
  }}
>
  <button
    class="tab"
    class:selected={index == m.activeType}
    onclick={() => (m.activeType = index)}
    onmousedown={(event: MouseEvent) => {
      hover.startMoving(event.clientX, event.clientY, index, m.id)
    }}>{t.type}</button
  >
  {#if hover.isContextOpened(m.id, index)}
    <ContextMenu>
      <button class="button" onclick={removeType}>Remove</button>

      <SelectInput
        keepOpened={true}
        type={""}
        startValue={m.types[index].type}
        bind:currentValue={m.types[index].type}
        value={m.types[index].type}
        possibleValues={allWidgetTypes}
        isValid={true}
        alwaysValid={true}
      ></SelectInput>
      {#if m.types[index].type == WidgetTypes.CUSTOM}
        <SelectInput
          keepOpened={true}
          type={""}
          startValue={m.types[index].pluginID}
          bind:currentValue={m.types[index].pluginID}
          value={m.types[index].pluginID}
          possibleValues={[...info.plugins.map((it) => it.id), "none"]}
          isValid={true}
          alwaysValid={true}
        ></SelectInput>
        {#if m.types[index].pluginID != "none"}
          <SelectInput
            keepOpened={true}
            type={""}
            startValue={m.types[index].pageID}
            bind:currentValue={m.types[index].pageID}
            value={m.types[index].pageID}
            possibleValues={[
              ...(
                info.plugins.find((it) => it.id == m.types[index].pluginID)
                  ?.pages ?? []
              ).map((it) => it.id),
              "none",
            ]}
            isValid={true}
            alwaysValid={true}
          ></SelectInput>
        {/if}
      {/if}
    </ContextMenu>
  {/if}
</div>

<style>
  button {
    background-color: transparent;
    border: none;
    cursor: pointer;
    user-select: none;
    color: inherit;
  }

  .base {
    outline: 1px solid var(--text);
    position: relative;
  }
  .tab {
    padding: 0.25rem 0.5rem;
    opacity: 0.5;
  }

  .tab.selected {
    opacity: 1;
  }
</style>
