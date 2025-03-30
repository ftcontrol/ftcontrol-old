<script lang="ts">
  import { socket } from "$lib"
  import {
    Types,
    type CustomTypeJson,
    type GenericTypeJson,
  } from "$lib/genericType"
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

  let { item, depth = 0 }: { item: GenericTypeJson; depth?: number } = $props()

  function getNesting(input: CustomTypeJson, level = 0) {
    var components: {
      item: GenericTypeJson
      level: number
    }[] = []

    for (const item of input.customValues) {
      if (item.type === "CUSTOM") {
        components.push(...getNesting(item, level + 1))
      } else {
        components.push({ item, level })
      }
    }

    return components
  }

  function sendFieldUpdate(item: GenericTypeJson) {
    if (!item.isValid) return
    socket.sendMessage({
      kind: "jvmFields",
      fields: [
        {
          className: item.className,
          fieldName: item.fieldName,
          type: item.type,
          valueString: item.newValueString,
        },
      ],
    })
    item.valueString = item.newValueString
  }
</script>

<div
  class="item"
  class:disabled={item.type == Types.UNKNOWN}
  style="--depth:{depth};"
>
  <p>{item.fieldName} {item.type} {item.isValid}</p>
  {#if item.valueString != item.newValueString && item.isValid}
    <button
      onclick={() => {
        if (!item.isValid) return
        sendFieldUpdate(item)
      }}
    >
      Update
    </button>
  {/if}
  {#if [Types.INT, Types.LONG, Types.DOUBLE, Types.FLOAT, Types.STRING].includes(item.type)}
    <form
      onsubmit={() => {
        sendFieldUpdate(item)
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
