package lol.lazar.lazarkit.flows

import kotlinx.serialization.Serializable

@Serializable
abstract class JsonFlow(val type: Types) {
    var id: String = ""
    var isFinished: Boolean = false

    enum class Types {
        DOIF,
        SEQUENTIAL,
        PARALLEL,
        RACE,
        INSTANT
    }
}

@Serializable
data class DoIfJson(
    val condition: Boolean,
    val body: String
) : JsonFlow(Types.DOIF)

@Serializable
data class SequentialJson(
    val index: Int,
    val flowsIds: List<String>
) : JsonFlow(Types.SEQUENTIAL)

@Serializable
data class ParallelJson(
    val flowsIds: List<String>
) : JsonFlow(Types.PARALLEL)

@Serializable
data class RaceJson(
    val flowsIds: List<String>
) : JsonFlow(Types.RACE)

@Serializable
class InstantJson : JsonFlow(Types.INSTANT)