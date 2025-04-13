<script lang="ts">
  import { info } from "$lib"
  import { Section } from "$primitives"
</script>

<Section title={"Graph"}>
  <ul>
    {#each Object.entries(info.graphs) as [key, list]}
      <li>{key}: {list.length} entries</li>
    {/each}
  </ul>
  {JSON.stringify(info.graphs)}
</Section>

<style>
  ul {
    list-style: none;
    padding: 0;
  }

  li {
    margin: 0.5rem 0;
  }
</style>
