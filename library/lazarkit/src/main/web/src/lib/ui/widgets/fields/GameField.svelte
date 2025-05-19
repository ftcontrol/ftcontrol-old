<script lang="ts">
  import { onMount } from "svelte"
  import { Distance, Point } from "./primitives"
  import {
    drawCircle,
    drawFieldImage,
    drawGrid,
    drawLine,
    drawRectangle,
    init,
    initFieldImage,
    updateOffsets,
  } from "./draw"
  import { info } from "$lib"
  import Content from "$ui/primitives/Content.svelte"
  import { settings } from "$lib/settings.svelte"

  let canvas: HTMLCanvasElement

  let wasInit = $state(false)

  onMount(() => {
    init(
      canvas,
      new Distance(info.canvas.offsetX),
      new Distance(info.canvas.offsetY)
    )

    // drawLine(new Point(0, 0), new Point(0, 10), "blue", new Distance(1))
    if (settings.fieldShowCoordinates == "true") drawGrid(new Distance(24))
    // drawPoint(new Point(0.0, 0.0))
    wasInit = true
  })

  $effect(() => {
    if (!wasInit) return
    updateOffsets(
      canvas,
      new Distance(info.canvas.offsetX),
      new Distance(info.canvas.offsetY)
    )
  })

  $effect(() => {
    if (!wasInit) return
    drawFieldImage()
    if (settings.fieldShowCoordinates == "true") drawGrid(new Distance(24))
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

<Content>
  <div style="width: 100%; overflow: hidden;">
    <img
      style="rotate: {settings.fieldOrientation};"
      src="/fields/field.png"
      alt="field"
    />
    <canvas style="rotate: {settings.fieldOrientation};" bind:this={canvas}
    ></canvas>
  </div>
</Content>

<style>
  div {
    position: relative;
  }
  canvas {
    display: block;
    max-width: 100%;
    height: auto;
    rotate: 0deg;
    transition: rotate 0.5s;
  }
  img {
    width: 100%;
    position: absolute;
    top: 0;
    rotate: 0deg;
    transition: rotate 0.5s;
  }
</style>
