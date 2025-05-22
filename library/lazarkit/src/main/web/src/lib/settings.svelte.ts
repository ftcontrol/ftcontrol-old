import { info, notifications } from "$lib"
import { PresetManager } from "$ui/grid/logic/preset.svelte"
import type { Preset } from "$ui/grid/logic/types"
import { deleteCookie, getCookie, setCookie } from "./cookies"
type AnimationSpeed = "instant" | "fast" | "normal" | "slow"
type PrimaryColor = "blue" | "red"
type FieldOrientation = "0deg" | "90deg" | "180deg" | "270deg"
type ShowFieldCoordinates = "true" | "false"
class Settings {
  isDark = $state(getCookie("theme") === "dark")

  animationSpeed: AnimationSpeed = $state(
    (getCookie("animations") as AnimationSpeed) || "fast"
  )

  primaryColor: PrimaryColor = $state(
    (getCookie("primary") as PrimaryColor) || "blue"
  )

  fieldOrientation: FieldOrientation = $state(
    (getCookie("fieldOrientation") as FieldOrientation) || "0deg"
  )

  fieldShowCoordinates: ShowFieldCoordinates = $state(
    (getCookie("fieldShowCoordinates") as ShowFieldCoordinates) || "false"
  )

  constructor() {
    document.body.classList.toggle("dark-mode", this.isDark)
    document.body.classList.add(this.animationSpeed)
  }

  toggleIsDark() {
    this.isDark = !this.isDark
    document.body.classList.toggle("dark-mode", this.isDark)
    setCookie("theme", this.isDark ? "dark" : "light")
  }

  setSpeed(speed: AnimationSpeed) {
    document.body.classList.remove(this.animationSpeed)
    this.animationSpeed = speed

    document.body.classList.add(this.animationSpeed)
    setCookie("animations", speed)
  }

  setPrimaryColor(color: PrimaryColor) {
    document.body.classList.remove(this.primaryColor)
    this.primaryColor = color

    document.body.classList.add(this.primaryColor)
    setCookie("primary", this.primaryColor)
  }

  setFieldOrientation(newOrientation: FieldOrientation) {
    this.fieldOrientation = newOrientation
    setCookie("fieldOrientation", newOrientation)
  }

  setFieldShowCoordinates(newState: ShowFieldCoordinates) {
    this.fieldShowCoordinates = newState
    setCookie("fieldShowCoordinates", newState)
  }

  getInitialPresetManagers(): PresetManager[] {
    const raw = getCookie("presets")
    const parsed = raw ? JSON.parse(raw) : [null]

    const grids: PresetManager[] = []
    for (const [key, value] of Object.entries(parsed)) {
      grids.push(PresetManager.fromJSON(value as Preset))
    }

    if (grids.length < 1) {
      return [new PresetManager(null)]
    }

    return grids
  }

  resetPresets() {
    this.selectedManagerID = "default"
    deleteCookie("presets")
    this.presets = [new PresetManager(null)]
    this.initialPresets = [new PresetManager(null)]
    this.hasPresets = false
    info.showEdit = false
  }

  removePreset(id: string) {
    if (this.presets.length == 1) {
      notifications.add("Cannot remove last preset.")
      return
    }
    this.presets = this.presets.filter((it) => it.id != id)
  }

  hasPresets = $state(getCookie("presets") ? true : false)

  selectedManagerID = $state("default")

  initialPresets = $state(this.getInitialPresetManagers())

  presets: PresetManager[] = $state(this.getInitialPresetManagers())

  isGridEdited = $derived(
    JSON.stringify(this.initialPresets) != JSON.stringify(this.presets)
  )

  getPresetById(id: string): PresetManager | null {
    for (const grid of this.presets) {
      if (grid.id == id) return grid
    }
    return null
  }

  currentGrid = $derived.by(() => {
    const grid = this.getPresetById(this.selectedManagerID)
    if (grid != null) return grid
    this.selectedManagerID = this.presets[0].id
    return this.presets[0]
  })

  savePresets() {
    const serialized = this.presets.map((it) => it.toJSON())

    setCookie("presets", JSON.stringify(serialized))
    this.hasPresets = true
    this.initialPresets = this.presets
  }

  allIDs: string[] = $derived(this.presets.map((it) => it.id))
}

export const settings = new Settings()
