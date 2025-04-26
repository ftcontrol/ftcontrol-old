<script lang="ts">
  import type { Snippet } from "svelte"
  import Header from "./Header.svelte"
  import Title from "./Title.svelte"

  let {
    children,
    title = "",
    maxHeight = false,
    isDisabled = false,
  }: {
    children: Snippet
    title?: string
    maxHeight?: boolean
    isDisabled?: boolean
  } = $props()
</script>

<section class:maxHeight class:isDisabled>
  {#if title != ""}
    <Header>
      <Title>{title}</Title>
    </Header>
  {/if}
  <div class="main">
    {@render children()}
  </div>
</section>

<style>
  section {
    position: relative;
    opacity: 1;
    transition: opacity var(--d3);

    height: 100%;
  }
  section.maxHeight {
    height: 100%;
  }
  section.isDisabled {
    opacity: 0.5;
  }
  .main {
    padding-bottom: 1rem;
    padding-inline: 1rem;
    overflow: auto;
    height: 100%;
  }
</style>
