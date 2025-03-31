<script lang="ts">
  import type { Snippet } from "svelte"

  let {
    beforeTitle,
    afterTitle,
    children,
    title,
  }: {
    beforeTitle?: Snippet
    afterTitle?: Snippet
    children: Snippet
    title: string
  } = $props()
</script>

<section>
  <div class="header">
    {#if beforeTitle}
      {@render beforeTitle()}
    {/if}
    <h2>{title}</h2>
    {#if afterTitle}
      {@render afterTitle()}
    {/if}
  </div>
  <div class="main">
    {@render children()}
  </div>
</section>

<style>
  .header {
    display: flex;
    gap: 1rem;
    justify-content: space-between;
    position: relative;
  }
  h2 {
    margin: 0;
    text-align: center;
    flex-grow: 1;
  }
  section {
    background-color: var(--card);
    position: relative;
    border-radius: 16px;
    margin: 0.5rem;
    margin-bottom: 1rem;
    overflow-x: auto;
  }
  .header {
    padding-top: 1rem;
    padding-inline: 1rem;
    margin-bottom: 1rem;
  }
  .main {
    padding-bottom: 1rem;
    padding-inline: 1rem;
    min-height: 100px;
  }
</style>
