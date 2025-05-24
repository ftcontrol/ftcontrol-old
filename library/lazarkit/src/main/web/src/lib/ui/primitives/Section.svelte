<script lang="ts">
  import type { Snippet } from "svelte"

  let {
    children,
    maxHeight = false,
    isDisabled = false,
  }: {
    children?: Snippet
    maxHeight?: boolean
    isDisabled?: boolean
  } = $props()
</script>

<section class:maxHeight class:isDisabled>
  {@render children?.()}
</section>

<style>
  section {
    background-color: var(--card);
    position: relative;
    opacity: 1;
    transition: opacity var(--d3);
    display: flex;
    flex-direction: column;
    height: 100%;
  }
  section.maxHeight {
    height: 100%;
  }
  section.isDisabled {
    opacity: 0.5;
  }
</style>
