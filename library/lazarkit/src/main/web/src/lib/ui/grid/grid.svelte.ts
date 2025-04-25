import { notifications } from "$lib"

export type Module = {
  id: number
  type: "controls" | "test"
  start: {
    x: number
    y: number
  }
  sizes: {
    x: number
    y: number
  }
}

const defaultModuled: Module[] = [
  {
    id: 0,
    type: "controls",
    start: {
      x: 1,
      y: 1,
    },
    sizes: {
      x: 6,
      y: 3,
    },
  },
  {
    id: 1,
    type: "test",
    start: {
      x: 7,
      y: 1,
    },
    sizes: {
      x: 2,
      y: 3,
    },
  },
]

class Grid {
  isMoving = $state(false)
  selectedCell = $state(-1)

  selectedCellX = $derived((this.selectedCell % 12) + 1)
  selectedCellY = $derived(Math.floor(this.selectedCell / 12) + 1)

  selectedWidgetId = $state(-1)

  selectedWidget = $derived(this.getWidgetById(this.selectedWidgetId))

  canPlace = $derived(this.checkPlace(this.selectedWidget))

  checkIndex(index: number): boolean {
    return this.coreCheckPlace(
      (index % 12) + 1,
      Math.floor(index / 12) + 1,
      this.selectedWidget
    )
  }

  coreCheckPlace(newX: number, newY: number, selected: Module | null) {
    if (selected == null) return false
    for (let dx = 0; dx < selected.sizes.x; dx++) {
      for (let dy = 0; dy < selected.sizes.y; dy++) {
        const x = newX + dx
        const y = newY + dy
        if (
          x >= 13 ||
          y >= 13 ||
          x < 1 ||
          y < 1 ||
          (this.modulesMap[y][x] != null &&
            this.modulesMap[y][x] != selected.id)
        ) {
          return false
        }
      }
    }
    return true
  }

  private checkPlace(selected: Module | null): boolean {
    return this.coreCheckPlace(this.selectedCellX, this.selectedCellY, selected)
  }

  private getWidgetById(id: number) {
    for (const w of gridManager.modules) {
      if (w.id == id) return w
    }
    return null
  }

  modules = $state<Module[]>([])

  modulesMap: any[][] = $derived(this.updateMap(this.modules))

  private updateMap(widgets: Module[]) {
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

    console.table(map)

    return map
  }

  countCells = $derived.by(() => {
    var sum = 0
    for (const w of this.modules) {
      sum += w.sizes.x * w.sizes.y
    }
    return sum
  })

  constructor(modules: Module[]) {
    if (modules.length == 0) modules = structuredClone(defaultModuled)
    this.modules = modules
  }

  startMoving(id: number) {
    this.isMoving = true
    this.selectedWidgetId = id
    for (const w of this.modules) {
      if (w.id == id) {
        this.selectedCell = 12 * (w.start.y - 1) + w.start.x - 1
      }
    }
  }

  stopMoving(error: string | null) {
    if (error != "" && error != null) notifications.add(error)
    this.isMoving = false
    this.selectedWidgetId = -1
    return
  }

  performMove() {
    if (this.selectedWidget == null) return
    for (const w of gridManager.modules) {
      if (w.id == this.selectedWidgetId) {
        w.start.x = this.selectedCellX
        w.start.y = this.selectedCellY
      }
    }
  }
}

export let gridManager = new Grid([])
