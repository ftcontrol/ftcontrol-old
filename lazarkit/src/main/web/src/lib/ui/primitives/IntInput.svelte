<script lang="ts">
  let {
    startValue = $bindable(),
    currentValue = $bindable(),
  }: {
    startValue: string
    currentValue: string
  } = $props()

  let value = $state(parseInt(startValue))
</script>

<input
  type="number"
  bind:value
  oninput={() => {
    currentValue = value.toString()
  }}
/>

<style>
</style>
