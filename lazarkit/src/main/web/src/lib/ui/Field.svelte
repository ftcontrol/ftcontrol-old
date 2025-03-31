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

  let {
    item,
    parentItem,
    depth = 0,
  }: {
    item: GenericTypeJson
    parentItem: GenericTypeJson
    depth?: number
  } = $props()

  function getUpdatedValues(field) {
    var outputList = []
    if (field.type != Types.CUSTOM) outputList
    for (const customValue of field.customValues) {
      if (customValue.type != Types.CUSTOM) {
        if (
          customValue.valueString != customValue.newValueString &&
          customValue.isValid
        ) {
          outputList.push({
            id: customValue.id,
            className: customValue.className,
            fieldName: customValue.fieldName,
            type: customValue.type,
            valueString: customValue.newValueString,
          })
        }
      } else {
        var processed = getUpdatedValues(customValue)
        if (processed.customValues.length) outputList.push(processed)
      }
    }
    return {
      id: field.id,
      className: field.className,
      fieldName: field.fieldName,
      type: field.type,
      valueString: field.newValueString,
      customValues: outputList,
    }
  }

  function sendFieldUpdate(item: GenericTypeJson) {
    if (!item.isValid) return
    var output = {
      className: item.className,
      fieldName: item.fieldName,
      type: item.type,
      valueString: item.newValueString,
    }
    if (parentItem.type == Types.CUSTOM) {
      var processed = getUpdatedValues(parentItem)
      if (processed.customValues.length) {
        output = processed
        // alert(JSON.stringify(output))
        function process(data) {
          var out = []
          for (const i of data.customValues) {
            if (i.type == Types.CUSTOM) {
              var newItem = process(i)
              out.push(newItem)
            } else {
              console.log(i)
              console.log("comparing", i.id, item.id)
              if (i.id == item.id) {
                out.push(i)
              }
            }
          }
          data.customValues = out
          return data
        }
        // alert(JSON.stringify(process(output)))
      }
      // return
    }
    socket.sendMessage({
      kind: "jvmFields",
      fields: [output],
    })
    item.valueString = item.newValueString
  }
</script>

<div
  class="item"
  class:disabled={item.type == Types.UNKNOWN}
  style="--depth:{depth};"
>
  <p>{item.id} {item.fieldName} {item.type} {item.isValid}</p>
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
    {JSON.stringify(getUpdatedValues(item).customValues)}
    {#each item.customValues as custom}
      <FieldNested item={custom} parentItem={item} depth={depth + 1} />
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
