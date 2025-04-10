import { getCookie, setCookie } from "./cookies"
type AnimationSpeed = "instant" | "fast" | "normal" | "slow"
class Settings {
  isDark = $state(getCookie("theme") === "dark")

  animationSpeed: AnimationSpeed = $state(
    (getCookie("animations") as AnimationSpeed) || "fast"
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
}

export const settings = new Settings()
