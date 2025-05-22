<script lang="ts">
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

{#each settings.allIDs as id}
  {@const grid = settings.getPresetById(id)}
  {#if grid == null}
    <p>Not found</p>
  {:else}
    <div class="widget" data-id={id}>
      <a
        href="/?preset={id}"
        oncontextmenu={(event: MouseEvent) => {
          event.preventDefault()
          modular.context.openContextMenu(id, 0)
        }}
        onclick={() => {
          settings.selectedManagerID = id
        }}>{grid?.name}</a
      >
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

<style>
  div {
    position: relative;
  }
</style>
