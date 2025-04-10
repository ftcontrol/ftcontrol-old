<script lang="ts">
  import { socket } from "$lib"
  import TypedInput from "./TypedInput.svelte"

  let {
    startValue = $bindable(),
    currentValue = $bindable(),
    isValid = $bindable(),
    value = $bindable(),
    possibleValues,
    type = "ENUM",
    alwaysValid = false,
  }: {
    startValue: string
    currentValue: string
    isValid: boolean
    value: string
    possibleValues: string[]
    type?: string
    alwaysValid?: boolean
  } = $props()
  $effect(() => {
    if (value != null) return
    value = startValue
    isValid = true
  })
  let isShown = $state(false)
</script>

<TypedInput {type} isValid={socket.state == "opened" || alwaysValid}>
  <div class="content">
    <button
      onclick={() => {
        isShown = !isShown
      }}
    >
      {currentValue}
    </button>
  </div>
  {#if isShown}
    <div class="select">
      {#each possibleValues.filter((it) => it != currentValue) as v, index}
        <button
          class:first={index == 0}
          onclick={() => {
            if (socket.state == "closed" && !alwaysValid) return

            currentValue = v
            value = v
            isShown = false
          }}>{v}</button
        >
      {/each}
    </div>
  {/if}
</TypedInput>

<style>
  button {
    all: unset;
    display: block;
    width: 100%;
    padding-left: 0.25rem;
    cursor: pointer;
  }
  .select {
    position: absolute;
    width: 100%;
    background-color: var(--card);
    border: 1px solid var(--text);
    z-index: 10;
  }
  .content {
    position: relative;
  }
</style>
