import { GenericModularDependency } from "../modular.svelte"

export class ResizingManager extends GenericModularDependency {
  resizingModule: WidgetGroup | null = $state(null)

  isResizing = $derived(this.resizingModule != null)

  startResizing(m: WidgetGroup) {
    this.resizingModule = m
  }

  stopResizing(event: MouseEvent) {
    this.resizingMouse(event)
    this.resizingModule = null
  }

  canResize(newX: number, newY: number) {
    if (this.resizingModule == null) return false
    if (newX < this.resizingModule.start.x) return false
    if (newY < this.resizingModule.start.y) return false
    for (let dx = this.resizingModule.start.x; dx <= newX; dx++) {
      for (let dy = this.resizingModule.start.y; dy <= newY; dy++) {
        if (
          dx > settings.currentGrid.cellsX ||
          dy > settings.currentGrid.cellsY ||
          dx < 1 ||
          dy < 1 ||
          (settings.currentGrid.modulesMap[dy][dx] != null &&
            settings.currentGrid.modulesMap[dy][dx] != this.resizingModule.id)
        ) {
          return false
        }
      }
    }
    return true
  }

  private resizingMouse(event: MouseEvent) {
    if (this.resizingModule != null) {
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

        const x = parseInt(stringX)
        const y = parseInt(stringY)

        if (!this.canResize(x, y)) return
        this.resizingModule.sizes = {
          x: x - this.resizingModule.start.x + 1,
          y: y - this.resizingModule.start.y + 1,
        }
      }

      // [data-id="${this.resizingModule.id}"]
    }
  }
}
