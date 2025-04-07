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
  }
  .header {
    padding-top: 1rem;
    margin-bottom: 1rem;
  }
</style>
