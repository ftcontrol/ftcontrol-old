import { settings } from "$lib/settings.svelte"

class Hover {
  // TAB MOVING
  movingIndex: number | null = $state(null)
  movingID: string | null = $state(null)

  hoveringIndex: number | null = $state(null)
  hoveringID: string | null = $state(null)

  mouseX: number = $state(0)
  mouseY: number = $state(0)

  startX: number | null = $state(null)
  startY: number | null = $state(null)

  wasStartedMoving = $state(false)

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

  private moveBoundHandler!: (event: MouseEvent) => void
  private upBoundHandler!: (event: MouseEvent) => void
  private keyBoundHandler!: (event: KeyboardEvent) => void

  stopMoving() {
    if (this.hoveringID != null && this.hoveringIndex != null) {
      console.log("Try moving", this.hoveringID, this.hoveringIndex)
      console.log("Try moving", this.movingID, this.movingIndex)
      const movingWidget = settings.currentGrid.modules.find(
        (it) => it.id == this.movingID
      )
      const hoveringWidget = settings.currentGrid.modules.find(
        (it) => it.id == this.hoveringID
      )
      console.log("Found", movingWidget, hoveringWidget)
      if (movingWidget && hoveringWidget && this.movingIndex != null) {
        console.log("Got here")
        const movingType = movingWidget.types[this.movingIndex]
        if (movingType) {
          movingWidget.types.splice(this.movingIndex, 1)
          hoveringWidget.types.splice(this.hoveringIndex, 0, movingType)
          console.log(
            "Performed move",
            movingType,
            "from",
            movingWidget,
            "to",
            hoveringWidget
          )

          if (movingWidget.activeType == this.movingIndex) {
            if (movingWidget.activeType > 0) {
              movingWidget.activeType = movingWidget.activeType - 1
            } else {
              movingWidget.activeType = 0
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

  startMoving(x: number, y: number, index: number, id: string) {
    this.startX = x
    this.startY = y
    this.movingIndex = index
    this.movingID = id
    this.hoveringID = null
    this.hoveringIndex = null
    this.wasStartedMoving = false
  }

  private tabsMouse(event: MouseEvent) {
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
      console.log(indexString)
      if (!indexString) continue
      const index = parseInt(indexString)
      console.log("Hovered element:", { id, index, el })
      if (id != null && index != null) {
        this.hoveringID = id
        this.hoveringIndex = index
        return
      }
    }
  }

  updateMouse(event: MouseEvent) {
    this.tabsMouse(event)
    this.resizingMouse(event)
  }

  updateClick(event: MouseEvent) {
    this.stopMoving()

    this.stopResizing()

    // context

    const target = event.target as HTMLElement

    if (!(target.closest(".context-menu") || target.closest(".keepOpened"))) {
      this.closeContextMenu()
    }
  }

  updateKey(event: KeyboardEvent) {
    if (event.key === "Escape") this.closeContextMenu()
  }

  // CONTEXT MENU

  openedContextMenuID: string | null = $state(null)
  openedContextMenuIndex: number | null = $state(null)

  openContextMenu(id: string, index: number) {
    this.openedContextMenuID = id
    this.openedContextMenuIndex = index
  }

  closeContextMenu() {
    this.openedContextMenuID = null
    this.openedContextMenuIndex = null
  }

  isContextOpened(id: string, index: number) {
    return (
      this.openedContextMenuID == id && this.openedContextMenuIndex == index
    )
  }

  // RESIZING
  resizingID: string | null = $state(null)

  rstartX: number | null = $state(null)
  rstartY: number | null = $state(null)

  startResizing(id: string, x: number, y: number) {
    this.resizingID = id
    this.rstartX = x
    this.rstartY = y
  }

  stopResizing() {
    if (
      this.resizingID != null &&
      this.rstartX != null &&
      this.rstartY != null
    ) {
      const deltaX = this.mouseX - this.rstartX
      const deltaY = this.mouseY - this.rstartY
      console.log(deltaX, deltaY)

      const widgetElement = document.querySelector(
        `.widget[data-id="${this.resizingID}"]`
      )

      if (widgetElement) {
        console.log("Got widget", widgetElement)
      }
    }
    this.resizingID = null
    this.rstartX = null
    this.rstartY = null
  }

  private resizingMouse(event: MouseEvent) {
    if (
      this.resizingID != null &&
      this.rstartX != null &&
      this.rstartY != null
    ) {
      const deltaX = this.mouseX - this.rstartX
      const deltaY = this.mouseY - this.rstartY
      console.log(deltaX, deltaY)

      const widgetElement = document.querySelector(
        `.widget[data-id="${this.resizingID}"]`
      )

      if (widgetElement) {
        console.log("Got widget", widgetElement)
      }
    }
  }

  init() {
    this.moveBoundHandler = this.updateMouse.bind(this)
    this.upBoundHandler = this.updateClick.bind(this)
    this.keyBoundHandler = this.updateKey.bind(this)
    window.addEventListener("mousemove", this.moveBoundHandler)
    window.addEventListener("mouseup", this.upBoundHandler)
    window.addEventListener("keydown", this.keyBoundHandler)
  }
  destroy() {
    window.removeEventListener("mousemove", this.moveBoundHandler)
    window.removeEventListener("mouseup", this.upBoundHandler)
    window.removeEventListener("keydown", this.keyBoundHandler)
  }
}

export let hover = new Hover()
