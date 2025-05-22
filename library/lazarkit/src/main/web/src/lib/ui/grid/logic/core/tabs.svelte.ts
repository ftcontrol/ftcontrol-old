import { settings } from "$lib/settings.svelte"
import { GenericModularDependency } from "../generic.svelte"
export enum TabsManagerTypes {
  WIDGETS = "widgets",
  PRESETS = "presets",
}
export class TabsManager extends GenericModularDependency {
  type: TabsManagerTypes
  constructor(type: TabsManagerTypes) {
    super()
    this.type = type
  }
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

  private moveItem<T>(
    fromList: T[],
    toList: T[],
    fromIndex: number,
    toIndex: number
  ): void {
    if (fromIndex < toIndex && fromList === toList) {
      toIndex -= 1
    }

    const [item] = fromList.splice(fromIndex, 1)
    toList.splice(toIndex, 0, item)
  }

  onClick(event: MouseEvent): void {
    if (this.hoveringID != null && this.hoveringIndex != null) {
      if (this.type == TabsManagerTypes.WIDGETS) {
        const movingWidget = settings.currentGrid.widgets.find(
          (it) => it.id == this.movingID
        )
        const hoveringWidget = settings.currentGrid.widgets.find(
          (it) => it.id == this.hoveringID
        )
        if (movingWidget && hoveringWidget && this.movingIndex != null) {
          const movingType = movingWidget.widgets[this.movingIndex]
          if (movingType) {
            this.moveItem(
              movingWidget.widgets,
              hoveringWidget.widgets,
              this.movingIndex,
              this.hoveringIndex
            )

            if (movingWidget.activeWidgetID == this.movingIndex) {
              if (movingWidget.activeWidgetID > 0) {
                movingWidget.activeWidgetID = movingWidget.activeWidgetID - 1
              } else {
                movingWidget.activeWidgetID = 0
              }
            }
          }
        }
      } else if (this.type == TabsManagerTypes.PRESETS) {
        if (this.movingIndex != null && this.hoveringIndex != null) {
          const presets = [...settings.presets]

          this.moveItem(presets, presets, this.movingIndex, this.hoveringIndex)

          settings.presets = presets
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
