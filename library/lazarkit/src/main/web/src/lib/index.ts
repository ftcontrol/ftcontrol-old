import { GamepadManager } from "./gamepad.svelte"
import {
  InfoManager,
  SocketManager,
  type GenericData,
  type OpMode,
  type TelemetryPacket,
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
  info.graphs = data.graphs
  if (!info.isPlaying) {
    info.telemetry = data.lines
    info.canvas = data.canvas
  }
  if (info.isRecording) {
    info.history.push({
      canvas: data.canvas,
      lines: data.lines,
      timestamp: data.timestamp,
    } as TelemetryPacket)
  }
})

socket.addMessageHandler("jvmFields", (data: GenericData) => {
  info.jvmFields = data.fields

  const process = (field: GenericTypeJson) => {
    field.isShown = true
  }

  forAll(info.jvmFields, process, process)
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
