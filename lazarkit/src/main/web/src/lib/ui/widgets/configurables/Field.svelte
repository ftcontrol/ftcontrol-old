<script lang="ts">
  import { socket } from "$lib"
  import { Types, type GenericTypeJson } from "$lib/genericType"
  import { SelectInput, StringInput } from "$primitives"
  import Update from "$ui/icons/Update.svelte"
  import { anyValidator } from "../../primitives/validators"
  import FieldNested from "./FieldNested.svelte"
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
</script>

<div
  class:item
  class:disabled={item.type == Types.UNKNOWN}
  class:first={depth == 0}
  style="--depth:{depth};"
>
  {#if [Types.INT, Types.LONG, Types.DOUBLE, Types.FLOAT, Types.STRING].includes(item.type)}
    <p>
      <button
        onclick={() => {
          sendFieldUpdate()
        }}
      >
        <Update
          isActive={item.valueString != item.newValueString && item.isValid}
        />
      </button>
      {item.fieldName}
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
    <p>
      <button
        onclick={() => {
          sendFieldUpdate()
        }}
      >
        <Update
          isActive={item.valueString != item.newValueString && item.isValid}
        />
      </button>
      {item.fieldName}
    </p>
    <SelectInput
      bind:value={item.value}
      bind:isValid={item.isValid}
      bind:startValue={item.valueString}
      bind:currentValue={item.newValueString}
      possibleValues={item.possibleValues}
      type={item.type.toUpperCase()}
    />
  {:else if item.type == Types.CUSTOM || item.type == Types.ARRAY || item.type == Types.MAP}
    <div class="two" style="margin-left: -20px;">
      <Toggle bind:isOpened={item.isOpened}>
        <p>{item.fieldName} {item.type}</p>
      </Toggle>
    </div>
    {#if item.isOpened}
      {#each item.customValues as custom}
        <div class="two">
          <FieldNested item={custom} depth={depth + 1} />
        </div>
      {/each}
    {/if}
  {:else if item.type != Types.UNKNOWN}
    {JSON.stringify(item)}
  {/if}
</div>

<style>
  .item {
    --margin: calc((var(--depth)) * 0px);
    margin-left: calc(var(--margin));
    display: grid;
    grid-template-columns: 1fr 1fr;
    align-items: center;
    margin-bottom: 8px;
  }
  .first {
    margin-left: calc(var(--margin) + 42px);
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
</style>
