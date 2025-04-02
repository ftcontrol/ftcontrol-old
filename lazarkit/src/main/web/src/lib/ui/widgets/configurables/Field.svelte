<script lang="ts">
  import { socket } from "$lib"
  import { Types, type GenericTypeJson } from "$lib/genericType"
  import { BooleanInput, SelectInput, StringInput } from "$primitives"
  import {
    anyValidator,
    doubleValidator,
    floatValidator,
    intValidator,
    longValidator,
    stringValidator,
  } from "../../primitives/validators"
  import FieldNested from "./FieldNested.svelte"

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
</script>

<div
  class:item
  class:disabled={item.type == Types.UNKNOWN}
  class:first={depth == 0}
  style="--depth:{depth};"
>
  {#if item.valueString != item.newValueString && item.isValid}
    <button
      onclick={() => {
        sendFieldUpdate()
      }}
    >
      Update
    </button>
  {/if}

  {#if [Types.INT, Types.LONG, Types.DOUBLE, Types.FLOAT, Types.STRING].includes(item.type)}
    <div class="flex">
      <p>{item.fieldName}</p>

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
    </div>
  {:else if item.type === Types.ENUM || item.type === Types.BOOLEAN}
    <div class="flex">
      <p>{item.fieldName}</p>
      <SelectInput
        bind:value={item.value}
        bind:isValid={item.isValid}
        bind:startValue={item.valueString}
        bind:currentValue={item.newValueString}
        possibleValues={item.possibleValues}
        type={item.type.toUpperCase()}
      />
    </div>
  {:else if item.type == Types.CUSTOM}
    <p>{item.fieldName} {item.type}</p>

    {#each item.customValues as custom}
      <FieldNested item={custom} depth={depth + 1} />
    {/each}
  {:else if item.type == Types.ARRAY}
    <p>{item.fieldName} {item.type}</p>

    {#each item.arrayValues as custom}
      <FieldNested item={custom} depth={depth + 1} />
    {/each}
  {:else if item.type == Types.MAP}
    <p>{item.fieldName} {item.type}</p>

    {#each item.mapValues as mapCustom}
      <FieldNested item={mapCustom} depth={depth + 1} />
    {/each}
  {:else if item.type != Types.UNKNOWN}
    {JSON.stringify(item)}
  {/if}
</div>

<style>
  .item {
    margin-left: calc((var(--depth) + 1) * 16px);
  }
  p {
    margin: 0;
  }
  .item.first {
    /* padding: 8px;
    border-bottom: 1px solid black; */
  }
  .item.disabled {
    opacity: 0.5;
  }
  .flex {
    display: flex;
    gap: 0.25rem;
    align-items: center;
  }
  .flex > p {
    margin-top: 7px;
  }
</style>
