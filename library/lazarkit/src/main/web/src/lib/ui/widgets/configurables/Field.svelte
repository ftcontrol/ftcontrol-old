<script lang="ts">
  import { info, socket } from "$lib"
  import { Types, type GenericTypeJson } from "$lib/genericType"
  import { SelectInput, StringInput } from "$primitives"
  import Update from "$ui/icons/Update.svelte"
  import { anyValidator } from "../../primitives/validators"
  import FieldNested from "./FieldNested.svelte"
  import Hiddable from "./Hiddable.svelte"
  import Toggle from "./Toggle.svelte"

  let {
    item,
    depth = 0,
  }: {
    item: GenericTypeJson
    depth?: number
  } = $props()

  function sendFieldUpdate() {
    if (!item.isValid) return
    socket.sendMessage({
      kind: "updatedJvmFields",
      fields: [
        {
          id: item.id,
          newValueString: item.newValueString,
        },
      ],
    })
    item.valueString = item.newValueString
    item.value = item.newValueString
  }

  function processName(name: string): string {
    const searchParam = info.searchParam?.toLowerCase()
    if (!searchParam) return name

    const index = name.toLowerCase().indexOf(searchParam)
    if (index === -1) return name

    const before = name.slice(0, index)
    const match = name.slice(index, index + searchParam.length)
    const after = name.slice(index + searchParam.length)

    return `<span>${before}<span class="highlight">${match}</span>${after}</span>`
  }
</script>

{#if item.isShown}
  <div
    class:item
    class:disabled={item.type == Types.UNKNOWN}
    class:first={depth == 0}
    style="--depth:{depth};"
  >
    {#if [Types.INT, Types.LONG, Types.DOUBLE, Types.FLOAT, Types.STRING].includes(item.type)}
      <p style="margin-left: -24px;">
        <button
          onclick={() => {
            sendFieldUpdate()
          }}
        >
          <Update
            isActive={item.valueString != item.newValueString && item.isValid}
          />
        </button>
        {@html processName(item.fieldName)}
      </p>

      <form
        onsubmit={() => {
          sendFieldUpdate()
        }}
      >
        <StringInput
          bind:value={item.value}
          bind:isValid={item.isValid}
          bind:startValue={item.valueString}
          bind:currentValue={item.newValueString}
          type={item.type.toUpperCase()}
          validate={anyValidator(item.type)}
        />
      </form>
    {:else if item.type === Types.ENUM || item.type === Types.BOOLEAN}
      <p style="margin-left: -24px;">
        <button
          onclick={() => {
            sendFieldUpdate()
          }}
        >
          <Update
            isActive={item.valueString != item.newValueString && item.isValid}
          />
        </button>
        {@html processName(item.fieldName)}
      </p>
      <SelectInput
        bind:value={item.value}
        bind:isValid={item.isValid}
        bind:startValue={item.valueString}
        bind:currentValue={item.newValueString}
        possibleValues={item.possibleValues}
        type={item.type.toUpperCase()}
      />
    {:else if item.type == Types.CUSTOM || item.type == Types.ARRAY || item.type == Types.MAP || item.type == Types.LIST}
      <div class="two" style="margin-left: -20px;">
        <Toggle bind:isOpened={item.isOpened}>
          <p>{@html processName(item.fieldName)} {item.type}</p>
        </Toggle>
      </div>
      <div class="two">
        {#each item.customValues as custom}
          <Hiddable isShown={item.isOpened}>
            <FieldNested item={custom} depth={depth + 1} />
          </Hiddable>
        {/each}
      </div>
    {:else}
      <div class="two">
        <p>{@html processName(item.fieldName)} {item.type}</p>
      </div>
    {/if}
  </div>
{/if}

<style>
  .item {
    --margin: 48px;
    margin-left: calc(var(--margin));
    display: grid;
    grid-template-columns: 1fr 1fr;
    align-items: center;
    margin-bottom: 8px;
  }
  p {
    margin: 0;
    display: flex;
    align-items: center;
    gap: 0.2rem;
  }
  .two {
    grid-column-start: span 2;
  }
  button {
    all: unset;
    cursor: pointer;
    margin-top: 6px;
  }
  .item.disabled {
    opacity: 0.5;
  }

  :global(span.highlight) {
    color: var(--primary);
    font-weight: bold;
  }
</style>
