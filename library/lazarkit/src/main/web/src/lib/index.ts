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
  const now = new Date(data.timestamp).getTime()
  const windowStart = now - info.timeWindow * 1000

  const filteredGraphs: Record<string, GraphPacket[]> = {}

  for (const [key, items] of Object.entries(data.graphs as Graph)) {
    const filteredItems = items.filter((entry: GraphPacket) => {
      const ts = new Date(entry.timestamp).getTime()
      return ts >= windowStart
    })

    const count = filteredItems.length
    if (count <= maxEntries) {
      filteredGraphs[key] = filteredItems
      continue
    }

    const step = (count - 1) / (maxEntries - 1)
    const limitedEntries: GraphPacket[] = []
    for (let i = 0; i < maxEntries; i++) {
      const index = Math.round(i * step)
      limitedEntries.push(filteredItems[index])
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

socket.addMessageHandler("initialJvmFields", (data: GenericData) => {
  const initialFields = data.fields

  const initialMap = new Map<string, string>()
  function addToInitialMap(field: GenericTypeJson) {
    initialMap.set(field.id, field.valueString)
  }
  forAll(initialFields, addToInitialMap, addToInitialMap)

  info.initialJvmFields = initialMap
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

socket.addMessageHandler("plugins", (data: GenericData) => {
  info.plugins = data.plugins
})

socket.addMessageHandler("pluginsUpdate", (data: GenericData) => {
  const updatedPlugins = data.plugins
  return

  for (const updated of updatedPlugins) {
    const index = info.plugins.findIndex((p) => p.id === updated.id)
    if (index !== -1) {
      info.plugins[index] = updated
    } else {
      info.plugins.push(updated)
    }
  }
})

export const gamepads = new GamepadManager()

export const notifications = new NotificationsManager()
