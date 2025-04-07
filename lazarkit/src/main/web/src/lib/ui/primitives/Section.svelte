<script lang="ts">
  import type { Snippet } from "svelte"
  import Header from "./Header.svelte"
  import Title from "./Title.svelte"

  let {
    children,
    title = "",
  }: {
    children: Snippet
    title?: string
  } = $props()
</script>

<section>
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
    position: relative;
    border-radius: 16px;
    margin: 0.5rem;
    margin-bottom: 1rem;
    overflow-x: auto;
    overflow-y: hidden;

    min-width: 400px;
  }
  .main {
    padding-bottom: 1rem;
    padding-inline: 1rem;
    min-height: 100px;
  }
</style>
