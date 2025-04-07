<script lang="ts">
  import type { Snippet } from "svelte"

  let {
    children,
  }: {
    children: Snippet
  } = $props()
</script>

<h2>{@render children()}</h2>

<style>
  h2 {
    margin: 0;
    text-align: center;
    flex-grow: 1;
    font-size: 1.25rem;
  }
</style>
