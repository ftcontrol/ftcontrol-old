<script lang="ts">
  import { info } from "$lib"
  import { Section } from "$primitives"
</script>

<Section title={"Graph"}>
  <ul>
    {#each Object.entries(info.graphs) as [key, list]}
      <li>{key}: {list.length} entries</li>
      <div class="graph">
        {#each list as l}
          <p>{l.data} {l.timestamp}</p>
        {/each}
      </div>
    {/each}
  </ul>
</Section>

<style>
  .graph {
    width: 100%;
    aspect-ratio: 1 / 1;
    position: relative;
  }
  .entry {
  }
  ul {
    list-style: none;
    padding: 0;
  }

  li {
    margin: 0.5rem 0;
  }
</style>
