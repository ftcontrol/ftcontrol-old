<script lang="ts">
  import { Section } from "$primitives"
  import { onMount } from "svelte"
  import { Distance, Point } from "./primitives"
  import {
    drawGrid,
    drawImage,
    drawLine,
    drawPoint,
    getImage,
    imageToBase64,
    init,
  } from "./draw"
  import { info } from "$lib"

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
  })
</script>

<Section title={"Field"}>
  <div style="width: 100%; overflow: hidden;">
    <canvas bind:this={canvas}></canvas>
  </div>
</Section>

<style>
  canvas {
    display: block;
    max-width: 100%;
    height: auto;
  }
</style>
