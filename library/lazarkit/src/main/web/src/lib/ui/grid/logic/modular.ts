import { ContextMenuManager } from "./core/context.svelte"
import { MovingManager } from "./core/moving.svelte"
import { ResizingManager } from "./core/resizing.svelte"
import { TabsManager } from "./core/tabs.svelte"
import type { GenericModularDependency } from "./generic.svelte"

class ModularManager {
  context = new ContextMenuManager()
  moving = new MovingManager()
  resizing = new ResizingManager()
  tabs = new TabsManager()

  private dependencies: GenericModularDependency[] = [
    this.context,
    this.moving,
    this.resizing,
    this.tabs,
  ]

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
      dep.onClick(event)
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

  showWidget(x: number, y: number) {
    return this.resizing.canResize(x, y) || this.moving.canMov(x, y)
  }
}

export let modular = new ModularManager()
