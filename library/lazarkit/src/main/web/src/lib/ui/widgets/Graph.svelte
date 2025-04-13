<script lang="ts">
  import { info } from "$lib"
  import type { GraphPacket } from "$lib/socket.svelte"
  import Section from "$ui/primitives/Section.svelte"

  function normalize(values: number[], range = [0, 1]) {
    const min = Math.min(...values)
    const max = Math.max(...values)
    return values.map((v) =>
      max === min
        ? 0.5
        : ((v - min) / (max - min)) * (range[1] - range[0]) + range[0]
    )
  }

  function getNormalizedGraphPoints(list: GraphPacket[]) {
    const dataValues = list.map((l) => l.data)
    const timeValues = list.map((l) => l.timestamp)

    const x = normalize(timeValues, [0, 100])
    const y = normalize(dataValues, [0, 100])

    return list.map((_, i) => ({ x: x[i], y: 100 - y[i] }))
  }
</script>

<Section title="Graph">
  <input type="range" min="1" max="60" bind:value={info.timeWindow} />
  <ul>
    {#each Object.entries(info.graphs) as [key, list]}
      <li>{key}: {list.length} entries (last {info.timeWindow}s)</li>
      <div class="graph">
        <svg viewBox="0 0 100 100" preserveAspectRatio="none">
          {#each getNormalizedGraphPoints(list) as point}
            <circle cx={point.x} cy={point.y} r="0.25" fill="white" />
          {/each}
        </svg>
      </div>
    {/each}
  </ul>
</Section>

<style>
  .graph {
    width: 100%;
    aspect-ratio: 1 / 1;
    position: relative;
    border: 1px solid #ccc;
  }

  ul {
    list-style: none;
    padding: 0;
  }

  li {
    margin: 0.5rem 0;
  }

  svg {
    width: 100%;
    height: 100%;
  }
</style>
