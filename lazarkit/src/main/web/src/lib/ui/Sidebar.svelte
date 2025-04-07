<script lang="ts">
  import Arrow from "./icons/Arrow.svelte"
  import Logo from "./icons/Logo.svelte"
  import Button from "./primitives/Button.svelte"
  let isOpened = $state(true)

  function getThemeFromCookie() {
    const match = document.cookie.match(/(?:^|; )theme=(dark|light)/)
    return match ? match[1] : null
  }

  function applyInitialTheme() {
    const savedTheme = getThemeFromCookie()
    const isDark = savedTheme === "dark"

    document.body.classList.toggle("dark-mode", isDark)
  }

  applyInitialTheme()
  import { tick } from "svelte"
  let isCovering = $state(false)

  async function toggleTheme() {
    isCovering = true
    await tick()

    const cover = document.querySelector(".cover") as HTMLElement
    cover.classList.remove("applied")

    await new Promise((r) => setTimeout(r, 250))

    const isDark = document.body.classList.toggle("dark-mode")
    document.cookie = `theme=${isDark ? "dark" : "light"}; path=/; max-age=31536000`

    await new Promise((r) => setTimeout(r, 450))

    cover.classList.add("applied")

    await new Promise((r) => setTimeout(r, 250))
    isCovering = false
  }
</script>

<div class="cover" class:applied={!isCovering}></div>
<button
  class="arrow"
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

    <div class="gap"></div>
    <div class="gap"></div>
    <div class="gap"></div>
    <div class="gap"></div>
    <div class="gap"></div>
    <div class="gap"></div>
    <div class="gap"></div>

    <Button onclick={toggleTheme}>White Mode</Button>
  </nav>
</section>

<style>
  .cover {
    background-color: var(--bg);
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    z-index: 10000;
    transition: all 0.25s;
  }
  .cover.applied {
    top: -100%;
  }
  button.arrow {
    all: unset;
    cursor: pointer;
    position: absolute;
    top: 50%;
    left: 125px;
    transform: translateY(-50%);
    z-index: 100;

    transition: left 0.2s;
  }
  button.arrow.hidden {
    left: 4px;
  }
  nav {
    padding: 1rem 1rem 1rem 1rem;
    background-color: var(--card);
    border-radius: 0 16px 16px 0;
    height: 100%;
    width: fit-content;
    transition: background-color 0.5s;
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
