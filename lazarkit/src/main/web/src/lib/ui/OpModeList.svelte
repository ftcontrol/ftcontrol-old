<script lang="ts">
  import { info } from "$lib"
  import type { OpMode } from "$lib/socket.svelte"

  let {
    flavour,
    onselect,
  }: { flavour: OpMode["flavor"]; onselect: (opMode: OpMode) => void } =
    $props()
</script>

<!-- TODO: handle empty -->
<section>
  {#each info.opModes.filter((it) => it.flavor == flavour) as opMode}
    <button onclick={() => onselect(opMode)}>
      {opMode.name} / {opMode.group}
    </button>
  {/each}
</section>

<style>
  button {
    display: block;
    border: none;
    background-color: transparent;
    margin: 0.5rem 1rem;
  }
</style>
