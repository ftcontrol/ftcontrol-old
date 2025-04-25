<script lang="ts">
  import { gamepads, notifications } from "$lib"
  import GamepadDrawing from "$lib/ui/GamepadDrawing.svelte"
  import GameField from "$ui/widgets/fields/GameField.svelte"
  import Flows from "$ui/widgets/Flows.svelte"
  import Graph from "$ui/widgets/Graph.svelte"
  import PlaybackHistory from "$ui/widgets/PlaybackHistory.svelte"
  import { OpModeControl, Telemetry, Configurables } from "$widgets"
  import { onMount } from "svelte"

  function getAllCells(widgets): number {
    var sum = 0
    for (const w of widgets) {
      sum += w.sizes.x * w.sizes.y
    }
    return sum
  }

  function canExpand(w, dx: number, dy: number): boolean {
    const { x: startX, y: startY } = w.start
    const { x: width, y: height } = w.sizes

    if (dx === 1) {
      // Right
      for (let dyOffset = 0; dyOffset < height; dyOffset++) {
        const y = startY + dyOffset
        const x = startX + width
        if (widgetsMap[y]?.[x] !== null) return false
      }
    } else if (dx === -1) {
      // Left
      for (let dyOffset = 0; dyOffset < height; dyOffset++) {
        const y = startY + dyOffset
        const x = startX - 1
        if (widgetsMap[y]?.[x] !== null) return false
      }
    } else if (dy === 1) {
      // Down
      for (let dxOffset = 0; dxOffset < width; dxOffset++) {
        const x = startX + dxOffset
        const y = startY + height
        if (widgetsMap[y]?.[x] !== null) return false
      }
    } else if (dy === -1) {
      // Up
      for (let dxOffset = 0; dxOffset < width; dxOffset++) {
        const x = startX + dxOffset
        const y = startY - 1
        if (widgetsMap[y]?.[x] !== null) return false
      }
    }

    return true
  }

  function canExpandRight(w) {
    return canExpand(w, 1, 0)
  }

  function canExpandLeft(w) {
    return canExpand(w, -1, 0)
  }

  function canExpandDown(w) {
    return canExpand(w, 0, 1)
  }

  function canExpandUp(w) {
    return canExpand(w, 0, -1)
  }

  let widgets = $state([
    {
      id: 0,
      type: "controls",
      start: {
        x: 1,
        y: 1,
      },
      sizes: {
        x: 3,
        y: 3,
      },
    },
    {
      id: 1,
      type: "test",
      start: {
        x: 4,
        y: 1,
      },
      sizes: {
        x: 3,
        y: 3,
      },
    },
  ])

  let widgetsMap: any[][] = $state([])

  function updateMap(widgets) {
    let maxX = 0
    let maxY = 0
    for (const w of widgets) {
      maxX = Math.max(maxX, w.start.x + w.sizes.x)
      maxY = Math.max(maxY, w.start.y + w.sizes.y)
    }

    const map: any[][] = []
    for (let y = 1; y < 13; y++) {
      map[y] = []
      for (let x = 1; x < 13; x++) {
        map[y][x] = null
      }
    }

    for (const widget of widgets) {
      for (let dx = 0; dx < widget.sizes.x; dx++) {
        for (let dy = 0; dy < widget.sizes.y; dy++) {
          const x = widget.start.x + dx
          const y = widget.start.y + dy
          map[y][x] = widget.id
        }
      }
    }

    widgetsMap = map
    console.table(map)
  }

  $effect(() => {
    updateMap(widgets)
  })
</script>

<section>
  {#each widgets as w}
    <div
      style="
  grid-column: {w.start.x} / span {w.sizes.x};
  grid-row: {w.start.y} / span {w.sizes.y};
  "
    >
      <button class="mover"> MOVER </button>
      {#if w.type == "controls"}
        <OpModeControl />
      {:else}
        <p>Unknown type</p>
      {/if}
      <button
        onclick={() => {
          if (!canExpandRight(w)) {
            notifications.add("Not enough space to move right.")
            return
          }
          w.start.x++
        }}
      >
        Move Right
      </button>
      <button
        onclick={() => {
          if (!canExpandRight(w)) {
            notifications.add("Not enough space to expand right.")
            return
          }
          w.sizes.x++
        }}>Expand Right</button
      >
      <button
        onclick={() => {
          if (w.sizes.x <= 1) {
            notifications.add("Cannot make this small.")
            return
          }
          w.sizes.x--
        }}>Small Right</button
      >
      <button
        onclick={() => {
          if (!canExpandLeft(w)) {
            notifications.add("Not enough space to move left.")
            return
          }
          w.start.x--
        }}
      >
        Move Left
      </button>
      <button
        onclick={() => {
          if (!canExpandLeft(w)) {
            notifications.add("Not enough space to expand left.")
            return
          }
          w.sizes.x++
          w.start.x--
        }}>Expand Left</button
      >
      <button
        onclick={() => {
          if (w.sizes.x <= 1) {
            notifications.add("Cannot make this small.")
            return
          }
          w.sizes.x--
          w.start.x++
        }}>Small Left</button
      >
    </div>
  {/each}

  {#each Array.from({ length: 12 * 12 - getAllCells(widgets) }) as _, index}
    <p>{index + 1}</p>
  {/each}

  <!-- <div>
    <OpModeControl />
    {#if gamepads.current != null}
      <GamepadDrawing gamepad={gamepads.gamepads[0]} />
    {/if}
  </div>
  <div>
    <GameField />
    <Telemetry />
  </div>
  <div>
    <Configurables />
  </div>
  <div>
    <Graph />
  </div> -->
</section>

<style>
  section {
    display: grid;
    grid-template-columns: repeat(12, 1fr);
    grid-template-rows: repeat(12, 1fr);
    height: 100%;
    width: 100%;
    padding: 0.5rem;
    gap: 0.5rem;
  }
  div {
    background-color: var(--card);
    border-radius: 16px;
    overflow: auto;
    border: 2px solid var(--bg);
    transition: background-color var(--d3);
  }
</style>
