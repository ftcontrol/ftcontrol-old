<script lang="ts">
  import type { Snippet } from "svelte"
  import { onMount } from "svelte"

  let { children, id }: { children?: Snippet; id: string } = $props()

  let el: HTMLDivElement | null = null

  function positionContextMenu() {
    if (!el) return ""

    const rect = el.getBoundingClientRect()
    const element = document.querySelectorAll(`.widget[data-id="${id}"]`)[0]
    const rect2 = element.getBoundingClientRect()

    const overflowX = rect.right > rect2.right - 8

    console.log(overflowX)

    if (overflowX) {
      el.style.left = "auto"
      el.style.right = "0"
    } else {
      el.style.left = "0"
      el.style.right = "auto"
    }
    return ""
  }

  onMount(() => {
    positionContextMenu()
  })
</script>

<div class="context-menu" bind:this={el}>
  {@render children?.()}
</div>

<style>
  .context-menu {
    background: var(--card);
    color: var(--text);
    border: 1px solid var(--text);
    padding: 0.5rem;
    z-index: 1000;
    position: absolute;
    top: 2rem;
    min-width: 100%;
    display: flex;
    flex-direction: column;
    gap: 0.5rem;
  }
  .context-menu :global(.button) {
    width: 100%;
    padding: 0.25rem 0.5rem;
    border: 1px solid var(--text);
  }
</style>
