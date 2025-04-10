<script lang="ts">
  import type { Snippet } from "svelte"
  import Header from "./Header.svelte"
  import Title from "./Title.svelte"

  let {
    children,
    title = "",
    hasMargin = true,
  }: {
    children: Snippet
    title?: string
    hasMargin?: boolean
  } = $props()
</script>

<section class:hasMargin>
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
    height: 100%;
    transition: background-color 0.5s;
  }
  section.hasMargin {
    margin: 0.5rem;
    margin-bottom: 1rem;
  }
  .main {
    padding-bottom: 1rem;
    padding-inline: 1rem;
    min-height: 100px;
  }
</style>
