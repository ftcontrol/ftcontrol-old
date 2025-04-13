<script lang="ts">
  import { info } from "$lib"
  import type { GraphPacket } from "$lib/socket.svelte"
  import Section from "$ui/primitives/Section.svelte"

  export let windowSeconds = 10

  function normalize(values: number[], range = [0, 1]) {
    const min = Math.min(...values)
    const max = Math.max(...values)
    return values.map((v) =>
      max === min
        ? 0.5
        : ((v - min) / (max - min)) * (range[1] - range[0]) + range[0]
    )
  }

  function getNormalizedGraphPoints(
    list: GraphPacket[],
    windowSeconds: number
  ) {
    const now = Date.now()
    const windowStart = now - windowSeconds * 1000

    const filtered = list.filter((l) => {
      const ts = new Date(l.timestamp).getTime()
      return ts >= windowStart
    })

    const dataValues = filtered.map((l) => l.data)
    const timeValues = filtered.map((l) => new Date(l.timestamp).getTime())

    const x = normalize(timeValues, [0, 100])
    const y = normalize(dataValues, [0, 100])

    return filtered.map((_, i) => ({ x: x[i], y: 100 - y[i] }))
  }
</script>

<Section title="Graph">
  <ul>
    {#each Object.entries(info.graphs) as [key, list]}
      <li>{key}: {list.length} entries (last {windowSeconds}s)</li>
      <div class="graph">
        <svg viewBox="0 0 100 100" preserveAspectRatio="none">
          {#each getNormalizedGraphPoints(list, windowSeconds) as point}
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
