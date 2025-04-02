<script lang="ts">
  import type { Snippet } from "svelte"

  let {
    isOpened = $bindable(),
    children,
  }: {
    isOpened: boolean
    children: Snippet
  } = $props()
</script>

<div>
  <button
    onclick={() => {
      if (isOpened == true) {
        isOpened = false
      } else if (isOpened == false) {
        isOpened = true
      } else {
        isOpened = true
      }
    }}
  >
    Toggle
  </button>
  {@render children()}
</div>

<style>
  div {
    display: flex;
  }
</style>
