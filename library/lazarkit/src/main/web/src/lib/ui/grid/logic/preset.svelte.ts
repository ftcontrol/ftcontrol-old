import { notifications } from "$lib"
import {
  defaultModuled,
  WidgetTypes,
  type WidgetGroup,
  type Preset,
} from "./types"
import { v4 as uuidv4 } from "uuid"

export class PresetManager {
  cellsX = $state(8)
  cellsY = $state(6)
  name = $state("Default")
  id = $state("")

  widgets = $state<WidgetGroup[]>([])

  widgetsMap: any[][] = $derived(this.updateMap(this.widgets))

  constructor(preset: Preset | null) {
    if (preset == null) preset = defaultModuled()
    this.id = preset.id
    this.name = preset.name
    this.cellsX = preset.xSize
    this.cellsY = preset.ySize
    this.widgets = preset.groups
  }

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

    return map
  }

  cellsCount = $derived.by(() => {
    var sum = 0
    for (const w of this.widgets) {
      sum += w.sizes.x * w.sizes.y
    }
    return sum
  })

  private canExpand(w: WidgetGroup, dx: number, dy: number): boolean {
    const { x: startX, y: startY } = w.start
    const { x: width, y: height } = w.sizes

    for (let i = 0; i < (dx !== 0 ? height : width); i++) {
      const x = startX + (dx === 1 ? width : dx === -1 ? -1 : i)
      const y = startY + (dy === 1 ? height : dy === -1 ? -1 : i)

      if (
        x < 1 ||
        x > this.cellsX ||
        y < 1 ||
        y > this.cellsY ||
        this.widgetsMap[y]?.[x] !== null
      ) {
        return false
      }
    }

    return true
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

  remove(id: string) {
    this.widgets = this.widgets.filter((it) => it.id != id)
  }

  addNewAnywhere() {
    for (let y = 1; y <= this.cellsY; y++) {
      for (let x = 1; x <= this.cellsX; x++) {
        if (this.widgetsMap[y]?.[x] == null) {
          this.addNew(x, y)
          return
        }
      }
    }

    notifications.add("No available space in the grid.")
  }

  addNew(x: number, y: number) {
    if (this.widgetsMap[y][x] != null) {
      notifications.add("Slot is already in use.")
      return
    }

    this.widgets.push({
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

  static fromJSON(jsonObj: Preset): PresetManager {
    return new PresetManager(jsonObj)
  }

  toJSON(): Preset {
    return {
      id: this.id,
      name: this.name,
      xSize: this.cellsX,
      ySize: this.cellsY,
      groups: this.widgets,
    }
  }
}
