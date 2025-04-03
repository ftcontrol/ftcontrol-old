package lol.lazar.lazarkit.flows


class Flow(
    val execute: suspend () -> Unit,
)