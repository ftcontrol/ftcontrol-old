<script lang="ts">
  import { info, socket } from "$lib"
  import { type ChangeJson, type GenericTypeJson } from "$lib/genericType"
  import { ConfigurablesStates } from "$lib/socket.svelte"
  import Diff from "$ui/icons/Diff.svelte"
  import UpdateAll from "$ui/icons/UpdateAll.svelte"
  import Content from "$ui/primitives/Content.svelte"
  import Header from "$ui/primitives/Header.svelte"
  import Title from "$ui/primitives/Title.svelte"
  import ClassName from "./ClassName.svelte"
  import Field from "./Field.svelte"
  import Hiddable from "./Hiddable.svelte"
  import { handleDiff, handleSearch, hasDiff } from "./search.svelte"
  import { forAllRecursive } from "./utils"

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

<Header>
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
  <div>
    <button
      onclick={() => {
        sendAllUpdates(info.jvmFields)
      }}
    >
      <UpdateAll isActive={isChanged(info.jvmFields)} />
    </button>
    <button
      onclick={() => {
        handleDiff()
      }}
    >
      <Diff
        isActive={hasDiff(info.jvmFields)}
        isSelected={info.configurablesState == ConfigurablesStates.DIFF}
      />
    </button>
  </div>
</Header>
<Content>
  <div class="content">
    {#each Object.entries(processFields(info.jvmFields)) as [name, items]}
      <Hiddable
        isShown={info.openedStates[name] ||
          info.configurablesState == ConfigurablesStates.NORMAL}
      >
        <ClassName {name} bind:isOpened={info.openedStates[name]} />
        <Hiddable isShown={info.openedStates[name] == true}>
          {#each items as item}
            <Field {item} />
          {/each}
        </Hiddable>
      </Hiddable>
    {/each}
    {#if info.jvmFields.length == 0}
      <p>No configurables found.</p>
    {/if}
  </div>
</Content>

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
    margin-right: -1rem;
    padding-right: 1rem;
  }
</style>
