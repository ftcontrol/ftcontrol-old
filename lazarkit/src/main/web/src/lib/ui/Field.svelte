<script lang="ts">
  import type { JvmFieldInfo } from "$lib/socket.svelte"
  import FieldArray from "./FieldArray.svelte"
  import FieldRec from "./FieldRec.svelte"

  let { line }: { line: JvmFieldInfo } = $props()

  function getNesting(input: JvmFieldInfo, level = 0) {
    var components: {
      item: JvmFieldInfo
      level: number
    }[] = []

    for (const item of input.customValues) {
      if (item.type === "CUSTOM" && item.customValues) {
        components.push(...getNesting(item, level + 1))
      } else {
        components.push({ item, level })
      }
    }

    return components
  }
</script>

<div class="var" style={line.type == "UNKNOWN" ? "opacity: 0.3;" : ""}>
  {#if ["INT", "DOUBLE", "STRING", "ENUM"].includes(line.type)}
    <FieldRec {line} />
  {:else if line.type == "ENUM"}
    <p>{line.currentValueString} / {line.possibleValues}</p>
  {:else if line.type == "CUSTOM"}
    <p>
      {line.className} | {line.fieldName} | {line.type} | {line.arrayType}
    </p>

    {#each getNesting(line) as item}
      <p>{item.level}</p>
      <FieldRec line={item.item} />
    {/each}
  {:else if line.type == "ARRAY"}
    <FieldArray {line} />
  {:else}
    <FieldRec {line} />
  {/if}
</div>

<style>
  .var {
    outline: 1px solid black;
  }
</style>
