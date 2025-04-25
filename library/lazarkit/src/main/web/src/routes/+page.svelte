<script lang="ts">
  import { gamepads, notifications } from "$lib"
  import GamepadDrawing from "$lib/ui/GamepadDrawing.svelte"
  import GameField from "$ui/widgets/fields/GameField.svelte"
  import Flows from "$ui/widgets/Flows.svelte"
  import Graph from "$ui/widgets/Graph.svelte"
  import PlaybackHistory from "$ui/widgets/PlaybackHistory.svelte"
  import { OpModeControl, Telemetry, Configurables } from "$widgets"
  import { onMount } from "svelte"

  let isMoving = $state(false)
  let selectedCell = $state(-1)
  let selectedWidget = $state(-1)

  function getWidget(id) {
    for (const w of widgets) {
      if (w.id == id) return w
    }
    return null
  }

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
        x: 2,
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

<div class="container">
  <section>
    {#each widgets as w}
      <div
        class="item"
        class:isOverlay={selectedWidget == w.id}
        style="
    grid-column: {w.start.x} / span {w.sizes.x};
    grid-row: {w.start.y} / span {w.sizes.y};
    "
      >
        <button
          class="mover"
          onmousedown={() => {
            isMoving = true
            selectedWidget = w.id
          }}
        >
          MOVER
        </button>
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
    {#if getWidget(selectedWidget) != null}
      <div
        class="item"
        style="
grid-column: {(selectedCell % 12) + 1} / span {getWidget(selectedWidget).sizes
          .x};
grid-row: {Math.floor(selectedCell / 12) + 1} / span {getWidget(selectedWidget)
          .sizes.y};
"
      >
        <p>Moving {selectedCell}</p>
      </div>
    {/if}

    {#each Array.from({ length: 12 * 12 - getAllCells(widgets) }) as _, index}
      <!-- <p class="m"></p> -->
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

  {#if isMoving}
    <section
      onmouseup={() => {
        //do all checks

        const w = getWidget(selectedWidget)
        if (w == null) {
          notifications.add("W is null.")
          isMoving = false
          selectedWidget = -1
          return
        }
        const newX = (selectedCell % 12) + 1
        const newY = Math.floor(selectedCell / 12) + 1

        let canPlace = true
        for (let dx = 0; dx < w.sizes.x; dx++) {
          for (let dy = 0; dy < w.sizes.y; dy++) {
            const x = newX + dx
            const y = newY + dy
            if (
              x >= 12 ||
              y >= 12 ||
              x < 1 ||
              y < 1 ||
              (widgetsMap[y][x] != null && widgetsMap[y][x] != w.id)
            ) {
              canPlace = false
            }
          }
        }

        if (!canPlace) {
          notifications.add("Cannot move there.")
          isMoving = false
          selectedWidget = -1
          return
        }

        for (const w of widgets) {
          if (w.id == selectedWidget) {
            w.start.x = (selectedCell % 12) + 1
            w.start.y = Math.floor(selectedCell / 12) + 1
          }
        }
        isMoving = false
        selectedWidget = -1
      }}
    >
      {#each Array.from({ length: 12 * 12 }) as _, index}
        <p
          class="m"
          onmouseover={() => {
            const w = getWidget(selectedWidget)
            if (w == null) {
              notifications.add("W is null.")
              isMoving = false
              selectedWidget = -1
              return
            }
            const newX = (index % 12) + 1
            const newY = Math.floor(index / 12) + 1

            let canPlace = true
            for (let dx = 0; dx < w.sizes.x; dx++) {
              for (let dy = 0; dy < w.sizes.y; dy++) {
                const x = newX + dx
                const y = newY + dy
                if (
                  x >= 12 ||
                  y >= 12 ||
                  x < 1 ||
                  y < 1 ||
                  (widgetsMap[y][x] != null && widgetsMap[y][x] != w.id)
                ) {
                  canPlace = false
                }
              }
            }

            if (canPlace) {
              selectedCell = index
              return
            }

            notifications.add("Not allowed.")
          }}
        >
          <!-- {index} X: {index % 12} Y: {Math.floor(index / 12)} -->
        </p>
      {/each}
    </section>
  {/if}
</div>

<style>
  .m {
    border: 1px solid green;
    /* height: 100%; */
  }
  p {
    border: 1px solid green;
    margin: 0;
  }
  .mover {
    width: 100%;
    display: block;
  }
  .container {
    background-color: black;
    width: 100%;
    height: 100%;
    position: relative;
  }
  section {
    position: absolute;
    display: grid;
    grid-template-columns: repeat(12, 1fr);
    grid-template-rows: repeat(12, 1fr);
    height: 100%;
    width: 100%;
    padding: 0.5rem;
    gap: 0.5rem;
  }
  div.item {
    background-color: var(--card);
    border-radius: 16px;
    overflow: auto;
    border: 2px solid var(--bg);
    transition: background-color var(--d3);

    border: 2px solid red;
  }
  div.item.isOverlay {
    opacity: 0.5;
  }
</style>
