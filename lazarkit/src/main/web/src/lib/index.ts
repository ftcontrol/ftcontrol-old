import { GamepadManager } from "./gamepad.svelte"
import { InfoManager, SocketManager, type OpMode } from "./socket.svelte"
import { NotificationsManager } from "./notifications.svelte"

export const socket = new SocketManager()

socket.addMessageHandler("time", (data: string[]) => {
  info.time = data[0]
})
socket.addMessageHandler("opmodes", (data: string[]) => {
  var answer: OpMode[] = []

  for (let i = 0; i < data.length; i += 3) {
    if (i + 2 < data.length) {
      answer.push({
        name: data[i],
        group: data[i + 1],
        flavor: data[i + 2] as OpMode["flavor"],
      })
    }
  }

  info.opModes = answer
})
socket.addMessageHandler("current_opmode", (data: string[]) => {
  info.currentOpMode = data[0]
  info.currentOpModeStatus = data[1] as "init" | "running" | "stopped"
})

export const info = new InfoManager()

setTimeout(() => {
  socket.sendMessage("get_opmodes")
  socket.sendMessage("get_current_opmode")
}, 1000)

export const gamepads = new GamepadManager()

export const notifications = new NotificationsManager()

// TODO: on reconnect reset states
