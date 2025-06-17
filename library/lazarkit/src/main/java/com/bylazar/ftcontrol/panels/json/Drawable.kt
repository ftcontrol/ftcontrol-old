package com.bylazar.ftcontrol.panels.json

import kotlinx.serialization.Serializable

enum class CanvasRotation {
    DEG_0,
    DEG_90,
    DEG_180,
    DEG_270
}

enum class CanvasPresets {
    PANELS,
    PEDRO_PATHING,
    ROAD_RUNNER
}

@Serializable
class Canvas(
    var lines: MutableList<Line> = mutableListOf(),
    var rectangles: MutableList<Rectangle> = mutableListOf(),
    var circles: MutableList<Circle> = mutableListOf(),
    var offsetX: Double = 0.0,
    var offsetY: Double = 0.0,
    var rotation: CanvasRotation = CanvasRotation.DEG_0
) {
    fun clear() {
        lines = mutableListOf()
        rectangles = mutableListOf()
        circles = mutableListOf()
    }

    fun add(drawable: Drawable) {
        when (drawable) {
            is Line -> lines.add(drawable)
            is Rectangle -> rectangles.add(drawable)
            is Circle -> circles.add(drawable)
        }
    }

    fun withOffsetX(offsetX: Double): Canvas {
        this.offsetX = offsetX
        return this
    }

    fun withOffsetY(offsetY: Double): Canvas {
        this.offsetY = offsetY
        return this
    }

    fun withRotation(rotation: CanvasRotation): Canvas {
        this.rotation = rotation
        return this
    }

    fun withOffsets(offsetX: Double, offsetY: Double, rotation: CanvasRotation = CanvasRotation.DEG_0): Canvas {
        this.offsetX = offsetX
        this.offsetY = offsetY
        this.rotation = rotation
        return this
    }

    fun withOffsetPreset(preset: CanvasPresets): Canvas{
        when(preset){
            CanvasPresets.PANELS -> {
                //no need
            }
            CanvasPresets.PEDRO_PATHING -> {
                this.offsetX = -24.0 * 3
                this.offsetY = 24.0 * 3
                this.rotation = CanvasRotation.DEG_270
            }
            CanvasPresets.ROAD_RUNNER -> {
                //no need
            }
        }
        return this
    }

    val isEmpty: Boolean
        get() = lines.isEmpty() && rectangles.isEmpty() && circles.isEmpty()
}

@Serializable
class Point(
    var x: Double,
    var y: Double
) {
    constructor() : this(0.0, 0.0)

    val toPose: Pose
        get() = Pose(x, y, 0.0)
}

@Serializable
class Pose(
    var x: Double,
    var y: Double,
    var heading: Double
) {
    constructor() : this(0.0, 0.0, 0.0)

    val toPoint: Point
        get() = Point(x, y)
}

@Serializable
class Look(
    var fillColor: String,
    var outlineColor: String,
    var outlineWidth: Double,
    var opacity: Double,
) {
    constructor() : this("", "", 0.0, 1.0)
}

@Serializable
abstract class Drawable(val type: Types) {
    var offset: Pose = Pose()
    var look: Look = Look()
    var zIndex: Int = 0

    enum class Types {
        LINE,
        RECTANGLE,
        CIRCLE,
        IMG
    }

    fun withLook(look: Look): Drawable {
        this.look = look
        return this
    }

    fun withOffset(offset: Pose): Drawable {
        this.offset = offset
        return this
    }

    fun withZIndex(zIndex: Int): Drawable {
        this.zIndex = zIndex
        return this
    }
}

@Serializable
class Line(
    var start: Point,
    var end: Point,
) : Drawable(Types.LINE)

@Serializable
class Rectangle(
    var center: Point,
    var width: Double,
    var height: Double,
) : Drawable(Types.RECTANGLE)

@Serializable
class Circle(
    var center: Point,
    var radius: Double,
) : Drawable(Types.CIRCLE)