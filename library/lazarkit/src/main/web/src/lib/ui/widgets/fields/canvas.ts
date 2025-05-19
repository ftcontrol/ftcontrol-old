export enum DrawableTypes {
  LINE = "LINE",
  RECTANGLE = "RECTANGLE",
  CIRCLE = "CIRCLE",
}

export const emptyCanvas: Canvas = {
  offsetX: 0,
  offsetY: 0,
  lines: [],
  rectangles: [],
  circles: [],
}

export interface Canvas {
  offsetX: number
  offsetY: number
  lines: Line[]
  rectangles: Rectangle[]
  circles: Circle[]
}

export interface Point {
  x: number
  y: number
}

export interface Pose {
  x: number
  y: number
  heading: number
}

export interface Look {
  fillColor: string
  outlineColor: string
  outlineWidth: number
  opacity: number
}

export interface Drawable {
  type: DrawableTypes
  offset: Pose
  look: Look
}

export interface Line extends Drawable {
  type: DrawableTypes.LINE
  start: Point
  end: Point
}

export interface Rectangle extends Drawable {
  type: DrawableTypes.RECTANGLE
  center: Point
  width: number
  height: number
}

export interface Circle extends Drawable {
  type: DrawableTypes.RECTANGLE
  center: Point
  radius: number
}
