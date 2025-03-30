<script lang="ts">
  let {
    startValue = $bindable(),
    currentValue = $bindable(),
    isValid = $bindable(),
    value = $bindable(),
    validate = (value: string) => true,
    extraChar = "",
  }: {
    startValue: string
    currentValue: string
    isValid: boolean
    value: string
    validate: (value: string) => boolean
    extraChar?: string
  } = $props()

  $effect(() => {
    if (value != null) return
    value = startValue
  })
</script>

<input
  type="text"
  class:invalid={!isValid}
  bind:value
  oninput={() => {
    if (!validate(value)) {
      isValid = false
      return
    }
    isValid = true
    currentValue = value
  }}
/>{extraChar}

<style>
  input.invalid {
    opacity: 0.5;
  }
</style>
