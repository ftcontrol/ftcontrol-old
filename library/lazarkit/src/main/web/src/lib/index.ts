import { GamepadManager } from "./gamepad.svelte"
import {
  InfoManager,
  SocketManager,
  type GenericData,
  type OpMode,
} from "./socket.svelte"
import { NotificationsManager } from "./notifications.svelte"
import {
  Types,
  type ChangeJson,
  type CustomTypeJson,
  type GenericTypeJson,
} from "./genericType"
import { forAll } from "$ui/widgets/configurables/utils"

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
  info.canvas = data.canvas
})

socket.addMessageHandler("jvmFields", (data: GenericData) => {
  info.jvmFields = data.fields

  function process(fields: GenericTypeJson[]) {
    if (fields == null) return
    for (const f of fields) {
      if (
        f.type == Types.CUSTOM ||
        f.type == Types.ARRAY ||
        f.type == Types.MAP ||
        f.type == Types.LIST
      ) {
        process(f.customValues)
      } else {
        f.isShown = true
      }
    }
  }

  process(info.jvmFields)
})

socket.addMessageHandler("updatedJvmFields", (data: GenericData) => {
  forAll(
    info.jvmFields,
    (f) => {
      for (const u of data.fields as ChangeJson[]) {
        if (f.id == u.id) {
          f.valueString = u.newValueString
          f.valueString = u.newValueString
          f.value = u.newValueString
          f.isValid = true
        }
      }
    },
    (f) => {}
  )
})

socket.addMessageHandler("batteryVoltage", (data: GenericData) => {
  info.batteryVoltage = data.value
})

socket.addMessageHandler("allFlows", (data: GenericData) => {
  info.flows = data.flows
})

// setTimeout(() => {
//   socket.sendMessage({ kind: "getOpmodes" })
//   socket.sendMessage({ kind: "getActiveOpMode" })
//   socket.sendMessage({ kind: "getJvmFieldsRequest" })
// }, 1500)

export const gamepads = new GamepadManager()

export const notifications = new NotificationsManager()

// TODO: on reconnect reset states
