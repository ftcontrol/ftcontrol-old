<script lang="ts">
  import { info } from "$lib"
  import { Types, type GenericTypeJson } from "$lib/genericType"
  import { Section } from "$primitives"
  import BooleanInput from "$ui/primitives/BooleanInput.svelte"
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

  function getClassName(item: string): string {
    return item.split(".").at(-1) || ""
  }
  function getStartClassName(item: string): string {
    return item.split("." + getClassName(item))[0]
  }
</script>

<Section title={"Configurables"}>
  {#each Object.entries(processFields(info.jvmFields)) as [key, items]}
    <div>
      <h3>{key}</h3>
      {#each items as item}
        <div class="item" class:disabled={item.type == Types.UNKNOWN}>
          <p>{item.fieldName} {item.type}</p>
          {#if item.type == Types.ENUM}
            <SelectInput
              currentValue={item.valueString}
              possibleValues={item.possibleValues}
            />
          {:else if item.type == Types.BOOLEAN}
            <BooleanInput
              currentValue={item.valueString == "true" ? true : false}
            />
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
        </div>
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
