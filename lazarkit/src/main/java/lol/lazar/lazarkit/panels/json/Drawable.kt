package lol.lazar.lazarkit.panels.json

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
class Canvas(
    var lines: MutableList<Line> = mutableListOf(),
    var rectangles: MutableList<Rectangle> = mutableListOf(),
    var circles: MutableList<Circle> = mutableListOf(),
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