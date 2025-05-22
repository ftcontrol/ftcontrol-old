import { notifications } from "$lib"
import {
  defaultModuled,
  WidgetTypes,
  type WidgetGroup,
  type Preset,
} from "./types"
import { v4 as uuidv4 } from "uuid"

export class Grid {
  cellsX = $state(8)
  cellsY = $state(6)
  name = $state("Default")
  id = $state("")

  modules = $state<WidgetGroup[]>([])

  modulesMap: any[][] = $derived(this.updateMap(this.modules))

  private updateMap(widgets: WidgetGroup[]) {
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
    this.cellsX = preset.xSize
    this.cellsY = preset.ySize
    this.modules = preset.groups
  }

  private canExpand(w: WidgetGroup, dx: number, dy: number): boolean {
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

  addNewAnywhere() {
    for (let y = 1; y <= this.cellsY; y++) {
      for (let x = 1; x <= this.cellsX; x++) {
        if (this.modulesMap[y]?.[x] == null) {
          this.addNew(x, y)
          return
        }
      }
    }

    notifications.add("No available space in the grid.")
  }

  addNew(x: number, y: number) {
    if (this.modulesMap[y][x] != null) {
      notifications.add("Slot is already in use.")
      return
    }

    this.modules.push({
      id: uuidv4(),
      activeWidgetID: 0,
      widgets: [
        {
          type: WidgetTypes.TEST,
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

  canExpandRight(w: WidgetGroup) {
    return this.canExpand(w, 1, 0)
  }

  canExpandLeft(w: WidgetGroup) {
    return this.canExpand(w, -1, 0)
  }

  canExpandDown(w: WidgetGroup) {
    return this.canExpand(w, 0, 1)
  }

  canExpandUp(w: WidgetGroup) {
    return this.canExpand(w, 0, -1)
  }

  static fromJSON(jsonObj: Preset): Grid {
    return new Grid(jsonObj)
  }

  toJSON(): Preset {
    return {
      id: this.id,
      name: this.name,
      xSize: this.cellsX,
      ySize: this.cellsY,
      groups: this.modules,
    }
  }
}
