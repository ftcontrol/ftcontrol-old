import { settings } from "$lib/settings.svelte"

class Hover {
  movingIndex: number | null = $state(null)
  movingID: string | null = $state(null)

  hoveringIndex: number | null = $state(null)
  hoveringID: string | null = $state(null)

  mouseX: number = $state(0)
  mouseY: number = $state(0)

  startX: number | null = $state(null)
  startY: number | null = $state(null)

  isMoving = $derived.by(() => {
    if (!this.startX || !this.startY) {
      return false
    }
    let deltaX = Math.abs(this.mouseX - this.startX)
    let deltaY = Math.abs(this.mouseY - this.startY)
    let distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY)
    if (distance > 64) {
      return true
    }
    return false
  })

  private moveBoundHandler!: (event: MouseEvent) => void
  private upBoundHandler!: (event: MouseEvent) => void

  stopMoving() {
    //perform move
    if (this.hoveringID && this.hoveringIndex) {
      const movingWidget = settings.currentGrid.modules.find(
        (it) => it.id == this.movingID
      )
      const hoveringWidget = settings.currentGrid.modules.find(
        (it) => it.id == this.hoveringID
      )
      if (movingWidget && hoveringWidget) {
        console.log("Performed move", movingWidget, hoveringWidget)
      }
    }
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
  }

  init() {
    this.moveBoundHandler = this.updateMouse.bind(this)
    this.upBoundHandler = this.updateClick.bind(this)
    window.addEventListener("mousemove", this.moveBoundHandler)
    window.addEventListener("mouseup", this.upBoundHandler)
  }

  updateMouse(event: MouseEvent) {
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
      //   console.log("Hovered element:", { id, index, el })
      if (id && index) {
        this.hoveringID = id
        this.hoveringIndex = index
        return
      }
    }
  }

  updateClick(event: MouseEvent) {
    this.stopMoving()
  }

  destroy() {
    window.removeEventListener("mousemove", this.moveBoundHandler)
    window.removeEventListener("mouseup", this.upBoundHandler)
  }
}

export let hover = new Hover()
