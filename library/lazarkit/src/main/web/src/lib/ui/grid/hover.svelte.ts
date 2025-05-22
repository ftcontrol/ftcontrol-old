import { settings } from "$lib/settings.svelte"
import type { Module } from "./grid.svelte"

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
    this.moveMouse(event)
  }

  updateClick(event: MouseEvent) {
    this.stopMoving()

    this.stopResizing(event)

    this.stopMov(event)

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
  resizingModule: Module | null = $state(null)

  isResizing = $derived(this.resizingModule != null)

  startResizing(m: Module) {
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
        console.log("Got widget", el)
        let stringX = el.getAttribute("data-x")
        let stringY = el.getAttribute("data-y")

        if (stringX == null || stringY == null) return

        const x = parseInt(stringX)
        const y = parseInt(stringY)

        if (!this.canResize(x, y)) return
        console.log(x, y)
        this.resizingModule.sizes = {
          x: x - this.resizingModule.start.x + 1,
          y: y - this.resizingModule.start.y + 1,
        }
      }

      // [data-id="${this.resizingModule.id}"]
    }
  }

  // MOVING

  movingModule: Module | null = $state(null)

  isMov = $derived(this.movingModule != null)

  xTile: number | null = $state(null)
  yTile: number | null = $state(null)

  startMov(m: Module, x: number, y: number) {
    this.movingModule = m

    const element = document.querySelectorAll(`.widget[data-id="${m.id}"]`)[0]
    const box = element.getBoundingClientRect()

    this.xTile = Math.ceil(((x - box.x) / box.width) * m.sizes.x) - 1
    this.yTile = Math.ceil(((y - box.y) / box.height) * m.sizes.y) - 1

    console.log(this.xTile, this.yTile)
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
        console.log("Got widget", el)
        let stringX = el.getAttribute("data-x")
        let stringY = el.getAttribute("data-y")

        if (stringX == null || stringY == null) return
        if (this.xTile == null || this.yTile == null) return

        const x = parseInt(stringX)
        const y = parseInt(stringY)

        if (!this.canMov(x, y)) return
        console.log(x, y)
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
