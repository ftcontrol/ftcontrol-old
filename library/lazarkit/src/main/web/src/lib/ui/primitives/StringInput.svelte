<script lang="ts">
  import { socket } from "$lib"
  import TextTypedInput from "./TextTypedInput.svelte"

  let {
    startValue = $bindable(),
    currentValue = $bindable(),
    isValid = $bindable(),
    value = $bindable(),
    validate = (value: string) => true,
    extraChar = "",
    type = "",
    alwaysValid = false,
  }: {
    startValue: string
    currentValue: string
    isValid: boolean
    value: string
    validate: (value: string) => boolean
    extraChar?: string
    type?: string
    alwaysValid?: boolean
  } = $props()

  $effect(() => {
    if (value != null) return
    value = startValue
    isValid = validate(value)
  })

  $effect(() => {
    if (socket.state == "closed" || socket.state == "opened") {
      if (!validate(value)) {
        isValid = false
        return
      }
      isValid = true
    }
  })
</script>

<TextTypedInput
  {type}
  bind:text={value}
  oninput={() => {
    if (!validate(value)) {
      isValid = false
      return
    }
    isValid = true
    currentValue = value
  }}
  isValid={isValid || socket.state == "opened" || alwaysValid}
  {extraChar}
/>
