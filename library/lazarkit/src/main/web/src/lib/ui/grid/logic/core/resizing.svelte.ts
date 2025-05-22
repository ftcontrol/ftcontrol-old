import { settings } from "$lib/settings.svelte"
import { GenericModularDependency } from "../generic.svelte"
import type { WidgetGroup } from "../types"

export class ResizingManager extends GenericModularDependency {
  resizingModule: WidgetGroup | null = $state(null)

  isResizing = $derived(this.resizingModule != null)

  startResizing(m: WidgetGroup) {
    this.resizingModule = m
  }

  canResize(newX: number, newY: number) {
    if (this.resizingModule == null) return false
    if (newX < this.resizingModule.start.x) return false
    if (newY < this.resizingModule.start.y) return false
    if (
      newX == this.resizingModule.start.x + this.resizingModule.sizes.x - 1 &&
      newY == this.resizingModule.start.y + this.resizingModule.sizes.y - 1
    )
      return false

    for (let dx = this.resizingModule.start.x; dx <= newX; dx++) {
      for (let dy = this.resizingModule.start.y; dy <= newY; dy++) {
        if (
          dx > settings.currentGrid.cellsX ||
          dy > settings.currentGrid.cellsY ||
          dx < 1 ||
          dy < 1 ||
          (settings.currentGrid.widgetsMap[dy][dx] != null &&
            settings.currentGrid.widgetsMap[dy][dx] != this.resizingModule.id)
        ) {
          return false
        }
      }
    }
    return true
  }

  onMouseMove(event: MouseEvent): void {
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
    }
  }
  onClick(event: MouseEvent): void {
    this.onMouseMove(event)
    this.resizingModule = null
  }
  onKey(event: KeyboardEvent): void {
    throw new Error("Method not implemented.")
  }
}
