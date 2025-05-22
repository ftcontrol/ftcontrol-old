import { GenericModularDependency } from "../modular.svelte"

export class MovingManager extends GenericModularDependency {
  movingModule: WidgetGroup | null = $state(null)

  isMov = $derived(this.movingModule != null)

  xTile: number | null = $state(null)
  yTile: number | null = $state(null)

  startMov(m: WidgetGroup, x: number, y: number) {
    this.movingModule = m

    const element = document.querySelectorAll(`.widget[data-id="${m.id}"]`)[0]
    const box = element.getBoundingClientRect()

    this.xTile = Math.ceil(((x - box.x) / box.width) * m.sizes.x) - 1
    this.yTile = Math.ceil(((y - box.y) / box.height) * m.sizes.y) - 1
  }

  showWidget(x: number, y: number) {
    return this.canResize(x, y) || this.canMov(x, y)
  }

  canMov(newX: number, newY: number) {
    if (!this.isMov) return false
    if (this.xTile == null) return false
    if (this.yTile == null) return false
    if (!this.movingModule) return false

    const newStartX = newX - this.xTile
    const newStartY = newY - this.yTile

    for (let dx = newStartX; dx < newStartX + this.movingModule.sizes.x; dx++) {
      for (
        let dy = newStartY;
        dy < newStartY + this.movingModule.sizes.y;
        dy++
      ) {
        if (
          dx > settings.currentGrid.cellsX ||
          dy > settings.currentGrid.cellsY ||
          dx < 1 ||
          dy < 1 ||
          (settings.currentGrid.modulesMap[dy][dx] != null &&
            settings.currentGrid.modulesMap[dy][dx] != this.movingModule.id)
        ) {
          return false
        }
      }
    }
    return true
  }

  private moveMouse(event: MouseEvent) {
    if (this.movingModule != null) {
      const elements = document.elementsFromPoint(event.clientX, event.clientY)
      const el = elements.filter(
        (el) =>
          el instanceof HTMLElement &&
          el.classList.contains("overlay-item") &&
          el.classList.contains("isShown") &&
          el.hasAttribute("data-x") &&
          el.hasAttribute("data-y")
      )[0]

      if (el != null) {
        let stringX = el.getAttribute("data-x")
        let stringY = el.getAttribute("data-y")

        if (stringX == null || stringY == null) return
        if (this.xTile == null || this.yTile == null) return

        const x = parseInt(stringX)
        const y = parseInt(stringY)

        if (!this.canMov(x, y)) return
        this.movingModule.start = {
          x: x - this.xTile,
          y: y - this.yTile,
        }
      }
    }
  }

  stopMov(event: MouseEvent) {
    this.moveMouse(event)
    this.movingModule = null
    this.xTile = null
    this.yTile = null
  }
}
