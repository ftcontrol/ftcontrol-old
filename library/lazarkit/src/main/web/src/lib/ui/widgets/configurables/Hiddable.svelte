<script lang="ts">
  import type { Snippet } from "svelte"

  let { isShown, children }: { isShown: boolean; children: Snippet } = $props()
</script>

<div class="hiddable" class:shown={isShown}>
  <div class="inner">
    {@render children()}
  </div>
</div>

<style>
  .hiddable {
    display: grid;
    grid-template-rows: 0fr;
    opacity: 0;
    transition:
      grid-template-rows var(--d2) ease-out,
      opacity var(--d2) ease-out;
  }
  .hiddable.shown {
    grid-template-rows: 1fr;
    opacity: 1;
  }
  .inner {
    padding-top: 4px;
    overflow: hidden;
  }
</style>
