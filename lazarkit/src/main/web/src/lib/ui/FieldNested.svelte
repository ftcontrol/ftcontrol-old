<script lang="ts">
  import { type GenericTypeJson } from "$lib/genericType"
  import Field from "./Field.svelte"

  let {
    item,
    parentItem,
    depth = 0,
  }: {
    item: GenericTypeJson
    parentItem: GenericTypeJson
    depth?: number
  } = $props()
</script>

<Field {item} {parentItem} {depth} />
