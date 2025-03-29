import { GamepadManager } from "./gamepad.svelte"
import {
  InfoManager,
  SocketManager,
  type GenericData,
  type OpMode,
} from "./socket.svelte"
import { NotificationsManager } from "./notifications.svelte"
import type { GenericTypeJson } from "./genericType"

export const socket = new SocketManager()

socket.addMessageHandler("time", (data: GenericData) => {
  info.time = data.time
})
socket.addMessageHandler("opmodes", (data: GenericData) => {
  info.opModes = data.opModes
})
socket.addMessageHandler("activeOpMode", (data: GenericData) => {
  info.activeOpMode = data.opMode.name
  info.activeOpModeStatus = data.status as "init" | "running" | "stopped"
})

export const info = new InfoManager()

socket.addMessageHandler("telemetryPacket", (data: GenericData) => {
  info.telemetry = data.lines
})

socket.addMessageHandler("jvmFields", (data: GenericData) => {
  info.jvmFields = data.fields
  for (const field of info.jvmFields) {
    field.newValueString = field.valueString
    field.isValid = true
  }
})

socket.addMessageHandler("updatedJvmFields", (data: GenericData) => {
  for (const newField of data.fields as GenericTypeJson[]) {
    for (const oldField of info.jvmFields) {
      if (newField.className == oldField.className) {
        if (newField.fieldName == oldField.fieldName) {
          oldField.valueString = newField.valueString
          oldField.newValueString = oldField.valueString
          oldField.isValid = true
        }
      }
    }
  }
})

// setTimeout(() => {
//   socket.sendMessage({ kind: "getOpmodes" })
//   socket.sendMessage({ kind: "getActiveOpMode" })
//   socket.sendMessage({ kind: "getJvmFieldsRequest" })
// }, 1500)

export const gamepads = new GamepadManager()

export const notifications = new NotificationsManager()

// TODO: on reconnect reset states
