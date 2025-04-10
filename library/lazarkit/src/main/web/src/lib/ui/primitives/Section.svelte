<script lang="ts">
  import type { Snippet } from "svelte"
  import Header from "./Header.svelte"
  import Title from "./Title.svelte"

  let {
    children,
    title = "",
    hasMargin = true,
    maxHeight = false,
  }: {
    children: Snippet
    title?: string
    hasMargin?: boolean
    maxHeight?: boolean
  } = $props()
</script>

<section class:hasMargin class:maxHeight>
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
    background-color: var(--card);
    border: 2px solid var(--bg);
    position: relative;
    border-radius: 16px;
    overflow-x: auto;
    overflow-y: hidden;

    min-width: 400px;
    transition: background-color var(--d3);
  }
  section.hasMargin {
    margin: 0.5rem;
    margin-bottom: 1rem;
  }
  section.maxHeight {
    height: 100%;
  }
  .main {
    padding-bottom: 1rem;
    padding-inline: 1rem;
    min-height: 100px;
  }
</style>
