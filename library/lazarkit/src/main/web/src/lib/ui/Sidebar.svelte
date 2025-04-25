<script lang="ts">
  import { info, socket } from "$lib"
  import { settings } from "$lib/settings.svelte"
  import { allKeys } from "./grid"
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

<section class="shell" class:hidden={!isOpened}>
  <button
    class="arrow"
    class:hidden={!isOpened}
    onclick={() => {
      isOpened = !isOpened
    }}
  >
    <Arrow isOpened={!isOpened} isVertical={true} />
  </button>
  <nav class:hidden={!isOpened}>
    <div>
      <Logo />
      <p>{socket.state == "opened" ? "Connected to server" : "Disconnected"}</p>
      <p>{info.batteryVoltage}V</p>

      <div class="gap"></div>

      <h2>General</h2>

      {#each allKeys as title}
        <a
          href="/?preset={title}"
          onclick={() => {
            info.selectedManager = title
          }}>{title}</a
        >
      {/each}
      <a href="/limelight">Limelight</a>

      <h2>Developer</h2>
      <a href="/time">Global Time</a>
      <a href="/controller">Controller</a>
      <a href="/field">Game Field</a>
    </div>

    <div class="item">
      <Button onclick={toggleTheme}
        >{settings.isDark ? "Light Mode" : "Dark Mode"}</Button
      >
      <Button
        onclick={() => {
          info.showSettings = !info.showSettings
        }}>Settings</Button
      >
      <Button
        onclick={() => {
          info.showEdit = !info.showEdit
        }}>{info.showEdit ? "Disable" : "Enable"} Edit</Button
      >
    </div>
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
    right: 4px;
    transform: translateY(-50%);
    z-index: 100;
    transform: rotate(90deg);

    transition: right var(--d2);
  }
  button.arrow.hidden {
    right: -16px;
  }
  nav {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    padding-block: 1rem;
    padding-inline: 1rem;
    background-color: var(--card);
    border-radius: 0 16px 16px 0;
    height: 100%;
    transition:
      background-color var(--d3),
      padding-inline var(--d2);
    overflow: hidden;
  }
  nav.hidden {
    padding-inline: 0;
  }
  .item {
    display: flex;
    gap: 1rem;
    flex-wrap: wrap;
    max-width: 200px;
  }
  .shell {
    max-width: 400px;
    position: relative;

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
    text-wrap: nowrap;
  }
</style>
