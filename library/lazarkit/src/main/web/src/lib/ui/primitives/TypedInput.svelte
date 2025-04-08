<script lang="ts">
  import type { Snippet } from "svelte"

  let {
    type,
    children,
    isValid = true,
  }: {
    type: string
    children: Snippet
    isValid?: boolean
  } = $props()
</script>

<div class="field" data-type={type} class:invalid={!isValid}>
  <div class="content">
    {@render children()}
  </div>
</div>

<style>
  div.field {
    border: 1px solid var(--text);
    width: fit-content;
    min-width: 80px;
    position: relative;
    margin-top: -2px;
  }
  div.field.invalid {
    opacity: 0.5;
  }
  div.field::before {
    content: attr(data-type);
    position: absolute;
    top: -0.55rem;
    left: 0.25rem;
    padding-inline: 0.225rem;
    background-color: var(--card);
    font-size: 0.8em;
  }
  div.content {
    position: relative;
    margin-top: 4px;
  }
</style>
