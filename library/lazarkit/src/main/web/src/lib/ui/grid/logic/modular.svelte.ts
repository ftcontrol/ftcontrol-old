export abstract class GenericModularDependency {
  mouseX: number = $state(0)
  mouseY: number = $state(0)

  abstract onMouseMove(event: MouseEvent): void
  abstract onClick(event: MouseEvent): void
  abstract onKey(event: KeyboardEvent): void
}

class ModularManager {
  private dependencies: GenericModularDependency[]

  constructor(...dependencies: GenericModularDependency[]) {
    this.dependencies = dependencies
  }

  private moveBoundHandler!: (event: MouseEvent) => void
  private upBoundHandler!: (event: MouseEvent) => void
  private keyBoundHandler!: (event: KeyboardEvent) => void

  onMouseMove(event: MouseEvent) {
    for (const dep of this.dependencies) {
      dep.onMouseMove(event)
      dep.mouseX = event.clientX
      dep.mouseY = event.clientY
    }
  }

  onClick(event: MouseEvent) {
    for (const dep of this.dependencies) {
      dep.onMouseMove(event)
    }
  }

  onKey(event: KeyboardEvent) {
    for (const dep of this.dependencies) {
      dep.onKey(event)
    }
  }

  init() {
    this.moveBoundHandler = this.onMouseMove.bind(this)
    this.upBoundHandler = this.onClick.bind(this)
    this.keyBoundHandler = this.onKey.bind(this)
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

export let modular = new ModularManager()
