export abstract class GenericModularDependency {
  mouseX: number = $state(0)
  mouseY: number = $state(0)

  abstract onMouseMove(event: MouseEvent): void
  abstract onClick(event: MouseEvent): void
  abstract onKey(event: KeyboardEvent): void
}
