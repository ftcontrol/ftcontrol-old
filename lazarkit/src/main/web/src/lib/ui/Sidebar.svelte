<script lang="ts">
  import Logo from "./icons/Logo.svelte"
</script>

<nav>
  <Logo />

  <div class="gap"></div>

  <h2>General</h2>

  <a href="/control">Robot Control</a>
  <a href="/time">Global Time</a>
  <a href="/debug">Debug</a>
  <a href="/controller">Controller</a>
</nav>

<style>
  nav {
    outline: 1px solid black;
    padding: 1rem 1rem 1rem 1rem;
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
