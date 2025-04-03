package lol.lazar.lazarkit.panels.data

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
class Canvas(
    var lines: MutableList<Line> = mutableListOf(),
) {
    fun clear() {
        lines = mutableListOf()
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
)

@Serializable
abstract class Drawable(private val type: Types) {
    enum class Types {
        @SerializedName("line")
        LINE,
        SQUARE,
        RECTANGLE,
        CIRCLE,
        IMG
    }
}

@Serializable
class Line(
    var start: Point,
    var end: Point,
    var offset: Pose = Pose(),
    var look: Look,
    var zIndex: Int = 0,
) : Drawable(Types.LINE)