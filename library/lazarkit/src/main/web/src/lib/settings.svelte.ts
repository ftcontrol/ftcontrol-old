import { getCookie, setCookie } from "./cookies"

class Settings {
  isDark = $state(getCookie("theme") === "dark")

  constructor() {
    document.body.classList.toggle("dark-mode", this.isDark)
  }

  toggleIsDark() {
    this.isDark = !this.isDark
    document.body.classList.toggle("dark-mode", this.isDark)
    setCookie("theme", this.isDark ? "dark" : "light")
  }
}

export const settings = new Settings()
