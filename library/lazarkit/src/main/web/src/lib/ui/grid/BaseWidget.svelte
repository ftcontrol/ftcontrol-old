<script lang="ts">
  import Section from "$ui/primitives/Section.svelte"
  import BaseWidgetContent from "./BaseWidgetContent.svelte"
  import BaseWidgetTab from "./BaseWidgetTab.svelte"
  import { modular } from "./logic/modular"
  import type { PresetManager } from "./logic/preset.svelte"
  import type { WidgetGroup } from "./logic/types"
  import WidgetContextMenu from "./WidgetContextMenu.svelte"

  let { m, gridManager }: { m: WidgetGroup; gridManager: PresetManager } =
    $props()
</script>

<Section>
  <nav>
    <button
      class="mover"
      onmousedown={(event: MouseEvent) => {
        modular.moving.startMov(m, event.clientX, event.clientY)
      }}
      aria-label="Mover"
    ></button>
    {#each m.widgets as t, index}
      {#if modular.tabs.showExtra(m.id, index)}
        <div class="extra-small" data-id={m.id} data-index={index}>
          {index}
        </div>
      {/if}
      {#if modular.tabs.showLabel(m.id, index)}
        <BaseWidgetTab {m} {index} />
      {/if}
    {/each}
    {#if modular.tabs.showExtra(m.id, m.widgets.length) || true}
      <div
        class="extra"
        role="button"
        tabindex={0}
        data-id={m.id}
        data-index={m.widgets.length}
        oncontextmenu={(event: MouseEvent) => {
          event.preventDefault()
          modular.context.openContextMenu(m.id, -1)
        }}
      >
        {m.widgets.length}
        {#if modular.context.isContextOpened(m.id, -1)}
          <WidgetContextMenu {m} {gridManager} />
        {/if}
      </div>
    {/if}
  </nav>

  {#each m.widgets as item, index}
    <div class="content" class:shown={index == m.activeWidgetID}>
      <BaseWidgetContent {item} />
    </div>
  {/each}

  <button
    class="resizer"
    onmousedown={() => {
      modular.resizing.startResizing(m)
    }}
  >
    Resize
  </button>
</Section>

<style>
  .mover {
    cursor: grab;
    position: absolute;
    top: 0;
    width: 50%;
    left: 25%;
    height: 8px;
  }
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
  .content {
    display: none;
    overflow-y: auto;
  }
  .content.shown {
    display: block;
  }
</style>
