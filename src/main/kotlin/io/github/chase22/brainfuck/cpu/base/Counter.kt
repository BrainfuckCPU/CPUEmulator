package io.github.chase22.brainfuck.cpu.base

import io.github.chase22.brainfuck.cpu.components.ClockReceiver
import io.github.chase22.brainfuck.cpu.components.ControlLines

open class Counter(val countUpLine: () -> Boolean, val countDownLine: () -> Boolean) : ClockReceiver {
    var currentValue: ByteInt = ByteInt(0)
        private set

    fun countUp() = currentValue++
    fun countDown() = currentValue--
    fun reset() {
        currentValue = ByteInt(0)
    }

    override fun onClockTick(cycleCount: UInt) {
        if (countUpLine()) countUp()
        if (countDownLine()) countDown()
    }
}

class ProgramCounter : Counter(
    ControlLines::programCounterUp,
    ControlLines::programCounterDown
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