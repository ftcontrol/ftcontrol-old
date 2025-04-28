<script lang="ts">
  import { info } from "$lib"
  import { settings } from "$lib/settings.svelte"
  import Arrow from "./icons/Arrow.svelte"
  import Button from "./primitives/Button.svelte"
  import Content from "./primitives/Content.svelte"
  import Header from "./primitives/Header.svelte"
  import Section from "./primitives/Section.svelte"
  import SelectInput from "./primitives/SelectInput.svelte"

  let animationSpeed = $state(settings.animationSpeed)
  let primaryColor = $state(settings.primaryColor)

  $effect(() => {
    settings.setSpeed(animationSpeed)
  })

  $effect(() => {
    settings.setPrimaryColor(primaryColor)
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
  <Section maxHeight={true}>
    <Header>
      <button
        class="arrow"
        onclick={() => {
          info.showSettings = false
        }}
      >
        <Arrow isOpened={false} />
      </button>
      <h2>Settings</h2>
      <div></div>
    </Header>

    <Content>
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

      <div class="flex">
        <p>Primary Color</p>
        <SelectInput
          startValue={settings.primaryColor}
          bind:currentValue={primaryColor}
          value="blue"
          possibleValues={["red", "blue"]}
          isValid={true}
          alwaysValid={true}
        ></SelectInput>
      </div>

      <div class="flex">
        <p>Reset Presets</p>
        <Button
          disabled={!settings.hasPresets}
          onclick={() => {
            settings.resetPresets()
          }}>Reset all presets</Button
        >
      </div>

      <h3>Plugins</h3>
      <p>
        Found {info.plugins.length} plugin{info.plugins.length > 1 ? "s" : ""}.
      </p>
      {#each info.plugins as plugin}
        <div class="flex">
          <h4>{plugin.name}</h4>
          <p>
            {plugin.id}
          </p>
        </div>
      {/each}
    </Content>
  </Section>
</section>

<style>
  .flex {
    display: flex;
    gap: 1rem;
    align-items: center;
    justify-content: space-between;
  }
  section {
    --margin: 2rem;
    position: fixed;
    top: var(--margin);
    right: var(--margin);
    max-width: 600px;
    width: calc(100vw - 2 * var(--margin));
    height: calc(100vh - 2 * var(--margin));
    z-index: 101;
    transform: translateX(125%);

    background-color: var(--card);
    border-radius: 16px;
    border: 2px solid var(--bg);

    transition:
      transform var(--d3),
      background-color var(--d3);
  }
  section.shown {
    transform: translateX(0%);
  }
  button {
    all: unset;
    cursor: pointer;
    font-size: 2rem;
  }
  .arrow {
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
