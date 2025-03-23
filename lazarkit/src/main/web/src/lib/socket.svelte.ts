export type Handler = (data: string[]) => void

export class SocketManager {
  private socket: WebSocket | null = null
  private url: string = `ws://${window.location.hostname}:8002`
  private isConnected: boolean = false
  private readonly messageHandlers: Record<string, Handler> = {}
  private reconnectInterval: number = 3000
  private messageQueue: string[] = []

  constructor() {
    this.messageQueue = []
    this.messageHandlers = {}
  }

  init(): Promise<void> {
    return new Promise((resolve, reject) => {
      if (this.socket) return

      this.socket = new WebSocket(this.url)

      this.socket.onopen = () => {
        this.isConnected = true
        console.log("Connected to WebSocket.")
        resolve()
        this.flushQueue()
      }

      this.socket.onmessage = (event) => {
        const data = event.data.split(".")

        if (data.length < 2) return

        const type = data.shift()
        if (type != "time")
          console.log({
            type,
            data,
          })
        this.handleMessage(type, data)
      }

      this.socket.onerror = (event) => {
        console.error("WebSocket error:", event)
        reject(event)
      }

      this.socket.onclose = () => {
        this.isConnected = false
        console.log("WebSocket closed. Attempting to reconnect...")
        setTimeout(() => this.reconnect(), this.reconnectInterval)
      }
    })
  }

  reconnect() {
    if (!this.isConnected) {
      console.log("Reconnecting...")
      this.init()
    }
  }

  sendMessage(type: string, data: string[] = []) {
    const message = data.length ? `${type}.${data.join(".")}` : type

    if (this.socket && this.socket.readyState === WebSocket.OPEN) {
      this.socket.send(message)
    } else {
      console.warn("WebSocket not open. Queuing message.")
      this.messageQueue.push(message)
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

  private handleMessage(type: string, data: string[]) {
    const handler = this.messageHandlers[type]
    if (handler) {
      handler(data)
    } else {
      console.warn(`No handler for message type: ${type}`)
    }
  }

  public addMessageHandler(type: string, handler: Handler) {
    this.messageHandlers[type] = handler
  }

  public removeMessageHandler(type: string) {
    delete this.messageHandlers[type]
  }
}

export type OpMode = {
  name: string
  group: string
  flavor: "AUTONOMOUS" | "TELEOP"
}

export class InfoManager {
  time = $state("")
  opModes = $state<OpMode[]>([])
  currentOpMode = $state("$Stop$Robot$")
  currentOpModeStatus = $state<"init" | "running" | "stopped">("stopped")
}
