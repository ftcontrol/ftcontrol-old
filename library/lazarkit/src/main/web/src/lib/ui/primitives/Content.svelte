<script lang="ts">
  import type { Snippet } from "svelte"

  let {
    children,
  }: {
    children?: Snippet
  } = $props()
</script>

<div class="main">
  {@render children?.()}
</div>

<style>
  .main {
    padding-bottom: 1rem;
    padding-inline: 1rem;
    overflow: auto;
    flex-grow: 1;
  }
</style>
