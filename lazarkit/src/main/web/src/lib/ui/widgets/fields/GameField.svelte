<script lang="ts">
  import { data } from "$lib/canvas"
  import { Section } from "$primitives"
  import { onMount } from "svelte"

  let canvas: HTMLCanvasElement

  class Distance {
    inches: number
    pixels: number
    constructor(inches: number, pixels?: number) {
      this.inches = inches
      if (pixels !== undefined) {
        this.pixels = pixels
      } else {
        const pixelsPerInch = FIELD_WIDTH.pixels / FIELD_WIDTH.inches
        this.pixels = this.inches * pixelsPerInch
      }
    }
  }
  const FIELD_WIDTH = new Distance(24 * 6, 1024)
  const FIELD_HEIGHT = new Distance(24 * 6, 1024)
  class Point {
    fieldX: Distance
    fieldY: Distance

    x: number
    y: number
    constructor(fieldX: number, fieldY: number) {
      this.fieldX = new Distance(fieldX)
      this.fieldY = new Distance(fieldY)
      const pixelsPerInch = FIELD_WIDTH.pixels / FIELD_WIDTH.inches
      this.x = this.fieldX.inches * pixelsPerInch
      this.y = this.fieldY.inches * pixelsPerInch
    }
  }

  onMount(async () => {
    if (!canvas) return

    const dpr = window.devicePixelRatio || 1
    const width = FIELD_WIDTH.pixels * dpr
    const height = FIELD_HEIGHT.pixels * dpr

    canvas.width = width
    canvas.height = height

    const ctx = canvas.getContext("2d")
    if (ctx) {
      ctx.scale(dpr, dpr)
    }

    if (ctx) {
      ctx.translate(FIELD_WIDTH.pixels / 2, FIELD_HEIGHT.pixels / 2)
    }

    function moveTo(p: Point) {
      if (ctx == null) return
      ctx.moveTo(p.x, p.y)
    }

    function drawLine(
      start: Point,
      end: Point,
      color: string = "red",
      lineWidth: Distance = new Distance(0.03)
    ) {
      if (ctx == null) return
      ctx.beginPath()
      moveTo(start)
      ctx.lineTo(end.x, end.y)
      ctx.strokeStyle = color
      ctx.lineWidth = lineWidth.pixels
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
      cellSize: Distance,
      color: string = "#333",
      textColor: string = "#fff"
    ) {
      if (ctx == null) return
      ctx.strokeStyle = color
      ctx.lineWidth = 1
      ctx.fillStyle = textColor
      ctx.font = "16px Arial"

      const halfWidth = new Distance(FIELD_WIDTH.inches / 2)
      const halfHeight = new Distance(FIELD_HEIGHT.inches / 2)

      for (
        let x = -halfWidth.inches;
        x <= halfWidth.inches;
        x += cellSize.inches
      ) {
        drawLine(
          new Point(x, -halfHeight.inches),
          new Point(x, halfHeight.inches),
          color,
          new Distance(0.01)
        )

        for (
          let y = -halfHeight.inches;
          y <= halfHeight.inches;
          y += cellSize.inches
        ) {
          const p = new Point(x, y)
          drawPoint(p, "white", 2)
          ctx.fillText(`(${x}", ${y}")`, p.x + 5, -p.y + 5)
        }
      }

      for (
        let y = -halfHeight.inches;
        y <= halfHeight.inches;
        y += cellSize.inches
      ) {
        drawLine(
          new Point(-halfWidth.inches, y),
          new Point(halfWidth.inches, y),
          color,
          new Distance(0.01)
        )
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
      ctx.drawImage(img, start.x, start.y, width.pixels, height.pixels)
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

    drawLine(new Point(0, 0), new Point(24, 24), "red", new Distance(1))
    drawGrid(new Distance(24))
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
