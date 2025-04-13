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

    const timeStart = Math.min(...timeValues)
    const timeEnd = timeStart + info.timeWindow * 1000

    const x = timeValues.map(
      (t) => ((t - timeStart) / (timeEnd - timeStart)) * 100
    )
    const y = normalize(dataValues, [0, 100])

    return list.map((_, i) => ({
      x: Math.min(100, Math.max(0, x[i])),
      y: 100 - y[i],
    }))
  }
</script>

<Section title="Graph">
  <input type="range" min="1" max="60" bind:value={info.timeWindow} />
  <ul>
    {#each Object.entries(info.graphs) as [key, list]}
      <li>{key}: {list.length} entries (last {info.timeWindow}s)</li>
      <div class="graph">
        <svg viewBox="0 0 100 100" preserveAspectRatio="none">
          <polyline
            fill="none"
            stroke="white"
            stroke-width="0.5"
            points={getNormalizedGraphPoints(list)
              .map((p) => `${p.x},${p.y}`)
              .join(" ")}
          />
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
