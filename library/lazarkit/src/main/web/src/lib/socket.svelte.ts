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

export class InfoManager {
  time = $state("")
  opModes = $state<OpMode[]>([])
  activeOpMode = $state("$Stop$Robot$")
  activeOpModeStatus = $state<"init" | "running" | "stopped">("stopped")
  telemetry = $state<string[]>([])

  jvmFields = $state<GenericTypeJson[]>([])
  openedStates: { [key: string]: boolean } = $state({})
  searchParam = $state("")

  batteryVoltage = $state<number>(-1.0)
  canvas = $state<Canvas>(emptyCanvas)
  flows = $state([])
}
