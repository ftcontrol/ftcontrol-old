export class Distance {
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

export const FIELD_WIDTH = new Distance(24 * 6, 1024)
export const FIELD_HEIGHT = new Distance(24 * 6, 1024)

export class Point {
  fieldX: Distance
  fieldY: Distance

  x: number
  y: number
  constructor(fieldX: number, fieldY: number) {
    this.fieldX = new Distance(fieldX)
    this.fieldY = new Distance(fieldY)
    const pixelsPerInch = FIELD_WIDTH.pixels / FIELD_WIDTH.inches
    this.x = this.fieldX.inches * pixelsPerInch
    this.y = this.fieldY.inches * -pixelsPerInch
  }

  static withData(data: { x: number; y: number }) {
    return new Point(data.x, data.y)
  }
}
