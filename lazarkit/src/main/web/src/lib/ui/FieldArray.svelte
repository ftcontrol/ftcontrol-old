<script lang="ts">
  import type { JvmFieldInfo } from "$lib/socket.svelte"
  import FieldRec from "./FieldRec.svelte"

  let { line }: { line: JvmFieldInfo } = $props()
</script>

<p>
  {line.className} | {line.fieldName} | {line.type} | {line.arrayType}
</p>

{#each line.arrayValues as v}
  <FieldRec line={v} />
{/each}

<style>
</style>
