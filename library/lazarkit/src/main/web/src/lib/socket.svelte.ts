import { type Canvas, emptyCanvas } from "$ui/widgets/fields/canvas"
import type { GenericTypeJson } from "./genericType"

export type SocketState = "closed" | "opened"

export type Handler = (data: GenericData) => void
export type GenericData = { kind: string; [key: string]: any }
export class SocketManager {
  private socket: WebSocket | null = null
  private url: string = `ws://${window.location.hostname}:8002`
  private readonly messageHandlers: Record<string, Handler> = {}
  private reconnectInterval: number = 1000
  private messageQueue: string[] = []

  state: SocketState = $state("closed")

  constructor() {
    this.messageQueue = []
    this.messageHandlers = {}
  }

  init(): Promise<void> {
    console.log("ran init")
    return new Promise((resolve, reject) => {
      if (this.socket) return

      this.socket = new WebSocket(this.url)

      this.socket.onopen = () => {
        this.state = "opened"
        console.log("Connected to WebSocket.")
        resolve()
        this.flushQueue()
      }

      this.socket.onmessage = (event) => {
        const data = JSON.parse(event.data)
        const kind = data.kind
        if (kind != "time") console.log(data)
        this.handleMessage(kind, data)
      }

      this.socket.onerror = (event) => {
        console.error("WebSocket error:", event)
        reject(event)
      }

      this.socket.onclose = () => {
        this.state = "closed"
        console.log("WebSocket closed. Attempting to reconnect...")
        setTimeout(async () => await this.reconnect(), this.reconnectInterval)
      }
    })
  }

  async reconnect() {
    if (this.state == "closed") {
      console.log("Reconnecting...")
      this.socket = null
      await this.init()
    }
  }

  sendMessage(message: GenericData) {
    if (this.socket && this.socket.readyState === WebSocket.OPEN) {
      this.socket.send(JSON.stringify(message))
    } else {
      console.warn("WebSocket not open. Queuing message.")
      this.messageQueue.push(JSON.stringify(message))
    }
  }

  private flushQueue() {
    while (
      this.messageQueue.length > 0 &&
      this.socket?.readyState === WebSocket.OPEN
    ) {
      this.socket.send(this.messageQueue.shift()!)
    }
  }

  private handleMessage(kind: string, data: GenericData) {
    const handler = this.messageHandlers[kind]
    if (handler) {
      handler(data)
    } else {
      console.warn(`No handler for message kind: ${kind}`)
    }
  }

  public addMessageHandler(kind: string, handler: Handler) {
    this.messageHandlers[kind] = handler
  }

  public removeMessageHandler(kind: string) {
    delete this.messageHandlers[kind]
  }
}

export type OpMode = {
  name: string
  group: string
  flavour: "AUTONOMOUS" | "TELEOP"
}

export type GraphPacket = {
  data: number
  timestamp: number
}

export type Graph = { [key: string]: GraphPacket[] }

export type TelemetryPacket = {
  lines: string[]
  canvas: Canvas
  timestamp: number
  graphs: Graph
}

export class InfoManager {
  showSettings = $state(false)

  time = $state("")
  opModes = $state<OpMode[]>([])
  activeOpMode = $state("$Stop$Robot$")
  activeOpModeStatus = $state<"init" | "running" | "stopped">("stopped")

  telemetry = $state<string[]>([])
  canvas = $state<Canvas>(emptyCanvas)
  graphs = $state<Graph>({})
  timeWindow = $state(2)
  isRecording = $state(false)
  isPlaying = $state(false)
  isForwarding = $state(false)
  history: TelemetryPacket[] = $state([])

  hasRecording = $derived<boolean>(this.history.length > 0)
  startTimestamp = $derived<number>(this.history.at(0)?.timestamp || 0)
  endTimestamp = $derived<number>(
    this.history.at(this.history.length - 1)?.timestamp || 0
  )
  duration = $derived(this.endTimestamp - this.startTimestamp)
  timestamp = $state(0)
  entry: TelemetryPacket | null = $derived.by(() => {
    var answerIndex = 0
    var index = 0
    for (const entry of this.history) {
      if (entry.timestamp <= this.timestamp + this.startTimestamp) {
        answerIndex = index
      } else {
        return this.history.at(answerIndex) as TelemetryPacket
      }
      index++
    }

    return this.history.at(this.history.length - 1) || null
  })

  loop() {
    if (this.isPlaying && this.entry != null) {
      this.telemetry = this.entry.lines
      this.canvas = this.entry.canvas
      this.graphs = this.entry.graphs
    }
  }

  jvmFields = $state<GenericTypeJson[]>([])
  initialJvmFields = $state<Map<string, string>>(new Map())
  openedStates: { [key: string]: boolean } = $state({})
  searchParam = $state("")

  batteryVoltage = $state<number>(-1.0)
  flows = $state([])
}
