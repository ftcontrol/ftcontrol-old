<script lang="ts">
  import { info, socket } from "$lib"
  import { Types, type GenericTypeJson } from "$lib/genericType"
  import { Section } from "$primitives"
  import Field from "$ui/Field.svelte"

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
      if (field.type == Types.CUSTOM) {
        var innerIsChanged = isChanged(field.customValues)
        if (innerIsChanged) return true
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

  function sendAllUpdates(fields: GenericTypeJson[]) {
    var changedFields = []
    for (const field of fields) {
      //TODO: handle custom types
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
        <Field {item} />
      {/each}
    </div>
  {/each}
  {#if info.jvmFields.length == 0}
    <p>No configurables found.</p>
  {/if}
</Section>

<style>
</style>
