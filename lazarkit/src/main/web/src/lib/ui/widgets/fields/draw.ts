import { Distance, FIELD_HEIGHT, FIELD_WIDTH, Point } from "./primitives"

export let ctx: CanvasRenderingContext2D | null

export function init(canvas: HTMLCanvasElement) {
  if (!canvas) return

  const dpr = window.devicePixelRatio || 1
  const width = FIELD_WIDTH.pixels * dpr
  const height = FIELD_HEIGHT.pixels * dpr

  canvas.width = width
  canvas.height = height

  ctx = canvas.getContext("2d")
  if (ctx) {
    ctx.scale(dpr, dpr)
  }

  if (ctx) {
    ctx.translate(FIELD_WIDTH.pixels / 2, FIELD_HEIGHT.pixels / 2)
  }
}

export function moveTo(p: Point) {
  if (ctx == null) return
  ctx.moveTo(p.x, p.y)
}

export function drawLine(
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

export function drawPoint(
  p: Point,
  color: string = "blue",
  radius: number = 3
) {
  if (ctx == null) return
  ctx.beginPath()
  ctx.arc(p.x, p.y, radius, 0, 2 * Math.PI)
  ctx.fillStyle = color
  ctx.fill()
  ctx.closePath()
}

export function setFontSizeInInches(
  ctx: CanvasRenderingContext2D,
  sizeInInches: number
) {
  const pixelsPerInch = FIELD_WIDTH.pixels / FIELD_WIDTH.inches
  const fontSizeInPixels = sizeInInches * pixelsPerInch
  ctx.font = `${fontSizeInPixels}px Arial`
}

export function drawGrid(
  cellSize: Distance,
  color: string = "#333",
  textColor: string = "#fff"
) {
  if (ctx == null) return
  ctx.strokeStyle = color
  ctx.lineWidth = 1
  ctx.fillStyle = textColor
  setFontSizeInInches(ctx, 3)

  const halfWidth = new Distance(FIELD_WIDTH.inches / 2)
  const halfHeight = new Distance(FIELD_HEIGHT.inches / 2)

  for (let x = -halfWidth.inches; x <= halfWidth.inches; x += cellSize.inches) {
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
      ctx.fillText(`(${x}", ${y}")`, p.x + 5, p.y + 5)
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

export async function imageToBase64(url: string): Promise<string> {
  const response = await fetch(url)
  const blob = await response.blob()
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.onloadend = () => resolve(reader.result as string)
    reader.onerror = reject
    reader.readAsDataURL(blob)
  })
}

export async function drawBase64Image(
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
