<script lang="ts">
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
    <div class="context-menu">
      <button onclick={removeType}>Remove</button>

      <SelectInput
        keepOpened={true}
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
          keepOpened={true}
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
            keepOpened={true}
            type={""}
            startValue={m.types[m.activeType].pageID}
            bind:currentValue={m.types[m.activeType].pageID}
            value={m.types[m.activeType].pageID}
            possibleValues={[
              ...(info.plugins.find(
                (it) => it.id == m.types[m.activeType].pluginID
              ) || [].pages.map((it) => it.id)),
              "none",
            ]}
            isValid={true}
            alwaysValid={true}
          ></SelectInput>
        {/if}
      {/if}
    </div>
  {/if}
</div>

<style>
  .context-menu {
    background: var(--card);
    color: var(--text);
    border: 1px solid var(--text);
    padding: 0.5rem;
    z-index: 1000;
    position: absolute;
    left: 0;
    top: 2rem;
    min-width: 100%;
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
  }
  .context-menu button {
    width: 100%;
    padding: 0.25rem 0.5rem;
    border: 1px solid var(--text);
  }

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
