package lol.lazar.lazarkit.flows

class TestingSyntaxConcept {
    open class Flow1()
    class SeqFlow1(vararg val flows: Flow1) : Flow1()

    val autonomous1 = SeqFlow1(
        Flow1(),
        Flow1(),
        Flow1(),
        SeqFlow1(
            Flow1(),
            Flow1(),
        ),
    )

    open class Flow2(
        block: Flow2.() -> Unit
    ) : Flow1() {
        var mode = Mode.NONE

        enum class Mode {
            NONE,
            SEQ,
            PARA,
            RACE
        }

        init {
            this.block()
        }

        fun add(flow: Flow1) {

        }

        fun setSeq() {
            mode = Mode.SEQ
        }

        fun setPara() {
            mode = Mode.PARA
        }

        fun setRace() {
            mode = Mode.RACE
        }
    }

    var autonomous2 = Flow2 {
        setSeq()
        add(Flow1())
    }


}