<script lang="ts">
  import type { Snippet } from "svelte"
  import Header from "./Header.svelte"
  import Title from "./Title.svelte"

  let {
    children,
    title = "",
    maxHeight = false,
    isDisabled = false,
    isPrimary = false,
    widgetID = "",
  }: {
    children?: Snippet
    title?: string
    maxHeight?: boolean
    isDisabled?: boolean
    isPrimary?: boolean
    widgetID?: string
  } = $props()
</script>

<section
  class="widget"
  class:maxHeight
  class:isDisabled
  class:isPrimary
  data-id={widgetID}
>
  {#if title != ""}
    <Header>
      <Title>{title}</Title>
    </Header>
  {/if}
  {@render children?.()}
</section>

<style>
  section {
    position: relative;
    opacity: 1;
    transition: opacity var(--d3);
    display: flex;
    flex-direction: column;
    height: 100%;
  }
  section.maxHeight {
    height: 100%;
  }
  section.isDisabled {
    opacity: 0.5;
  }
  section.isPrimary {
    background-color: var(--primary);
  }
</style>
