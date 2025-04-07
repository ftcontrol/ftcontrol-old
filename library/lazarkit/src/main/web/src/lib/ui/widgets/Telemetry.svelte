<script lang="ts">
  import { info } from "$lib"
  import { Section } from "$primitives"
</script>

<Section title={"Telemetry"}>
  {#each info.telemetry as line}
    <p>{line}</p>
  {/each}
  {#if info.telemetry.length == 0}
    <p>No telemetry found.</p>
  {/if}
</Section>

<style>
</style>
