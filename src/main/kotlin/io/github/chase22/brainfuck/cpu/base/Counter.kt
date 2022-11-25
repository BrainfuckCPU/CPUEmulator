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
        if (resetLine()) reset()
        if (countUpLine()) countUp()
        if (countDownLine()) countDown()
    }
}

class ProgramCounter : Counter(
    ControlLines::programCounterUp,
    ControlLines::programCounterDown
)

class MicrostepCounter : Counter(
    { !ControlLines.microstepCounterHold },
    { false },
    ControlLines::microstepCounterReset
)

class TapeMemoryCounter : Counter(
    ControlLines::tapeMemoryCounterUp,
    ControlLines::tapeMemoryCounterDown
)

class LoopCounter : Counter(
    ControlLines::loopCounterUp,
    ControlLines::loopCounterDown
) {
    val isInLoop
        get() = currentValue != ByteInt(0)
}