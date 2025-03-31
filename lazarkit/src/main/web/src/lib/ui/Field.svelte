<script lang="ts">
  import { socket } from "$lib"
  import { Types, type GenericTypeJson } from "$lib/genericType"
  import FieldNested from "./FieldNested.svelte"
  import BooleanInput from "./primitives/BooleanInput.svelte"
  import SelectInput from "./primitives/SelectInput.svelte"
  import StringInput from "./primitives/StringInput.svelte"
  import {
    doubleValidator,
    floatValidator,
    intValidator,
    longValidator,
    stringValidator,
  } from "./primitives/validators"

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
  class="item"
  class:disabled={item.type == Types.UNKNOWN}
  style="--depth:{depth};"
>
  <p>{item.id} {item.fieldName} {item.type}</p>
  <p>VS: {item.valueString} NVS:{item.newValueString} IV{item.isValid}</p>
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
  {:else}
    {JSON.stringify(item)}
  {/if}
</div>

<!--
  {#if item.type == "ARRAY"}
    <p>{JSON.stringify(item.arrayValues)}</p>
  {/if} -->

<style>
  .item {
    border-bottom: 1px solid black;
    margin-left: calc(var(--depth) * 32px);
  }
  .item.disabled {
    opacity: 0.5;
  }
</style>
