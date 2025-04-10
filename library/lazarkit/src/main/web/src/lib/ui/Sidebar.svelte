<script lang="ts">
  import { info, socket } from "$lib"
  import { settings } from "$lib/settings.svelte"
  import { Logo, Arrow } from "./icons"
  import Button from "./primitives/Button.svelte"
  let isOpened = $state(true)

  import { tick } from "svelte"
  let isCovering = $state(false)

  async function toggleTheme() {
    isCovering = true
    await tick()

    const cover = document.querySelector(".cover") as HTMLElement
    const computedStyles = getComputedStyle(cover)
    const multiplierStr = computedStyles.getPropertyValue("--multiplier").trim()
    const multiplier = parseFloat(multiplierStr) || 0

    console.log(multiplier)

    cover.classList.remove("applied")

    await new Promise((r) => setTimeout(r, 4250 * multiplier))

    settings.toggleIsDark()

    await new Promise((r) => setTimeout(r, 4250 * multiplier))

    cover.classList.add("applied")

    await new Promise((r) => setTimeout(r, 3250 * multiplier))
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
    <p>{socket.state}</p>

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

    <Button onclick={toggleTheme}
      >{settings.isDark ? "Light Mode" : "Dark Mode"}</Button
    >
    <Button
      onclick={() => {
        info.showSettings = !info.showSettings
      }}>Settings</Button
    >
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
    transition: all var(--d3);
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

    transition: left var(--d2);
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
    transition: background-color var(--d3);
  }
  .shell {
    max-width: 400px;
    overflow: hidden;

    transition:
      max-width var(--d2),
      padding var(--d2);
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
