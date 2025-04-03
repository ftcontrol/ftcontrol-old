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
  <h3>{getClassName(name)} <span>of {getStartClassName(name)}</span></h3>
</Toggle>

<style>
  span {
    opacity: 0.5;
  }
  h3 {
    margin: 0;
    font-size: 14px;
    margin-block: 0.5rem;
  }
</style>
