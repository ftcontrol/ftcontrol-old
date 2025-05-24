<script lang="ts">
  import ResizerIcon from "$ui/icons/ResizerIcon.svelte"
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
      class:selected={modular.moving.movingModule?.id == m.id}
      onmousedown={(event: MouseEvent) => {
        modular.moving.startMov(m, event.clientX, event.clientY)
      }}
      aria-label="Mover"
    ></button>
    {#each m.widgets as t, index}
      {#if modular.tabs.showExtra(m.id, index)}
        <div
          class="extra-small"
          data-id={m.id}
          data-index={index}
          class:selected={m.id == modular.tabs.hoveringID &&
            index == modular.tabs.hoveringIndex}
        ></div>
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
        class:selected={(m.id == modular.tabs.hoveringID &&
          m.widgets.length == modular.tabs.hoveringIndex) ||
          modular.context.isContextOpened(m.id, -1)}
        oncontextmenu={(event: MouseEvent) => {
          event.preventDefault()
          modular.context.openContextMenu(m.id, -1)
        }}
      >
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
    <ResizerIcon />
  </button>
</Section>

<style>
  .mover {
    cursor: grab;
    position: absolute;
    top: -4px;
    width: 50%;
    left: 25%;
    height: 8px;
    opacity: 0.25;
  }
  .mover.selected,
  .mover:hover {
    opacity: 1;
  }
  nav {
    display: flex;
    flex-wrap: wrap;
    padding: 1rem;
    gap: 0.5rem;
  }
  .extra {
    flex-grow: 1;
    outline: 1px solid var(--text);
    opacity: 0.5;
    min-height: 24px;

    position: relative;
  }

  .extra-small {
    outline: 1px solid var(--text);
    opacity: 0.5;
    width: 16px;
  }
  .extra.selected,
  .extra-small.selected,
  .extra:hover {
    opacity: 1;
  }
  .resizer {
    all: unset;
    background-color: transparent;
    width: 20px;
    height: 20px;
    cursor: grabbing;
    position: absolute;
    right: 0.5rem;
    bottom: 0.5rem;
  }
  .content {
    display: none;
    overflow-y: auto;
  }
  .content.shown {
    display: block;
  }
</style>
