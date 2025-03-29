<script lang="ts">
  let {
    startValue = $bindable(),
    currentValue = $bindable(),
    isValid = $bindable(),
  }: {
    startValue: string
    currentValue: string
    isValid: boolean
  } = $props()

  let value = $state(parseInt(startValue))

  let innerIsValid = $derived.by(() => {
    for (const char of value.toString()) {
      if (!["0", "1", "2", "3", "4", "5", "6", "7", "8", "9"].includes(char)) {
        return false
      }
    }
    return true
  })
</script>

<input
  type="text"
  class:invalid={!isValid}
  bind:value
  oninput={() => {
    if (!innerIsValid) {
      isValid = false
      return
    }
    isValid = true
    currentValue = value.toString()
  }}
/>

<style>
  input.invalid {
    opacity: 0.5;
  }
</style>
