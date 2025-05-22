<script lang="ts">
  import { socket } from "$lib"
  import TypedInput from "./TypedInput.svelte"
  import Portal from "svelte-portal"

  let {
    startValue = $bindable(),
    currentValue = $bindable(),
    isValid = $bindable(),
    value = $bindable(),
    possibleValues,
    type = "ENUM",
    alwaysValid = false,
    keepOpened = false,
  }: {
    startValue: string
    currentValue: string
    isValid: boolean
    value: string
    possibleValues: string[]
    type?: string
    alwaysValid?: boolean
    keepOpened?: boolean
  } = $props()

  $effect(() => {
    if (value != null) return
    value = startValue
    isValid = true
  })

  let isShown = $state(false)
  let buttonElement: HTMLElement | null = null
</script>

<TypedInput {type} isValid={socket.state == "opened" || alwaysValid}>
  <div class="content">
    <button
      bind:this={buttonElement}
      onclick={() => {
        isShown = !isShown
      }}
    >
      {currentValue}
    </button>
  </div>

  {#if isShown && buttonElement}
    <Portal>
      <div
        class="select"
        class:keepOpened
        style={`top: ${buttonElement.getBoundingClientRect().bottom}px; left: ${buttonElement.getBoundingClientRect().left}px;`}
      >
        {#each possibleValues.filter((it) => it != currentValue) as v, index}
          <button
            class:keepOpened
            class:first={index == 0}
            onclick={() => {
              if (socket.state == "closed" && !alwaysValid) return
              currentValue = v
              value = v
              isShown = false
            }}
          >
            {v}
          </button>
        {/each}
      </div>
    </Portal>
  {/if}
</TypedInput>

<style>
  button {
    all: unset;
    display: block;
    width: 100%;
    padding-left: 0.25rem;
    cursor: pointer;
    text-wrap: nowrap;
  }

  .content {
    position: relative;
  }

  .select {
    position: absolute;
    width: max-content;
    min-width: 80px;
    max-height: 90px;
    overflow-x: hidden;
    overflow-y: auto;
    z-index: 1000;
    background-color: var(--card);
    border: 1px solid var(--text);
    box-sizing: border-box;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }
</style>
