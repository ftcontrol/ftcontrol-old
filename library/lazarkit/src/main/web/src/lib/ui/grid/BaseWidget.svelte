<script lang="ts">
  import { info } from "$lib"
  import Section from "$ui/primitives/Section.svelte"
  import BaseWidgetContent from "./BaseWidgetContent.svelte"
  import BaseWidgetTab from "./BaseWidgetTab.svelte"
  import ContextMenu from "./ContextMenu.svelte"
  import { Grid, WidgetTypes, type Module } from "./grid.svelte"
  import GridControls from "./GridControls.svelte"
  import { hover } from "./hover.svelte"

  let { m, gridManager }: { m: Module; gridManager: Grid } = $props()
</script>

<Section>
  <nav>
    {#each m.types as t, index}
      {#if hover.showExtra(m.id, index)}
        <div class="extra-small" data-id={m.id} data-index={index}>
          {index}
        </div>
      {/if}
      {#if hover.showLabel(m.id, index)}
        <BaseWidgetTab {m} {index} />
      {/if}
    {/each}
    {#if hover.showExtra(m.id, m.types.length) || true}
      <div
        class="extra"
        role="button"
        tabindex={0}
        data-id={m.id}
        data-index={m.types.length}
        oncontextmenu={(event: MouseEvent) => {
          event.preventDefault()
          hover.openContextMenu(m.id, -1)
        }}
      >
        {m.types.length}
        {#if hover.isContextOpened(m.id, -1)}
          <ContextMenu>
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
                gridManager.remove(m.id)
              }}
            >
              Remove Widget
            </button>
          </ContextMenu>
        {/if}
      </div>
    {/if}
  </nav>
  {#if info.showEdit}
    <div class="controls">
      <GridControls {m} {gridManager} />
    </div>
  {/if}

  {#each m.types as item, index}
    <div class="content" class:shown={index == m.activeType}>
      <BaseWidgetContent {item} />
    </div>
  {/each}

  <button
    class="resizer"
    onmousedown={() => {
      info.showEdit = true
    }}
    onmouseup={() => {
      info.showEdit = false
    }}
  >
  </button>
</Section>

<style>
  nav {
    display: flex;
    flex-wrap: wrap;
    padding: 1rem;
    gap: 0.5rem;
  }
  .extra {
    flex-grow: 1;
    background-color: blue;

    position: relative;
  }

  .extra-small {
    background-color: blue;
    width: 16px;
  }
  .resizer {
    background-color: red;
    cursor: grabbing;
    width: 32px;
    height: 32px;
    position: absolute;
    right: 0;
    bottom: 0;
  }
  .bar {
    margin-bottom: 1rem;
  }
  .content {
    display: none;
    overflow-y: auto;
  }
  .content.shown {
    display: block;
  }
  .controls {
    width: 100%;
    display: flex;
    flex-wrap: wrap;
    padding: 1rem;
    padding-bottom: 0;
    gap: 0.5rem;
  }

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
  button.base {
    outline: 1px solid var(--text);
    opacity: 0.5;
    padding: 0.25rem 0.5rem;
  }
  button.base.selected {
    opacity: 1;
  }
</style>
