<script lang="ts">
  import ContextMenu from "./ContextMenu.svelte"

  import { info } from "$lib"
  import SelectInput from "$ui/primitives/SelectInput.svelte"
  import { allWidgetTypes, WidgetTypes, type WidgetGroup } from "./logic/types"
  import { modular } from "./logic/modular"

  let { m, index }: { m: WidgetGroup; index: number } = $props()
  let t = $derived(m.widgets[index])

  function removeType() {
    modular.context.closeContextMenu()

    m.widgets.splice(index, 1)
    if (m.activeWidgetID == index) {
      if (m.activeWidgetID > 0) {
        m.activeWidgetID = m.activeWidgetID - 1
      } else {
        m.activeWidgetID = 0
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
    modular.context.openContextMenu(m.id, index)
  }}
>
  <button
    class="tab"
    class:selected={index == m.activeWidgetID}
    onclick={() => (m.activeWidgetID = index)}
    onmousedown={(event: MouseEvent) => {
      modular.tabs.startMoving(event.clientX, event.clientY, index, m.id)
    }}>{t.type}</button
  >
  {#if modular.context.isContextOpened(m.id, index)}
    <ContextMenu id={m.id}>
      <button class="button" onclick={removeType}>Remove</button>

      <SelectInput
        keepOpened={true}
        type={""}
        startValue={m.widgets[index].type}
        bind:currentValue={m.widgets[index].type}
        value={m.widgets[index].type}
        possibleValues={allWidgetTypes}
        isValid={true}
        alwaysValid={true}
      ></SelectInput>
      {#if m.widgets[index].type == WidgetTypes.CUSTOM}
        {#if info.plugins.length == 0}
          <p>No plugins found</p>
        {:else}
          <SelectInput
            keepOpened={true}
            type={""}
            startValue={m.widgets[index].pluginID}
            bind:currentValue={m.widgets[index].pluginID}
            value={m.widgets[index].pluginID}
            possibleValues={[...info.plugins.map((it) => it.id), "none"]}
            isValid={true}
            alwaysValid={true}
          ></SelectInput>
          {#if m.widgets[index].pluginID != "none"}
            <SelectInput
              keepOpened={true}
              type={""}
              startValue={m.widgets[index].pageID}
              bind:currentValue={m.widgets[index].pageID}
              value={m.widgets[index].pageID}
              possibleValues={[
                ...(
                  info.plugins.find(
                    (it) =>
                      m.widgets[index].type == WidgetTypes.CUSTOM &&
                      it.id == m.widgets[index].pluginID
                  )?.pages ?? []
                ).map((it) => it.id),
                "none",
              ]}
              isValid={true}
              alwaysValid={true}
            ></SelectInput>
          {/if}
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
