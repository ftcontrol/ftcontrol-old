<script lang="ts">
  import type { Snippet } from "svelte"
  import Header from "./Header.svelte"
  import Title from "./Title.svelte"

  let {
    children,
    title = "",
    maxHeight = false,
  }: {
    children: Snippet
    title?: string
    maxHeight?: boolean
  } = $props()
</script>

<section class:maxHeight>
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

    height: 100%;
  }
  section.maxHeight {
    height: 100%;
  }
  .main {
    padding-bottom: 1rem;
    padding-inline: 1rem;
  }
</style>
