<script lang="ts">
  let { name }: { name: string } = $props()

  function getClassName(item: string): string {
    return item.split(".").at(-1) || ""
  }
  function getStartClassName(item: string): string {
    return item.split("." + getClassName(item))[0]
  }
</script>

<h3>{getClassName(name)} <span>of {getStartClassName(name)}</span></h3>

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
