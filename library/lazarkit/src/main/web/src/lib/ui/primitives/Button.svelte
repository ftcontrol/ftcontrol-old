<script lang="ts">
  import type { Snippet } from "svelte"

  let {
    disabled = false,
    onclick = () => {},
    children,
  }: {
    disabled?: boolean | null | undefined
    onclick?: () => void
    children: Snippet
  } = $props()
</script>

<button {disabled} {onclick}>
  {@render children()}
</button>

<style>
  button {
    border: none;
    background-color: transparent;
    margin: 0;
    background-color: var(--bg);
    color: var(--text);
    padding: 1em 2em;
    flex-grow: 1;
    border-radius: 10px;
    cursor: pointer;
    text-wrap: nowrap;
  }
  button:disabled {
    opacity: 0.5;
  }
</style>
