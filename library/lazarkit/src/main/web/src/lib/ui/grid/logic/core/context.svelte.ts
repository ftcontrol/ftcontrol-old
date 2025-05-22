import { GenericModularDependency } from "../generic.svelte"

export class ContextMenuManager extends GenericModularDependency {
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

  onMouseMove(event: MouseEvent): void {}
  onClick(event: MouseEvent): void {
    const target = event.target as HTMLElement

    if (!(target.closest(".context-menu") || target.closest(".keepOpened"))) {
      this.closeContextMenu()
    }
  }
  onKey(event: KeyboardEvent): void {
    if (event.key === "Escape") this.closeContextMenu()
  }
}
