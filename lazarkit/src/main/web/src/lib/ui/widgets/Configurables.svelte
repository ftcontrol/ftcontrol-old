<script lang="ts">
  import { info, socket } from "$lib"
  import { Types, type GenericTypeJson } from "$lib/genericType"
  import { Section } from "$primitives"
  import BooleanInput from "$ui/primitives/BooleanInput.svelte"
  import IntInput from "$ui/primitives/IntInput.svelte"
  import SelectInput from "$ui/primitives/SelectInput.svelte"

  function processFields(fields: GenericTypeJson[]): {
    [key: string]: GenericTypeJson[]
  } {
    const result: { [key: string]: GenericTypeJson[] } = {}

    for (const field of fields) {
      if (!result.hasOwnProperty(field.className)) {
        result[field.className] = []
      }
      result[field.className].push(field)
    }

    return result
  }

  function isChanged(fields: GenericTypeJson[]): boolean {
    for (const field of fields) {
      if (field.valueString != field.newValueString && field.isValid) {
        return true
      }
    }
    return false
  }

  function getClassName(item: string): string {
    return item.split(".").at(-1) || ""
  }
  function getStartClassName(item: string): string {
    return item.split("." + getClassName(item))[0]
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

  function sendAllUpdates(fields: GenericTypeJson[]) {
    var changedFields = []
    for (const field of fields) {
      if (field.valueString != field.newValueString && field.isValid) {
        changedFields.push({
          className: field.className,
          fieldName: field.fieldName,
          type: field.type,
          valueString: field.newValueString,
        })
        field.valueString = field.newValueString
      }
    }
    socket.sendMessage({
      kind: "jvmFields",
      fields: changedFields,
    })
  }
</script>

<Section title={"Configurables"}>
  {#each Object.entries(processFields(info.jvmFields)) as [key, items]}
    {#if isChanged(info.jvmFields)}
      <button
        onclick={() => {
          sendAllUpdates(info.jvmFields)
        }}>Update All</button
      >
    {/if}
    <div>
      <h3>{key}</h3>
      {#each items as item}
        <form
          class="item"
          class:disabled={item.type == Types.UNKNOWN}
          onsubmit={() => sendFieldUpdate(item)}
        >
          <p>{item.fieldName} {item.type} {item.isValid}</p>
          {#if item.valueString != item.newValueString && item.isValid}
            <button
              onclick={() => {
                if (!item.isValid) return
                sendFieldUpdate(item)
              }}>Update</button
            >
          {/if}
          {#if item.type == Types.ENUM}
            <SelectInput
              currentValue={item.valueString}
              possibleValues={item.possibleValues}
            />
          {:else if item.type == Types.BOOLEAN}
            <BooleanInput
              startValue={item.valueString}
              currentValue={item.newValueString}
            />
          {:else if [Types.INT, Types.LONG].includes(item.type)}
            <IntInput
              bind:value={item.value}
              bind:isValid={item.isValid}
              bind:startValue={item.valueString}
              bind:currentValue={item.newValueString}
            />
          {:else if [Types.FLOAT, Types.DOUBLE].includes(item.type)}
            <!-- <IntInput currentValue={item.valueString} /> -->
          {:else}
            {JSON.stringify(item)}
          {/if}
          <!-- {#if item.type == "CUSTOM"}
            <p>{JSON.stringify(item.customValues)}</p>
          {/if}
          {#if item.type == "ARRAY"}
            <p>{JSON.stringify(item.arrayValues)}</p>
          {/if} -->
          <!-- <p>
            {item.valueString}
          </p>
          {JSON.stringify(item)} -->
        </form>
      {/each}
    </div>
  {/each}
  {#if info.jvmFields.length == 0}
    <p>No configurables found.</p>
  {/if}
</Section>

<style>
  .item {
    border-bottom: 1px solid black;
  }
  .item.disabled {
    opacity: 0.5;
  }
</style>
