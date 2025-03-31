<script lang="ts">
  import { type GenericTypeJson } from "$lib/genericType"
  import Field from "./Field.svelte"

  let {
    item,
    depth = 0,
  }: {
    item: GenericTypeJson
    depth?: number
  } = $props()
</script>

<Field {item} {depth} />
