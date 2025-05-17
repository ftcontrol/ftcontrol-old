<script lang="ts">
  import { info } from "$lib"
  import Content from "$ui/primitives/Content.svelte"
</script>

<Content>
  {#each info.telemetry as line}
    <p>{line}</p>
  {/each}
  {#if info.telemetry.length == 0}
    <p>No telemetry found.</p>
  {/if}
</Content>

<style>
</style>
