import { GamepadManager } from "./gamepad.svelte"
import {
  InfoManager,
  SocketManager,
  type GenericData,
  type GraphPacket,
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
import type { Graph } from "./socket.svelte"

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
  const maxEntries = 1200
  const maxPerSecond = Math.round(maxEntries / info.timeWindow)
  const now = new Date(data.timestamp).getTime()
  const windowStart = now - info.timeWindow * 1000

  const filteredGraphs: Record<string, GraphPacket[]> = {}

  for (const [key, items] of Object.entries(data.graphs as Graph)) {
    const filteredItems = items.filter((entry: GraphPacket) => {
      const ts = new Date(entry.timestamp).getTime()
      return ts >= windowStart
    })

    const groupedBySecond: Record<string, GraphPacket[]> = {}
    for (const entry of filteredItems) {
      const ts = new Date(entry.timestamp).getTime()
      const secondKey = Math.floor(ts / 1000)

      if (!groupedBySecond[secondKey]) {
        groupedBySecond[secondKey] = []
      }
      groupedBySecond[secondKey].push(entry)
    }

    const limitedEntries: GraphPacket[] = []
    for (const secondKey in groupedBySecond) {
      const entriesForSecond = groupedBySecond[secondKey]
      const count = entriesForSecond.length

      if (count <= maxPerSecond) {
        limitedEntries.push(...entriesForSecond)
      } else {
        const step = (count - 1) / (maxPerSecond - 1)
        for (let i = 0; i < maxPerSecond; i++) {
          const index = Math.round(i * step)
          limitedEntries.push(entriesForSecond[index])
        }
      }
    }

    filteredGraphs[key] = limitedEntries
  }

  if (!info.isPlaying) {
    info.telemetry = data.lines
    info.canvas = data.canvas
    info.graphs = filteredGraphs
  }
  if (info.isRecording) {
    info.history.push({
      canvas: data.canvas,
      lines: data.lines,
      graphs: filteredGraphs,
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
