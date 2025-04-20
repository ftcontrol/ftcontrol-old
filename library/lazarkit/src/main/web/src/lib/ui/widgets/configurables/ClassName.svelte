<script lang="ts">
  import Toggle from "./Toggle.svelte"

  let { name, isOpened = $bindable() }: { name: string; isOpened: boolean } =
    $props()

  function getClassName(item: string): string {
    return item.split(".").at(-1) || ""
  }
  function getStartClassName(item: string): string {
    return item.split("." + getClassName(item))[0]
  }
</script>

<Toggle bind:isOpened>
  <h3 data-class={getStartClassName(name)}>
    {getClassName(name)}
  </h3>
</Toggle>

<style>
  h3 {
    margin: 0;
    font-size: 14px;
    margin-block: 0.5rem;
    position: relative;
  }
  h3::before {
    content: attr(data-class);
    position: absolute;
    top: -0.85rem;
    left: -1rem;
    padding-inline: 0.225rem;
    background-color: var(--card);
    font-size: 0.7em;
    opacity: 0;
    transition: opacity var(--d2) ease-in-out;
  }
  h3:hover::before {
    opacity: 1;
  }
</style>
