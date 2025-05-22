<script lang="ts">
  import Section from "$ui/primitives/Section.svelte"
  import BaseWidgetContent from "./BaseWidgetContent.svelte"
  import BaseWidgetTab from "./BaseWidgetTab.svelte"
  import { Grid, type Module } from "./grid.svelte"
  import { hover } from "./hover.svelte"
  import WidgetContextMenu from "./WidgetContextMenu.svelte"

  let { m, gridManager }: { m: Module; gridManager: Grid } = $props()
</script>

<!-- svelte-ignore a11y_no_noninteractive_element_interactions -->
<Section widgetID={m.id}>
  <!-- svelte-ignore a11y_click_events_have_key_events -->
  <nav
    onclick={(event: MouseEvent) => {
      const x = event.clientX
      const y = event.clientY
      const element = document.querySelectorAll(`.widget[data-id="${m.id}"]`)[0]
      const box = element.getBoundingClientRect()
      m.sizes.x
      box.x
      box.width
      // console.log(box.x, box.width, x)
      // console.log(x, y, element)
      const xTile =
        m.start.x + Math.ceil(((x - box.x) / box.width) * m.sizes.x) - 1
      const YTile =
        m.start.y + Math.ceil(((y - box.y) / box.height) * m.sizes.y) - 1

      console.log(xTile, YTile)
    }}
  >
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
          <WidgetContextMenu {m} {gridManager} />
        {/if}
      </div>
    {/if}
  </nav>

  {#each m.types as item, index}
    <div class="content" class:shown={index == m.activeType}>
      <BaseWidgetContent {item} />
    </div>
  {/each}

  <button
    class="resizer"
    onmousedown={() => {
      hover.startResizing(m)
    }}
  >
    Resize
  </button>
</Section>

<style>
  nav {
    display: flex;
    flex-wrap: wrap;
    padding: 1rem;
    gap: 0.5rem;
    background-color: green;
    cursor: grab;
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
</style>
