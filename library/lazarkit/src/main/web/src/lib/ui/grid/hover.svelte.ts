class Hover {
  movingIndex: number | null = $state(null)
  movingID: string | null = $state(null)

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

  startMoving(x: number, y: number, index: number, id: string) {
    this.startX = x
    this.startY = y
    this.movingIndex = index
    this.movingID = id
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
  }

  updateClick(event: MouseEvent) {}

  destroy() {
    window.removeEventListener("mousemove", this.moveBoundHandler)
    window.removeEventListener("mouseup", this.upBoundHandler)
  }
}

export let hover = new Hover()
