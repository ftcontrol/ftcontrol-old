export enum DrawableTypes {
  LINE = "LINE",
}

export const emptyCanvas: Canvas = {
  lines: [],
}

export interface Canvas {
  lines: Line[]
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
