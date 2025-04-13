<script lang="ts">
  import { info } from "$lib"
  import type { GraphPacket } from "$lib/socket.svelte"
  import Section from "$ui/primitives/Section.svelte"

  const selectedKeys = $state<{ [key: string]: boolean }>({})

  function toggleKey(key: string) {
    selectedKeys[key] = !selectedKeys[key]
  }

  function normalize(
    values: number[],
    min: number,
    max: number,
    range = [0, 1]
  ) {
    return values.map((v) =>
      max === min
        ? 0.5
        : ((v - min) / (max - min)) * (range[1] - range[0]) + range[0]
    )
  }

  function getNormalizedGraphPoints(selectedGraphs: {
    [key: string]: GraphPacket[]
  }) {
    // Extract all data points from selected graphs
    const allDataValues = Object.values(selectedGraphs)
      .flat()
      .map((l) => l.data)

    const allTimeValues = Object.values(selectedGraphs)
      .flat()
      .map((l) => l.timestamp)

    const globalDataMin = Math.min(...allDataValues)
    const globalDataMax = Math.max(...allDataValues)
    const timeStart = Math.min(...allTimeValues)
    const timeEnd = timeStart + info.timeWindow * 1000

    const normalizedPoints: { [key: string]: { x: number; y: number }[] } = {}

    Object.entries(selectedGraphs).forEach(([key, list]) => {
      const timeValues = list.map((l) => l.timestamp)
      const dataValues = list.map((l) => l.data)

      const x = timeValues.map(
        (t) => ((t - timeStart) / (timeEnd - timeStart)) * 100
      )
      const y = normalize(dataValues, globalDataMin, globalDataMax, [0, 100])

      normalizedPoints[key] = list.map((_, i) => ({
        x: Math.min(100, Math.max(0, x[i])),
        y: 100 - y[i],
      }))
    })

    return normalizedPoints
  }

  const colors = ["red", "lime", "cyan", "yellow", "magenta", "orange", "white"]
</script>

<Section title="Graph">
  <input type="range" min="1" max="60" bind:value={info.timeWindow} />
  <ul>
    {#each Object.entries(info.graphs) as [key, list]}
      <li>
        <button
          onclick={() => toggleKey(key)}
          class:selected={selectedKeys[key] == true}
        >
          {#if selectedKeys[key] == true}✅{/if}
          {key}
        </button>
        – {list.length} entries (last {info.timeWindow}s)
      </li>
    {/each}
  </ul>

  <div class="graph">
    <svg viewBox="0 0 100 100" preserveAspectRatio="none">
      {#if Object.keys(selectedKeys).filter((key) => selectedKeys[key]).length > 0}
        {#each Object.entries(getNormalizedGraphPoints(Object.fromEntries(Object.entries(selectedKeys)
                .filter(([key, isSelected]) => isSelected)
                .map( ([key]) => [key, info.graphs[key]] )))) as [key, points], index}
          <polyline
            fill="none"
            stroke={colors[index % colors.length]}
            stroke-width="0.6"
            points={points.map((p) => `${p.x},${p.y}`).join(" ")}
          />
        {/each}
      {/if}
    </svg>
  </div>
</Section>

<style>
  .graph {
    width: 100%;
    aspect-ratio: 1 / 1;
    position: relative;
    border: 1px solid #ccc;
    margin-top: 1rem;
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
    background-color: #111;
  }

  button {
    background: transparent;
    border: 1px solid #666;
    color: white;
    border-radius: 0.25rem;
    padding: 0.2rem 0.5rem;
    cursor: pointer;
    margin-right: 0.5rem;
    font-family: monospace;
  }

  button.selected {
    background-color: #333;
    border-color: white;
  }
</style>
