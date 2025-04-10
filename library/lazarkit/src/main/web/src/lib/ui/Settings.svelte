<script lang="ts">
  import { info } from "$lib"
  import Header from "./primitives/Header.svelte"
  import Section from "./primitives/Section.svelte"
  import SelectInput from "./primitives/SelectInput.svelte"
</script>

<button
  class="overlay"
  onclick={() => {
    info.showSettings = false
  }}
  aria-label="Close Settings"
></button>

<section>
  <Section hasMargin={false} maxHeight={true}>
    <Header>
      <button
        onclick={() => {
          info.showSettings = false
        }}
      >
        {"<"}
      </button>
      <h2>Settings</h2>
      <div></div>
    </Header>

    <h3>General</h3>

    <div class="flex">
      <p>Animation speed</p>
      <SelectInput
        startValue="normal"
        currentValue="normal"
        value="normal"
        possibleValues={["fast", "normal", "slow"]}
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
  }
  button {
    all: unset;
    cursor: pointer;
    font-size: 2rem;
  }
  .overlay {
    position: fixed;
    top: 0;
    right: 0;
    width: 100vw;
    height: 100vh;
    background-color: black;
    opacity: 0.5;
    z-index: 100;
  }
</style>
