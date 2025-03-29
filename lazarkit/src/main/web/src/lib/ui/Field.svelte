<script lang="ts">
  import type { CustomTypeJson, GenericTypeJson } from "$lib/genericType"

  let { line }: { line: GenericTypeJson } = $props()

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
</script>

<div class="item" style={line.type == "UNKNOWN" ? "opacity: 0.3;" : ""}>
  <p>{line.className.split("." + line.className.split(".").at(-1))[0]}</p>

  <p>{line.className.split(".").at(-1)} {line.fieldName} {line.type}</p>
  <p>
    {line.valueString}
  </p>
  {#if line.type == "ENUM"}
    <p>{JSON.stringify(line.possibleValues)}</p>
  {/if}
  {#if line.type == "CUSTOM"}
    <p>{JSON.stringify(line.customValues)}</p>
  {/if}
  {#if line.type == "ARRAY"}
    <p>{JSON.stringify(line.arrayValues)}</p>
  {/if}
  {JSON.stringify(line)}
</div>

<style>
  .item {
    outline: 1px solid black;
  }
</style>
