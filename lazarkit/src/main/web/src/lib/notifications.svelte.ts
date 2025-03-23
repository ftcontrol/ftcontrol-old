type Notification = {
  timestamp: number
  text: string
}
export class NotificationsManager {
  data = $state<Notification[]>([])

  add(text: string) {
    this.data.push({
      timestamp: Date.now(),
      text,
    })
  }
}
