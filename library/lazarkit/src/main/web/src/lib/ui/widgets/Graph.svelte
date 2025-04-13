<script lang="ts">
  import { info } from "$lib"
  import type { GraphPacket } from "$lib/socket.svelte"
  import Section from "$ui/primitives/Section.svelte"

  interface Point {
    x: number
    y: number
  }

  const selectedKeys = $state<{ [key: string]: boolean }>({})

  function toggleKey(key: string) {
    selectedKeys[key] = !selectedKeys[key]
  }

  function normalize(
    values: number[],
    min: number,
    max: number,
    range: [number, number] = [0, 1]
  ): number[] {
    if (max === min) return values.map(() => 0.5)
    return values.map(
      (v) => ((v - min) / (max - min)) * (range[1] - range[0]) + range[0]
    )
  }

  function getNormalizedGraphPoints(selectedGraphs: {
    [key: string]: GraphPacket[]
  }): { [key: string]: Point[] } {
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

    const normalizedPoints: { [key: string]: Point[] } = {}

    Object.entries(selectedGraphs).forEach(([key, list]) => {
      const timeValues = list.map((l) => l.timestamp)
      const dataValues = list.map((l) => l.data)

      const x = normalize(timeValues, timeStart, timeEnd, [0, 100])
      const y = normalize(dataValues, globalDataMin, globalDataMax, [0, 100])

      normalizedPoints[key] = list.map((_, i) => ({
        x: Math.min(100, Math.max(0, x[i])),
        y: 100 - y[i],
      }))
    })

    return normalizedPoints
  }

  const colors = ["red", "lime", "cyan", "yellow", "magenta", "orange"]
</script>

<Section title="Graph">
  <input
    type="range"
    min="1"
    max="60"
    bind:value={info.timeWindow}
    aria-label="Time window"
  />

  <ul>
    {#each Object.entries(info.graphs) as [key, list]}
      <li>
        <button
          onclick={() => toggleKey(key)}
          class:selected={selectedKeys[key]}
          aria-pressed={selectedKeys[key]}
        >
          {#if selectedKeys[key]}✅{/if}
          {key}
        </button>
        – {list.length} entries (last {info.timeWindow}s)
      </li>
    {/each}
  </ul>

  <div class="graph">
    <svg viewBox="0 0 100 100" preserveAspectRatio="none">
      {#each [0, 25, 50, 75, 100] as x}
        <text {x} y="98" font-size="2" fill="white" text-anchor="middle">
          {Math.round((x / 100) * info.timeWindow)}s
        </text>
      {/each}

      {#each [0, 25, 50, 75, 100] as y}
        <text x="2" y={100 - y} font-size="2" fill="white" text-anchor="start">
          {Math.round((y / 100) * 100)}%
        </text>
      {/each}

      {#each [25, 50, 75] as value}
        <line
          x1="0"
          y1={value}
          x2="100"
          y2={value}
          stroke="#333"
          stroke-width="0.2"
        />
        <line
          x1={value}
          y1="0"
          x2={value}
          y2="100"
          stroke="#333"
          stroke-width="0.2"
        />
      {/each}

      {#if Object.keys(selectedKeys).some((key) => selectedKeys[key])}
        {#each Object.entries(getNormalizedGraphPoints(Object.fromEntries(Object.entries(info.graphs).filter(([key]) => selectedKeys[key])))) as [key, points], index}
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
    aspect-ratio: 4 / 3;
    position: relative;
    border: 1px solid #ccc;
    margin-top: 1rem;
  }

  ul {
    list-style: none;
    padding: 0;
    margin: 0.5rem 0;
  }

  li {
    display: flex;
    align-items: center;
    gap: 0.5rem;
    margin: 0.25rem 0;
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
    font-family: monospace;
    display: inline-flex;
    align-items: center;
    gap: 0.25rem;
  }

  button.selected {
    background-color: #333;
    border-color: white;
  }
</style>
