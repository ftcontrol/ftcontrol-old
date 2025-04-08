<script lang="ts">
  import { info, socket } from "$lib"
  import {
    Types,
    type ChangeJson,
    type GenericTypeJson,
  } from "$lib/genericType"
  import { Section } from "$primitives"
  import UpdateAll from "$ui/icons/UpdateAll.svelte"
  import Header from "$ui/primitives/Header.svelte"
  import Title from "$ui/primitives/Title.svelte"
  import ClassName from "./ClassName.svelte"
  import Field from "./Field.svelte"
  import Hiddable from "./Hiddable.svelte"
  let openedStates: { [key: string]: boolean } = $state({})

  function processFields(fields: GenericTypeJson[]): {
    [key: string]: GenericTypeJson[]
  } {
    const result: {
      [key: string]: GenericTypeJson[]
    } = {}

    for (const field of fields) {
      if (!result.hasOwnProperty(field.className)) {
        result[field.className] = []
      }
      result[field.className].push(field)
    }

    return result
  }

  function isChanged(fields: GenericTypeJson[]): boolean {
    if (fields == null) return false
    for (const field of fields) {
      if (field.valueString != field.newValueString && field.isValid) {
        return true
      }
      if (
        field.type == Types.CUSTOM ||
        field.type == Types.ARRAY ||
        field.type == Types.MAP ||
        field.type == Types.LIST
      ) {
        var innerIsChanged = isChanged(field.customValues)
        if (innerIsChanged) return true
      }
    }
    return false
  }

  function getAllValues(fields: GenericTypeJson[]): ChangeJson[] {
    var values: ChangeJson[] = []
    for (const field of fields) {
      if (
        field.type == Types.CUSTOM ||
        field.type == Types.ARRAY ||
        field.type == Types.MAP ||
        field.type == Types.LIST
      ) {
        var nested = getAllValues(field.customValues)
        if (nested.length) values = [...values, ...nested]
      } else {
        if (field.valueString != field.newValueString && field.isValid) {
          values.push({
            id: field.id,
            newValueString: field.newValueString,
          })
        }
      }
    }
    return values
  }

  function sendAllUpdates(fields: GenericTypeJson[]) {
    socket.sendMessage({
      kind: "updatedJvmFields",
      fields: getAllValues(fields),
    })
  }

  function search(p: string, fields: GenericTypeJson[]) {
    if (!fields) return

    p = p.toLowerCase()
    const newOpenedStates: { [key: string]: boolean } = {}

    function recursiveSearch(fields: GenericTypeJson[]): boolean {
      let foundInGroup = false

      for (const field of fields) {
        const matchesField =
          field.fieldName?.toLowerCase().includes(p) ||
          field.className?.toLowerCase().includes(p)

        let foundInNested = false
        if (
          (field.type === Types.CUSTOM ||
            field.type === Types.ARRAY ||
            field.type === Types.MAP ||
            field.type === Types.LIST) &&
          Array.isArray(field.customValues)
        ) {
          field.isOpened = false
          foundInNested = recursiveSearch(field.customValues)
          if (foundInNested) field.isOpened = true
        }

        field.isShown = matchesField || foundInNested

        if (field.isShown) {
          foundInGroup = true
          newOpenedStates[field.className] = true
        } else if (!(field.className in newOpenedStates)) {
          newOpenedStates[field.className] = false
        }
      }

      return foundInGroup
    }

    recursiveSearch(fields)
    openedStates = newOpenedStates
  }

  let isSearching = $state(false)
  let savedStates: {
    openedStates: { [key: string]: boolean }
    isShownMap: Map<string, boolean>
  } = {
    openedStates: {},
    isShownMap: new Map(),
  }
</script>

<Section>
  <Header>
    <Title>Configurables</Title>
    <button
      onclick={() => {
        if (!isSearching) {
          // Save previous state
          savedStates.openedStates = { ...openedStates }
          savedStates.isShownMap = new Map()
          function saveStates(fields: GenericTypeJson[]) {
            for (const field of fields) {
              savedStates.isShownMap.set(field.id, field.isShown ?? true)
              if (
                (field.type === Types.CUSTOM ||
                  field.type === Types.ARRAY ||
                  field.type === Types.MAP ||
                  field.type === Types.LIST) &&
                Array.isArray(field.customValues)
              ) {
                saveStates(field.customValues)
              }
            }
          }
          saveStates(info.jvmFields)

          // Run search
          search("Int", info.jvmFields)
        } else {
          // Restore previous states
          openedStates = { ...savedStates.openedStates }
          function restoreStates(fields: GenericTypeJson[]) {
            for (const field of fields) {
              field.isShown = savedStates.isShownMap.get(field.id) ?? true
              if (
                (field.type === Types.CUSTOM ||
                  field.type === Types.ARRAY ||
                  field.type === Types.MAP ||
                  field.type === Types.LIST) &&
                Array.isArray(field.customValues)
              ) {
                restoreStates(field.customValues)
              }
            }
          }
          restoreStates(info.jvmFields)
        }

        isSearching = !isSearching
      }}
    >
      <span>{isSearching ? "Reset" : "Search Int"}</span>
    </button>

    <button
      onclick={() => {
        sendAllUpdates(info.jvmFields)
      }}
    >
      <UpdateAll isActive={isChanged(info.jvmFields)} />
    </button>
  </Header>
  <div class="content">
    {#each Object.entries(processFields(info.jvmFields)) as [name, items]}
      <div>
        <ClassName {name} bind:isOpened={openedStates[name]} />
        <Hiddable isShown={openedStates[name] == true}>
          {#each items as item}
            <Field {item} />
          {/each}
        </Hiddable>
      </div>
    {/each}
    {#if info.jvmFields.length == 0}
      <p>No configurables found.</p>
    {/if}
  </div>
</Section>

<style>
  button {
    all: unset;
    cursor: pointer;
  }
  .content {
    height: 500px;
    overflow-y: auto;
    margin-right: -1rem;
    padding-right: 1rem;
  }
</style>
