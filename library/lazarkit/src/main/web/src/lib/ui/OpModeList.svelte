<script lang="ts">
  import { info } from "$lib"
  import type { OpMode } from "$lib/socket.svelte"

  let {
    flavour,
    onselect,
  }: { flavour: OpMode["flavour"]; onselect: (opMode: OpMode) => void } =
    $props()
</script>

{#each info.opModes.filter((it) => it.flavour == flavour) as opMode}
  <button onclick={() => onselect(opMode)}>
    {opMode.name} / {opMode.group}
  </button>
{/each}
{#if info.opModes.filter((it) => it.flavour == flavour).length == 0}
  <p>No opModes here.</p>
{/if}

<style>
  button {
    display: block;
    border: none;
    background-color: transparent;
    padding: 0.5rem 1rem;
    color: inherit;
  }
  p {
    padding: 0.5rem 1rem;
  }
</style>
