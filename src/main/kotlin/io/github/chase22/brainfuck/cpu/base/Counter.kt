package io.github.chase22.brainfuck.cpu.base

import io.github.chase22.brainfuck.cpu.components.ClockReceiver
import io.github.chase22.brainfuck.cpu.components.ControlLines

open class Counter(
    val countUpLine: () -> Boolean,
    val countDownLine: () -> Boolean,
    val resetLine: () -> Boolean = { false }
) : ClockReceiver {
    var currentValue: ByteInt = ByteInt(0)
        private set

    private fun countUp() = currentValue++
    private fun countDown() = currentValue--
    fun reset() {
        currentValue = ByteInt(0)
    }

    override fun onClockTick(cycleCount: UInt) {
        when {
            resetLine() -> reset()
            countUpLine() -> countUp()
            countDownLine() -> countDown()
        }
    }
}

class ProgramCounter(controlLines: ControlLines) : Counter(
    controlLines::programCounterUp,
    controlLines::programCounterDown
)

class MicrostepCounter(controlLines: ControlLines) : Counter(
    { !controlLines.microstepCounterHold },
    { false },
    controlLines::microstepCounterReset
)

class TapeMemoryCounter(controlLines: ControlLines) : Counter(
    controlLines::tapeMemoryCounterUp,
    controlLines::tapeMemoryCounterDown
)

class LoopCounter(controlLines: ControlLines) : Counter(
    controlLines::loopCounterUp,
    controlLines::loopCounterDown
) {
    val isInLoop
        get() = currentValue != ByteInt(0)
}