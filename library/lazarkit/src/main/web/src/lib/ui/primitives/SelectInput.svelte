<script lang="ts">
  import TypedInput from "./TypedInput.svelte"

  let {
    startValue = $bindable(),
    currentValue = $bindable(),
    isValid = $bindable(),
    value = $bindable(),
    possibleValues,
    type = "ENUM",
  }: {
    startValue: string
    currentValue: string
    isValid: boolean
    value: string
    possibleValues: string[]
    type?: string
  } = $props()
  $effect(() => {
    if (value != null) return
    value = startValue
    isValid = true
  })
  let isShown = $state(false)
</script>

<TypedInput {type}>
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
  }
  .content {
    position: relative;
  }
</style>
