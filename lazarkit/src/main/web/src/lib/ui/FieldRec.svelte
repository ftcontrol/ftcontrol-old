<script lang="ts">
  import type { JvmFieldInfo } from "$lib/socket.svelte"

  let { line }: { line: JvmFieldInfo } = $props()
</script>

<p>
  {line.className} | {line.fieldName} | {line.type} | {line.arrayType}
</p>
{#if ["INT", "DOUBLE", "STRING"].includes(line.type)}
  <p>{line.currentValueString}</p>
{:else if line.type == "ENUM"}
  <p>{line.currentValueString} / {line.possibleValues}</p>
{:else}
  <p>TODO</p>
  <p>{JSON.stringify(line, null, 2)}</p>
{/if}

<style>
</style>
