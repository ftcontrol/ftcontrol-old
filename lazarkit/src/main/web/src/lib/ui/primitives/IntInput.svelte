<script lang="ts">
  let {
    currentValue = $bindable(),
    onchange = (newValue: number) => {},
  }: {
    currentValue: number
    onchange?: (newValue: number) => void
  } = $props()

  function setValue(newValue: number) {
    currentValue = newValue
    onchange(newValue)
  }
</script>

<input type="number" bind:value={currentValue} />

<style>
</style>
