<script lang="ts">
  import { Section } from "$primitives"
  import { onMount } from "svelte"
  import { Distance, FIELD_HEIGHT, FIELD_WIDTH, Point } from "./primitives"
  import {
    drawBase64Image,
    drawGrid,
    drawLine,
    drawPoint,
    imageToBase64,
    init,
  } from "./draw"
  import { info } from "$lib"

  let canvas: HTMLCanvasElement
  let base64Image: string

  onMount(async () => {
    init(canvas)

    const imageUrl = "/fields/field.png"
    base64Image = await imageToBase64(imageUrl)

    await drawBase64Image(
      base64Image,
      new Point(-72, 72),
      new Distance(24 * 6),
      new Distance(24 * 6)
    )

    drawLine(new Point(0, 0), new Point(0, 10), "blue", new Distance(1))
    drawGrid(new Distance(24))
    drawPoint(new Point(0.0, 0.0))
  })

  $effect(() => {
    info.canvas.lines.forEach((line) => {
      drawLine(
        Point.withData(line.start),
        Point.withData(line.end),
        line.look.outlineColor,
        new Distance(line.look.outlineWidth)
      )
    })
  })
</script>

<Section title={"Field"}>
  <canvas bind:this={canvas} style="width: 1024px; height: 1024px;"></canvas>
  {JSON.stringify(info.canvas)}
</Section>

<style>
  canvas {
    background-color: black;
  }
</style>
