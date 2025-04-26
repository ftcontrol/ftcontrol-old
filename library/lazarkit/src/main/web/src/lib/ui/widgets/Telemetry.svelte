<script lang="ts">
  import { info } from "$lib"
  import { Section } from "$primitives"
  import Content from "$ui/primitives/Content.svelte"
</script>

<Section title={"Telemetry"}>
  <Content>
    {#each info.telemetry as line}
      <p>{line}</p>
    {/each}
    {#if info.telemetry.length == 0}
      <p>No telemetry found.</p>
    {/if}
  </Content>
</Section>

<style>
</style>
