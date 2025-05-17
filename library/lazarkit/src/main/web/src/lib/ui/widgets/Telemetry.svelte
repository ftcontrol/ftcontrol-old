<script lang="ts">
  import { info } from "$lib"
  import Content from "$ui/primitives/Content.svelte"
  import Header from "$ui/primitives/Header.svelte"
  import Title from "$ui/primitives/Title.svelte"
</script>

<Header>
  <Title>Telemetry</Title>
</Header>
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
