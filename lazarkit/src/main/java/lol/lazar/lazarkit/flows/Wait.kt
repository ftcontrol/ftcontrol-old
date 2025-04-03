package lol.lazar.lazarkit.flows

import kotlinx.coroutines.delay


fun wait(durationMillis: Long) = Wait(durationMillis)

class Wait(durationMillis: Long) : Flow(
    {
        delay(durationMillis)
    }
)