package io.github.chase22.brainfuck.cpu.base

import io.github.chase22.brainfuck.cpu.components.ClockReceiver
import io.github.chase22.brainfuck.cpu.components.ControlLines

open class Counter {
    var currentValue: ByteInt = ByteInt(0)
        private set

    fun countUp() = currentValue++
    fun countDown() = currentValue--
    fun reset() {
        currentValue = ByteInt(0)
    }
}

class ProgramCounter : Counter(), ClockReceiver {
    override fun onClockTick(cycleCount: UInt) {
        if (ControlLines.programCounterUp) countUp()
        if (ControlLines.programCounterDown) countDown()
    }

}

class TapeMemoryCounter : Counter(), ClockReceiver {
    override fun onClockTick(cycleCount: UInt) {
        if (ControlLines.tapeMemoryCounterUp) countUp()
        if (ControlLines.tapeMemoryCounterDown) countDown()
    }

}