<script lang="ts">
  import Arrow from "./icons/Arrow.svelte"
  import Logo from "./icons/Logo.svelte"
  let isOpened = $state(true)
</script>

<button
  class:hidden={!isOpened}
  onclick={() => {
    isOpened = !isOpened
  }}
>
  <Arrow isOpened={false} isVertical={false} />
</button>
<section class="shell" class:hidden={!isOpened}>
  <nav>
    <Logo />

    <div class="gap"></div>

    <h2>General</h2>

    <a href="/control">Robot Control</a>
    <a href="/time">Global Time</a>
    <a href="/controller">Controller</a>
    <a href="/field">Game Field</a>
  </nav>
</section>

<style>
  button {
    all: unset;
    cursor: pointer;
    position: absolute;
    top: 50%;
    left: 125px;
    transform: translateY(-50%);

    transition: left 0.2s;
  }
  button.hidden {
    left: 4px;
  }
  nav {
    padding: 1rem 1rem 1rem 1rem;
    background-color: var(--card);
    border-radius: 0 16px 16px 0;
    height: 100%;
    width: fit-content;
  }
  .shell {
    max-width: 400px;
    overflow: hidden;

    transition:
      max-width 0.2s,
      padding 0.2s;
  }

  .shell.hidden {
    max-width: 0;
    padding: 0;
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
