export class GamepadManager {
  gamepads = $state<Gamepad[]>([])
  current = $state<Gamepad | null>(null)

  lastTimestamp: number = 0

  updateGamepads() {
    this.gamepads = navigator.getGamepads
      ? (Array.from(navigator.getGamepads()).filter((g) => g) as Gamepad[])
      : []

    if (this.gamepads.length) {
      this.current = this.gamepads[0]
      if (this.lastTimestamp < this.current.timestamp) {
        console.log("Update: ", this.current)
        this.lastTimestamp = this.current.timestamp
      }
    } else {
      this.current = null
      //send empty
    }
  }

  getPS4ButtonLabel(index: number): string {
    const labels: Record<number, string> = {
      0: "✕",
      1: "◯",
      2: "◻",
      3: "△",
      4: "L1",
      5: "R1",
      6: "L2",
      7: "R2",
      8: "Share",
      9: "Options",
      10: "L3",
      11: "R3",
      12: "D-Pad Up",
      13: "D-Pad Down",
      14: "D-Pad Left",
      15: "D-Pad Right",
      16: "PS",
      17: "Touchpad",
    }
    return labels[index] || `B${index}`
  }
}
