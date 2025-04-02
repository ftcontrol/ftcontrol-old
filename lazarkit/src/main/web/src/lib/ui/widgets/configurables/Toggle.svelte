<script lang="ts">
  import Arrow from "$ui/icons/Arrow.svelte"
  import type { Snippet } from "svelte"

  let {
    isOpened = $bindable(),
    children,
  }: {
    isOpened: boolean
    children: Snippet
  } = $props()
</script>

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
  <Arrow {isOpened} />
  {@render children()}
</button>

<style>
  button {
    all: unset;
    cursor: pointer;
    display: flex;
    gap: 0.6rem;
    align-items: center;
  }
</style>
