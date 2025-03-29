<script lang="ts">
  let {
    currentValue = $bindable(),
    onchange = (newValue: number) => {},
  }: {
    currentValue: number
    onchange?: (newValue: number) => void
  } = $props()

  function setValue(newValue: number) {
    onchange(newValue)
  }
  let startValue = currentValue
</script>

{#if startValue != currentValue}
  <button onclick={() => setValue(currentValue)}> Update </button><br />
{/if}
<input type="number" bind:value={currentValue} />

<style>
</style>
