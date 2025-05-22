class Hover {
  movingIndex: number | null = $state(null)
  movingID: string | null = $state(null)

  mouseX: number = $state(0)
  mouseY: number = $state(0)

  private boundHandler!: (event: MouseEvent) => void

  init() {
    this.boundHandler = this.updateMouse.bind(this)
    window.addEventListener("mousemove", this.boundHandler)
  }

  updateMouse(event: MouseEvent) {
    this.mouseX = event.clientX
    this.mouseY = event.clientY
  }

  destroy() {
    window.removeEventListener("mousemove", this.boundHandler)
  }
}

export let hover = new Hover()
