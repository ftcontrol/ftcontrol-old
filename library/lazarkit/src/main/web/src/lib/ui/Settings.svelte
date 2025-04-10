<script lang="ts">
  import { info } from "$lib"
  import { settings } from "$lib/settings.svelte"
  import Arrow from "./icons/Arrow.svelte"
  import Header from "./primitives/Header.svelte"
  import Section from "./primitives/Section.svelte"
  import SelectInput from "./primitives/SelectInput.svelte"

  let animationSpeed = $state(settings.animationSpeed)

  $effect(() => {
    settings.setSpeed(animationSpeed)
  })
</script>

<button
  class="overlay"
  class:shown={info.showSettings}
  onclick={() => {
    info.showSettings = false
  }}
  aria-label="Close Settings"
></button>

<section class:shown={info.showSettings}>
  <Section hasMargin={false} maxHeight={true}>
    <Header>
      <button
        onclick={() => {
          info.showSettings = false
        }}
      >
        <Arrow isOpened={false} />
      </button>
      <h2>Settings</h2>
      <div></div>
    </Header>

    <h3>General</h3>

    <div class="flex">
      <p>Animation speed</p>
      <SelectInput
        startValue={settings.animationSpeed}
        bind:currentValue={animationSpeed}
        value="normal"
        possibleValues={["instant", "fast", "normal", "slow"]}
        isValid={true}
        alwaysValid={true}
      ></SelectInput>
    </div>
  </Section>
</section>

<style>
  .flex {
    display: flex;
    gap: 1rem;
    align-items: center;
  }
  section {
    --margin: 2rem;
    position: fixed;
    top: var(--margin);
    right: var(--margin);
    width: calc(100vw - 2 * var(--margin));
    height: calc(100vh - 2 * var(--margin));
    z-index: 101;
    transform: translateY(-125%);
    transition: transform var(--d3);
  }
  section.shown {
    transform: translateY(0%);
  }
  button {
    all: unset;
    cursor: pointer;
    font-size: 2rem;
    transform: rotate(90deg);
  }
  .overlay {
    position: fixed;
    top: 0;
    right: 0;
    width: 100vw;
    height: 100vh;
    background-color: black;
    z-index: 100;
    opacity: 0;
    transition: opacity var(--d3);
    pointer-events: none;
  }
  .overlay.shown {
    opacity: 0.5;
    pointer-events: all;
  }
</style>
