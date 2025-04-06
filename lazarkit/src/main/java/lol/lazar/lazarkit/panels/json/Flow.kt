package lol.lazar.lazarkit.panels.json

import kotlinx.serialization.SerialName
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
        INSTANT,
        EMPTY,
        WAIT
    }
}

@Serializable
data class DoIfJson(
    val condition: Boolean,
    val body: String
) : JsonFlow(Types.DOIF)

@Serializable
data class WaitJson(
    val startedTimestamp: Long,
    val time: Long
) : JsonFlow(Types.WAIT)

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

@Serializable
class EmptyJson : JsonFlow(Types.EMPTY)

@Serializable
@SerialName("allFlows")
data class AllFlowsJson(
    var flows: List<JsonFlow>
) : JSONData()