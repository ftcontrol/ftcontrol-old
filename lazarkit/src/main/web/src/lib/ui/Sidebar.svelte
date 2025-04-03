<script lang="ts">
  import Logo from "./icons/Logo.svelte"
</script>

<nav>
  <Logo />

  <div class="gap"></div>

  <h2>General</h2>

  <a href="/control">Robot Control</a>
  <a href="/time">Global Time</a>
  <a href="/controller">Controller</a>
  <a href="/field">Game Field</a>
</nav>

<style>
  nav {
    padding: 1rem 1rem 1rem 1rem;
    background-color: var(--card);
    border-radius: 0 16px 16px 0;
  }

  .gap {
    height: 60px;
  }

  h2 {
    text-transform: uppercase;
  }

  a {
    color: inherit;
    text-decoration: none;
    display: block;
  }
</style>
