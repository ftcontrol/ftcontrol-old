<script lang="ts">
  import type { Snippet } from "svelte"

  let {
    children,
  }: {
    children: Snippet
  } = $props()
</script>

<div class="header">
  {@render children()}
</div>

<style>
  .header {
    display: flex;
    gap: 1rem;
    justify-content: space-between;
    align-items: center;
    position: relative;
    margin-inline: 1rem;
  }
  .header {
    padding-top: 0.5rem;
    margin-bottom: 0.5rem;
  }
</style>
