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
  import { computeDiff, handleSearch } from "./search.svelte"
  import { forAll, forAllRecursive } from "./utils"

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
    return forAllRecursive(
      fields,
      (field) => field.valueString !== field.newValueString && field.isValid,
      (_, childrenResults) => childrenResults.some(Boolean)
    ).some(Boolean)
  }

  function getAllValues(fields: GenericTypeJson[]): ChangeJson[] {
    const rawResults = forAllRecursive(
      fields,
      (field) => {
        if (field.valueString !== field.newValueString && field.isValid) {
          return {
            id: field.id,
            newValueString: field.newValueString,
          }
        }
        return null
      },
      (_field, childResults) => {
        return null
      }
    )

    return rawResults.filter((v): v is ChangeJson => v !== null)
  }

  function sendAllUpdates(fields: GenericTypeJson[]) {
    if (fields == null) return
    socket.sendMessage({
      kind: "updatedJvmFields",
      fields: getAllValues(fields),
    })
  }
</script>

<Section>
  <Header>
    <Title>Configurables</Title>
    <input
      class="search"
      type="text"
      placeholder="Search"
      oninput={(e) => {
        const target = e.target as HTMLInputElement | null
        if (!target) return
        handleSearch(target.value)
      }}
    />
    <button
      onclick={() => {
        sendAllUpdates(info.jvmFields)
      }}
    >
      <UpdateAll isActive={isChanged(info.jvmFields)} />
    </button>
    <button
      onclick={() => {
        computeDiff(info.jvmFields)
      }}>Compute Diff</button
    >
  </Header>
  <div class="content">
    {#each Object.entries(processFields(info.jvmFields)) as [name, items]}
      <div>
        <ClassName {name} bind:isOpened={info.openedStates[name]} />
        <Hiddable isShown={info.openedStates[name] == true}>
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
  .search {
    all: unset;
    color: inherit;
    border: 2px solid var(--bg);
    padding: 0.15em 0.3em;
  }
  button {
    all: unset;
    cursor: pointer;
    margin-top: 8px;
  }
  .content {
    height: 500px;
    overflow-y: auto;
    margin-right: -1rem;
    padding-right: 1rem;
  }
</style>
