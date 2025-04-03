<script lang="ts">
  import { data } from "$lib/canvas"
  import { Section } from "$primitives"
  import { onMount } from "svelte"

  let canvas: HTMLCanvasElement
  const FIELD_WIDTH = 24 * 6
  const FIELD_HEIGHT = 24 * 6

  class Distance {
    fieldDistance: number
    distance: number
    constructor(fieldDistance: number) {
      this.fieldDistance = fieldDistance
      const pixelsPerInch = 1024 / FIELD_WIDTH
      this.distance = this.fieldDistance * pixelsPerInch
    }
  }

  class Point {
    fieldX: number
    fieldY: number

    x: number
    y: number
    constructor(fieldX: number, fieldY: number) {
      this.fieldX = fieldX
      this.fieldY = fieldY
      const pixelsPerInch = 1024 / FIELD_WIDTH
      this.x = this.fieldX * pixelsPerInch
      this.y = this.fieldY * pixelsPerInch
    }
  }

  onMount(async () => {
    if (!canvas) return

    const dpr = window.devicePixelRatio || 1
    const width = 1024 * dpr
    const height = 1024 * dpr

    canvas.width = width
    canvas.height = height

    const ctx = canvas.getContext("2d")
    if (ctx) {
      ctx.scale(dpr, dpr)
    }

    if (ctx) {
      ctx.translate(1024 / 2, 1024 / 2)
    }

    function moveTo(p: Point) {
      if (ctx == null) return
      ctx.moveTo(p.x, p.y)
    }

    function drawLine(
      start: Point,
      end: Point,
      color: string = "red",
      lineWidth: number = 3
    ) {
      if (ctx == null) return
      ctx.beginPath()
      moveTo(start)
      ctx.lineTo(end.x, end.y)
      ctx.strokeStyle = color
      ctx.lineWidth = lineWidth
      ctx.stroke()
      ctx.closePath()
    }

    function drawPoint(p: Point, color: string = "blue", radius: number = 3) {
      if (ctx == null) return
      ctx.beginPath()
      ctx.arc(p.x, p.y, radius, 0, 2 * Math.PI)
      ctx.fillStyle = color
      ctx.fill()
      ctx.closePath()
    }

    function drawGrid(
      cellSize: number,
      color: string = "#333",
      textColor: string = "#fff"
    ) {
      if (ctx == null) return
      ctx.strokeStyle = color
      ctx.lineWidth = 1
      ctx.fillStyle = textColor
      ctx.font = "16px Arial"

      const halfWidth = FIELD_WIDTH / 2
      const halfHeight = FIELD_HEIGHT / 2

      for (let x = -halfWidth; x <= halfWidth; x += cellSize) {
        drawLine(new Point(x, -halfHeight), new Point(x, halfHeight), color, 1)

        for (let y = -halfHeight; y <= halfHeight; y += cellSize) {
          const p = new Point(x, y)
          drawPoint(p, "white", 2)
          ctx.fillText(`(${x}, ${y})`, p.x + 5, -p.y + 5)
        }
      }

      for (let y = -halfHeight; y <= halfHeight; y += cellSize) {
        drawLine(new Point(-halfWidth, y), new Point(halfWidth, y), color, 1)
      }
    }
    async function imageToBase64(url: string): Promise<string> {
      const response = await fetch(url)
      const blob = await response.blob()
      return new Promise((resolve, reject) => {
        const reader = new FileReader()
        reader.onloadend = () => resolve(reader.result as string)
        reader.onerror = reject
        reader.readAsDataURL(blob)
      })
    }
    async function drawBase64Image(
      base64: string,
      start: Point,
      width: Distance,
      height: Distance
    ) {
      if (ctx == null) return
      const img = new Image()
      img.src = base64
      await img.decode()
      ctx.save()
      ctx.drawImage(img, start.x, start.y, width.distance, height.distance)
      ctx.restore()
    }

    const imageUrl = "/fields/field.png"
    const base64Image = await imageToBase64(imageUrl)

    await drawBase64Image(
      base64Image,
      new Point(-72, -72),
      new Distance(24 * 6),
      new Distance(24 * 6)
    )

    drawLine(new Point(0, 0), new Point(24, 24), "red", 3)
    drawGrid(24)
    drawPoint(new Point(0.0, 0.0))
  })
</script>

<Section title={"Field"}>
  <canvas bind:this={canvas} style="width: 1024px; height: 1024px;"></canvas>
  {JSON.stringify(data)}
</Section>

<style>
  canvas {
    background-color: black;
  }
</style>
