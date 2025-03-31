<script lang="ts">
  import { socket } from "$lib"
  import { Types, type GenericTypeJson } from "$lib/genericType"
  import BooleanInput from "../../primitives/BooleanInput.svelte"
  import SelectInput from "../../primitives/SelectInput.svelte"
  import StringInput from "../../primitives/StringInput.svelte"
  import {
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
  <p>{item.fieldName} {item.type}</p>
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
    <form
      onsubmit={() => {
        sendFieldUpdate()
      }}
    >
      {#if item.type == Types.INT}
        <StringInput
          bind:value={item.value}
          bind:isValid={item.isValid}
          bind:startValue={item.valueString}
          bind:currentValue={item.newValueString}
          validate={intValidator}
        />
      {:else if item.type == Types.LONG}
        <StringInput
          bind:value={item.value}
          bind:isValid={item.isValid}
          bind:startValue={item.valueString}
          bind:currentValue={item.newValueString}
          validate={longValidator}
          extraChar={"L"}
        />
      {:else if item.type == Types.DOUBLE}
        <StringInput
          bind:value={item.value}
          bind:isValid={item.isValid}
          bind:startValue={item.valueString}
          bind:currentValue={item.newValueString}
          validate={doubleValidator}
        />
      {:else if item.type == Types.FLOAT}
        <StringInput
          bind:value={item.value}
          bind:isValid={item.isValid}
          bind:startValue={item.valueString}
          bind:currentValue={item.newValueString}
          validate={floatValidator}
          extraChar={"f"}
        />
      {:else if item.type == Types.STRING}
        <StringInput
          bind:value={item.value}
          bind:isValid={item.isValid}
          bind:startValue={item.valueString}
          bind:currentValue={item.newValueString}
          validate={stringValidator}
        />
      {/if}
    </form>
  {:else if item.type == Types.BOOLEAN}
    <BooleanInput
      bind:value={item.value}
      bind:isValid={item.isValid}
      bind:startValue={item.valueString}
      bind:currentValue={item.newValueString}
    />
  {:else if item.type == Types.ENUM}
    <SelectInput
      bind:value={item.value}
      bind:isValid={item.isValid}
      bind:startValue={item.valueString}
      bind:currentValue={item.newValueString}
      possibleValues={item.possibleValues}
    />
  {:else if item.type == Types.CUSTOM}
    {#each item.customValues as custom}
      <FieldNested item={custom} depth={depth + 1} />
    {/each}
  {:else if item.type == Types.ARRAY}
    {#each item.arrayValues as custom}
      <FieldNested item={custom} depth={depth + 1} />
    {/each}
  {:else}
    {JSON.stringify(item)}
  {/if}
</div>

<style>
  .item {
    margin-left: calc(var(--depth) * 32px);
  }
  p {
    margin: 0;
  }
  .item.first {
    padding: 8px;
    border-bottom: 1px solid black;
  }
  .item.disabled {
    opacity: 0.5;
  }
</style>
