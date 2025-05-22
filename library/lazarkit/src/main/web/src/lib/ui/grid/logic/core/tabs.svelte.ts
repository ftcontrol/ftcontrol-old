import { settings } from "$lib/settings.svelte"
import { GenericModularDependency } from "../modular.svelte"

export class TabsManager extends GenericModularDependency {
  movingIndex: number | null = $state(null)
  movingID: string | null = $state(null)

  hoveringIndex: number | null = $state(null)
  hoveringID: string | null = $state(null)

  mouseX: number = $state(0)
  mouseY: number = $state(0)

  startX: number | null = $state(null)
  startY: number | null = $state(null)

  wasStartedMoving = $state(false)
  isMoving = $derived.by(() => {
    if (!this.startX || !this.startY) {
      return false
    }
    if (this.wasStartedMoving) return true
    let deltaX = Math.abs(this.mouseX - this.startX)
    let deltaY = Math.abs(this.mouseY - this.startY)
    let distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY)
    if (distance > 32) {
      this.wasStartedMoving = true
      return true
    }
    return false
  })

  showExtra(id: string, index: number) {
    if (!this.isMoving) return false

    if (this.movingID != id) return true
    if (this.movingIndex == index) return false
    if (this.movingIndex == index - 1) return false

    if (this.movingIndex != index) return true

    return false
  }

  showLabel(id: string, index: number) {
    if (!this.isMoving) return true
    if (this.movingID != id) return true
    if (this.movingIndex != index) return true
    return false
  }

  startMoving(x: number, y: number, index: number, id: string) {
    this.startX = x
    this.startY = y
    this.movingIndex = index
    this.movingID = id
    this.hoveringID = null
    this.hoveringIndex = null
    this.wasStartedMoving = false
  }

  onMouseMove(event: MouseEvent): void {
    this.hoveringID = null
    this.hoveringIndex = null

    this.mouseX = event.clientX
    this.mouseY = event.clientY
    if (!this.isMoving) return

    const elements = document.elementsFromPoint(event.clientX, event.clientY)
    const matchingElements = elements.filter(
      (el) =>
        el instanceof HTMLElement &&
        el.hasAttribute("data-id") &&
        el.hasAttribute("data-index")
    )

    for (const el of matchingElements) {
      const id = el.getAttribute("data-id")
      const indexString = el.getAttribute("data-index")
      if (!indexString) continue
      const index = parseInt(indexString)
      if (id != null && index != null) {
        this.hoveringID = id
        this.hoveringIndex = index
        return
      }
    }
  }
  onClick(event: MouseEvent): void {
    if (this.hoveringID != null && this.hoveringIndex != null) {
      const movingWidget = settings.currentGrid.widgets.find(
        (it) => it.id == this.movingID
      )
      const hoveringWidget = settings.currentGrid.widgets.find(
        (it) => it.id == this.hoveringID
      )
      if (movingWidget && hoveringWidget && this.movingIndex != null) {
        const movingType = movingWidget.widgets[this.movingIndex]
        if (movingType) {
          movingWidget.widgets.splice(this.movingIndex, 1)
          hoveringWidget.widgets.splice(this.hoveringIndex, 0, movingType)

          if (movingWidget.activeWidgetID == this.movingIndex) {
            if (movingWidget.activeWidgetID > 0) {
              movingWidget.activeWidgetID = movingWidget.activeWidgetID - 1
            } else {
              movingWidget.activeWidgetID = 0
            }
          }
        }
      }
    }
    this.wasStartedMoving = false
    this.startX = null
    this.startY = null
    this.movingIndex = null
    this.movingID = null
    this.hoveringID = null
    this.hoveringIndex = null
  }
  onKey(event: KeyboardEvent): void {}
}
