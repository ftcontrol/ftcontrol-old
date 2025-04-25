import { notifications } from "$lib"
import { v4 as uuidv4 } from "uuid"

export type Module = {
  id: string
  type: WidgetTypes
  start: {
    x: number
    y: number
  }
  sizes: {
    x: number
    y: number
  }
}

export const allWidgetTypes = [
  "controls",
  "gamepad",
  "field",
  "telemetry",
  "configurables",
  "graph",
  "capture",
  "dash limelight",
  "feed limelight",
  "test",
]

export enum WidgetTypes {
  CONTROLS = "controls",
  GAMEPAD = "gamepad",
  FIELD = "field",
  TELEMETRY = "telemetry",
  CONFIGURABLES = "configurables",
  GRAPH = "graph",
  CAPTURE = "capture",
  LIMELIGHT_DASH = "dash limelight",
  LIMELIGHT_FEED = "feed limelight",
  TEST = "test",
}

export type Preset = {
  x: number
  y: number
  modules: Module[]
}

const defaultModuled: Preset = {
  x: 12,
  y: 8,
  modules: [
    {
      id: uuidv4(),
      type: WidgetTypes.CONTROLS,
      start: {
        x: 1,
        y: 1,
      },
      sizes: {
        x: 6,
        y: 3,
      },
    },
  ],
}

export class Grid {
  cellsX = $state(8)
  cellsY = $state(6)

  isMoving = $state(false)

  selectedCellX = $state(-1)
  selectedCellY = $state(-1)

  selectedWidgetId = $state("")

  selectedWidget = $derived(this.getWidgetById(this.selectedWidgetId))

  canPlace = $derived(this.checkPlace(this.selectedWidget))

  checkIndex(index: number): boolean {
    return this.coreCheckPlace(
      (index % this.cellsX) + 1,
      Math.floor(index / this.cellsX) + 1,
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
          x > this.cellsX ||
          y > this.cellsY ||
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

  private getWidgetById(id: string) {
    for (const w of this.modules) {
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
    for (let y = 1; y < this.cellsY + 1; y++) {
      map[y] = []
      for (let x = 1; x < this.cellsX + 1; x++) {
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

    return map
  }

  countCells = $derived.by(() => {
    var sum = 0
    for (const w of this.modules) {
      sum += w.sizes.x * w.sizes.y
    }
    return sum
  })

  constructor(preset: Preset | null) {
    if (preset == null) preset = defaultModuled
    this.cellsX = preset.x
    this.cellsY = preset.y
    this.modules = preset.modules
  }

  startMoving(id: string) {
    this.isMoving = true
    this.selectedWidgetId = id
    for (const w of this.modules) {
      if (w.id == id) {
        this.selectedCellX = w.start.x - 1
        this.selectedCellY = w.start.y - 1
      }
    }
  }

  stopMoving(error: string | null) {
    if (error != "" && error != null) notifications.add(error)
    this.isMoving = false
    this.selectedWidgetId = ""
    return
  }

  performMove() {
    if (this.selectedWidget == null) return
    for (const w of this.modules) {
      if (w.id == this.selectedWidgetId) {
        w.start.x = this.selectedCellX
        w.start.y = this.selectedCellY
      }
    }
  }

  private canExpand(w: Module, dx: number, dy: number): boolean {
    const { x: startX, y: startY } = w.start
    const { x: width, y: height } = w.sizes

    if (dx === 1) {
      // Right
      for (let dyOffset = 0; dyOffset < height; dyOffset++) {
        const y = startY + dyOffset
        const x = startX + width
        if (x > this.cellsX || this.modulesMap[y]?.[x] !== null) return false
      }
    } else if (dx === -1) {
      // Left
      for (let dyOffset = 0; dyOffset < height; dyOffset++) {
        const y = startY + dyOffset
        const x = startX - 1
        if (x < 1 || this.modulesMap[y]?.[x] !== null) return false
      }
    } else if (dy === 1) {
      // Down
      for (let dxOffset = 0; dxOffset < width; dxOffset++) {
        const x = startX + dxOffset
        const y = startY + height
        if (y > this.cellsY || this.modulesMap[y]?.[x] !== null) return false
      }
    } else if (dy === -1) {
      // Up
      for (let dxOffset = 0; dxOffset < width; dxOffset++) {
        const x = startX + dxOffset
        const y = startY - 1
        if (y < 1 || this.modulesMap[y]?.[x] !== null) return false
      }
    }

    return true
  }

  canExpandRight(w: Module) {
    return this.canExpand(w, 1, 0)
  }

  canExpandLeft(w: Module) {
    return this.canExpand(w, -1, 0)
  }

  canExpandDown(w: Module) {
    return this.canExpand(w, 0, 1)
  }

  canExpandUp(w: Module) {
    return this.canExpand(w, 0, -1)
  }
}
