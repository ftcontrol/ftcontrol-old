<script lang="ts">
  import { info, notifications, socket } from "$lib"
  import { settings } from "$lib/settings.svelte"
  import { Logo, Arrow } from "./icons"
  import Button from "./primitives/Button.svelte"
  let isOpened = $state(true)

  import { tick } from "svelte"
  import StringInput from "./primitives/StringInput.svelte"
  import { stringValidator } from "./primitives/validators"
  import { defaultModuled, Grid } from "./grid/grid.svelte"
  import Remove from "./icons/Remove.svelte"
  let isCovering = $state(false)

  let newNameValue = $state("")

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

      <h2>My Panels</h2>

      {#each settings.allIDs as id}
        {@const grid = settings.getGridById(id)}
        {#if grid != null}
          {#if info.showEdit}
            <div class="item">
              <button
                class="icon"
                onclick={() => {
                  settings.removePreset(id)
                }}
              >
                <Remove />
              </button>
              <form
                class="item"
                onsubmit={() => {
                  settings.savePresets()
                  info.showEdit = false
                }}
              >
                <StringInput
                  value={grid.name}
                  isValid={true}
                  startValue={grid.name}
                  bind:currentValue={grid.name}
                  type={""}
                  validate={stringValidator}
                  alwaysValid={true}
                />
              </form>
              {#if settings.selectedManagerID != id}
                <a
                  href="/?preset={id}"
                  onclick={() => {
                    settings.selectedManagerID = id
                  }}>Go</a
                >
              {/if}
            </div>
          {:else}
            <a
              href="/?preset={id}"
              onclick={() => {
                settings.selectedManagerID = id
              }}>{grid?.name}</a
            >
          {/if}
        {/if}
      {/each}

      {#if info.showEdit}
        <form
          class="item"
          onsubmit={() => {
            if (newNameValue == "") {
              notifications.add("Cannot have an empty name.")
              return
            }
            var newPreset = structuredClone(defaultModuled())
            newPreset.name = newNameValue
            settings.gridManagers.push(new Grid(newPreset))
            newNameValue = ""
          }}
        >
          <StringInput
            value={newNameValue}
            isValid={true}
            startValue={newNameValue}
            bind:currentValue={newNameValue}
            type={""}
            validate={stringValidator}
            alwaysValid={true}
          />
          <input type="submit" value="Submit" disabled={newNameValue == ""} />
        </form>
      {/if}

      <h2>Other Panels</h2>

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
          if (settings.isGridEdited) {
            settings.savePresets()
          }
        }}
      >
        {#if info.showEdit}
          {#if settings.isGridEdited}
            Save changes
          {:else}
            Disable Edit
          {/if}
        {:else}
          Enable Edit
        {/if}
      </Button>
    </div>
  </nav>
</section>

<style>
  input[type="submit"] {
    all: unset;
    cursor: pointer;
    transition: opacity var(--d3);
  }
  input[type="submit"]:disabled {
    opacity: 0.5;
  }
  button.icon {
    all: unset;
    cursor: pointer;
  }
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
