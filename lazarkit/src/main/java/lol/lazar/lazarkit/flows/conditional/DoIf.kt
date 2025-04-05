package lol.lazar.lazarkit.flows.conditional

import lol.lazar.lazarkit.flows.Flow
import lol.lazar.lazarkit.flows.FlowScope

fun doIf(condition: () -> Boolean, block: FlowScope.() -> Unit) = DoIf(
    condition = condition,
    flowProvider = { FlowScope().apply(block) }
)

class DoIf(
    val condition: () -> Boolean,
    val flowProvider: () -> Flow
) : Flow({}) {
    var passedCheck = false
    var flow: Flow? = null

    override fun innerAction() {
        if (!passedCheck) {
            passedCheck = condition()
            if (!passedCheck) {
                finishedTime = System.currentTimeMillis()
                return
            }
            flow = flowProvider()
        }
        if (flow == null) {
            return
        }
        flow?.execute()
        if (flow?.isFinished == true) finishedTime = System.currentTimeMillis()
    }
}
