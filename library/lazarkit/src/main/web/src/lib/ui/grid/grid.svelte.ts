import { notifications } from "$lib"
import { v4 as uuidv4 } from "uuid"
export type ModuleType = {
  pluginID: string
  pageID: string
  type: WidgetTypes
}
export type Module = {
  id: string
  activeType: number
  types: ModuleType[]
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
  "Custom",
  "OpMode Control",
  "Gamepad",
  "Field",
  "Telemetry",
  "Configurables",
  "Graph",
  "Capture",
  "Test",
]

export enum WidgetTypes {
  CONTROLS = "OpMode Control",
  GAMEPAD = "Gamepad",
  FIELD = "Field",
  TELEMETRY = "Telemetry",
  CONFIGURABLES = "Configurables",
  GRAPH = "Graph",
  CAPTURE = "Capture",
  CUSTOM = "Custom",
  TEST = "Test",
}

export type Preset = {
  id: string
  name: string
  x: number
  y: number
  modules: Module[]
}

export function defaultModuled(): Preset {
  return {
    id: uuidv4(),
    name: "Default",
    x: 10,
    y: 10,
    modules: [
      {
        id: uuidv4(),
        activeType: 0,
        types: [
          {
            type: WidgetTypes.CONTROLS,
            pluginID: "none",
            pageID: "none",
          },
        ],
        start: {
          x: 1,
          y: 1,
        },
        sizes: {
          x: 3,
          y: 2,
        },
      },
      {
        id: uuidv4(),
        activeType: 0,
        types: [
          {
            type: WidgetTypes.CAPTURE,
            pluginID: "none",
            pageID: "none",
          },
        ],
        start: {
          x: 4,
          y: 1,
        },
        sizes: {
          x: 3,
          y: 4,
        },
      },
      {
        id: uuidv4(),
        activeType: 0,
        types: [
          {
            type: WidgetTypes.GAMEPAD,
            pluginID: "none",
            pageID: "none",
          },
        ],
        start: {
          x: 1,
          y: 3,
        },
        sizes: {
          x: 3,
          y: 2,
        },
      },
      {
        id: uuidv4(),
        activeType: 0,
        types: [
          {
            type: WidgetTypes.FIELD,
            pluginID: "none",
            pageID: "none",
          },
        ],
        start: {
          x: 1,
          y: 5,
        },
        sizes: {
          x: 3,
          y: 6,
        },
      },
      {
        id: uuidv4(),
        activeType: 0,
        types: [
          {
            type: WidgetTypes.TELEMETRY,
            pluginID: "none",
            pageID: "none",
          },
          {
            type: WidgetTypes.GRAPH,
            pluginID: "none",
            pageID: "none",
          },
        ],
        start: {
          x: 4,
          y: 5,
        },
        sizes: {
          x: 3,
          y: 6,
        },
      },
      {
        id: uuidv4(),
        activeType: 0,
        types: [
          {
            type: WidgetTypes.CONFIGURABLES,
            pluginID: "none",
            pageID: "none",
          },
        ],
        start: {
          x: 7,
          y: 1,
        },
        sizes: {
          x: 4,
          y: 10,
        },
      },
    ],
  }
}

export class Grid {
  cellsX = $state(8)
  cellsY = $state(6)
  name = $state("Default")
  id = $state("")

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

  constructor(preset: Preset | null) {
    if (preset == null) preset = defaultModuled()
    this.id = preset.id
    this.name = preset.name
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

  remove(id: string) {
    this.modules = this.modules.filter((it) => it.id != id)
  }

  addNew(x: number, y: number) {
    if (this.modulesMap[y][x] != null) {
      notifications.add("Slot is already in use.")
      return
    }

    this.modules.push({
      id: uuidv4(),
      activeType: 0,
      types: [
        {
          type: WidgetTypes.TEST,
          pluginID: "none",
          pageID: "none",
        },
      ],
      start: {
        x,
        y,
      },
      sizes: {
        x: 1,
        y: 1,
      },
    })
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

  static fromJSON(jsonObj: Preset): Grid {
    return new Grid(jsonObj)
  }

  toJSON(): Preset {
    return {
      id: this.id,
      name: this.name,
      x: this.cellsX,
      y: this.cellsY,
      modules: this.modules,
    }
  }
}
