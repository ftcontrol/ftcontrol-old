<script lang="ts">
  import { onMount } from "svelte"
  import { Distance, Point } from "./primitives"
  import {
    drawCircle,
    drawGrid,
    drawImage,
    drawLine,
    drawPoint,
    drawRectangle,
    getImage,
    imageToBase64,
    init,
  } from "./draw"
  import { info } from "$lib"
  import Content from "$ui/primitives/Content.svelte"
  import Header from "$ui/primitives/Header.svelte"
  import Title from "$ui/primitives/Title.svelte"

  let canvas: HTMLCanvasElement
  let base64Image: string
  let fieldImage: HTMLImageElement

  onMount(async () => {
    init(canvas)

    const imageUrl = "/fields/field.png"
    base64Image = await imageToBase64(imageUrl)

    fieldImage = await getImage(base64Image)

    drawImage(
      fieldImage,
      new Point(-72, 72),
      new Distance(24 * 6),
      new Distance(24 * 6)
    )

    drawLine(new Point(0, 0), new Point(0, 10), "blue", new Distance(1))
    drawGrid(new Distance(24))
    drawPoint(new Point(0.0, 0.0))
  })

  $effect(() => {
    drawImage(
      fieldImage,
      new Point(-72, 72),
      new Distance(24 * 6),
      new Distance(24 * 6)
    )
    if (info.canvas.lines) {
      info.canvas.lines.forEach((line) => {
        drawLine(
          Point.withData(line.start),
          Point.withData(line.end),
          line.look.outlineColor,
          new Distance(line.look.outlineWidth)
        )
      })
    }

    if (info.canvas.rectangles) {
      info.canvas.rectangles.forEach((rectangle) => {
        drawRectangle(
          Point.withData(rectangle.center),
          new Distance(rectangle.width),
          new Distance(rectangle.height),
          rectangle.look.fillColor,
          rectangle.look.outlineColor,
          new Distance(rectangle.look.outlineWidth)
        )
      })
    }

    if (info.canvas.circles) {
      info.canvas.circles.forEach((circle) => {
        drawCircle(
          Point.withData(circle.center),
          new Distance(circle.radius),
          circle.look.fillColor,
          circle.look.outlineColor,
          new Distance(circle.look.outlineWidth)
        )
      })
    }
  })
</script>

<Header>
  <Title>Field</Title>
</Header>
<Content>
  <div style="width: 100%; overflow: hidden;">
    <canvas bind:this={canvas}></canvas>
  </div>
</Content>

<style>
  canvas {
    display: block;
    max-width: 100%;
    height: auto;
  }
</style>
