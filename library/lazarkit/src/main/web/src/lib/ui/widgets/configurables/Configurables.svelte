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
        field.type == Types.MAP
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
        field.type == Types.MAP
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
</script>

<Section>
  <Header>
    <Title>Configurables</Title>
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
