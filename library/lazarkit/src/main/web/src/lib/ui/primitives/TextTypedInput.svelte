<script lang="ts">
  import TypedInput from "$ui/primitives/TypedInput.svelte"
  import { onMount } from "svelte"

  let {
    text = $bindable(),
    extraChar = "",
    type = "",
    isValid = true,
    oninput = () => {},
    alwaysValid = false,
  }: {
    text: string
    extraChar?: string
    type?: string
    isValid?: boolean
    oninput?: () => void
    alwaysValid?: boolean
  } = $props()
  onMount(() => {
    updateSize()
  })
  function updateSize() {
    input.style.width = Math.max(span.offsetWidth + 2, 100) + "px"
  }
  $effect(() => {
    if (text?.length > 0) return
    updateSize()
  })
  let span: HTMLElement
  let input: HTMLElement
</script>

<TypedInput {type} isValid={isValid || alwaysValid}>
  <div class="flex">
    <input
      bind:this={input}
      type="text"
      bind:value={text}
      oninput={() => {
        updateSize()
        oninput()
      }}
      autocorrect="off"
      spellcheck="false"
    />
    <span class="extra">{extraChar}</span>
  </div>
</TypedInput>
<span class="helper" bind:this={span}>
  {@html text?.replaceAll(" ", "&nbsp;")}
</span>

<style>
  input,
  span.helper {
    all: unset;
    font-size: 14px;
  }
  span.helper {
    position: absolute;
    top: -100rem;
    white-space: pre;
    visibility: hidden;
  }
  .extra {
    width: fit-content;
  }
  .flex {
    display: flex;
    align-items: center;
    margin: 4px;
  }
</style>
