import { getCookie, setCookie } from "./cookies"
type AnimationSpeed = "instant" | "fast" | "normal" | "slow"
type PrimaryColor = "blue" | "red"
class Settings {
  isDark = $state(getCookie("theme") === "dark")

  animationSpeed: AnimationSpeed = $state(
    (getCookie("animations") as AnimationSpeed) || "fast"
  )

  primaryColor: PrimaryColor = $state(
    (getCookie("primary") as PrimaryColor) || "blue"
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
}

export const settings = new Settings()
