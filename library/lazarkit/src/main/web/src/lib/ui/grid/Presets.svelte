<script lang="ts">
  import { goto } from "$app/navigation"
  import { info } from "$lib"
  import { settings } from "$lib/settings.svelte"
  import Arrow from "$ui/icons/Arrow.svelte"
  import Remove from "$ui/icons/Remove.svelte"
  import StringInput from "$ui/primitives/StringInput.svelte"
  import { stringValidator } from "$ui/primitives/validators"
  import ContextMenu from "./ContextMenu.svelte"
  import { modular } from "./logic/modular"
  import { PresetManager } from "./logic/preset.svelte"
  import { defaultModuled } from "./logic/types"
</script>

{#each settings.allIDs as id, index}
  {@const grid = settings.getPresetById(id)}
  {#if modular.sidebarTabs.showExtra("", index)}
    <div class="extra" data-id={""} data-index={index}>
      {index}
    </div>
  {/if}
  {#if modular.sidebarTabs.showLabel("", index)}
    {#if grid == null}
      <p>Not found</p>
    {:else}
      <div class="widget" data-id={id}>
        <button
          oncontextmenu={(event: MouseEvent) => {
            event.preventDefault()
            modular.context.openContextMenu(id, 0)
          }}
          onclick={() => {
            goto(`/?preset=${id}`)
            settings.selectedManagerID = id
          }}
          onmousedown={(event: MouseEvent) => {
            modular.sidebarTabs.startMoving(
              event.clientX,
              event.clientY,
              index,
              ""
            )
          }}
        >
          {grid?.name}
        </button>
        {#if modular.context.isContextOpened(id, 0)}
          <ContextMenu {id}>
            <button
              class="icon"
              onclick={() => {
                settings.removePreset(id)
              }}
            >
              <Remove /> Remove Preset
            </button>
            <StringInput
              value={grid.name}
              isValid={true}
              startValue={grid.name}
              bind:currentValue={grid.name}
              type={""}
              validate={stringValidator}
              alwaysValid={true}
            />
            <button
              onclick={() => {
                var newPreset = structuredClone(defaultModuled())
                newPreset.name = "New Preset"
                const index = settings.presets.findIndex((p) => p.id === id)
                if (index !== -1) {
                  settings.presets.splice(
                    index + 1,
                    0,
                    new PresetManager(newPreset)
                  )
                } else {
                  settings.presets.push(new PresetManager(newPreset))
                }
                modular.context.closeContextMenu()
              }}
            >
              New Preset
            </button>
          </ContextMenu>
        {/if}
      </div>
    {/if}
  {/if}
  <!-- <Button
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
      </Button> -->
{/each}
{#if modular.sidebarTabs.showExtra("", settings.allIDs.length)}
  <div class="extra" data-id={""} data-index={settings.allIDs.length}></div>
{/if}

<style>
  div {
    position: relative;
  }
  .extra {
    background-color: red;
    width: 100%;
    height: 16px;
  }
</style>
