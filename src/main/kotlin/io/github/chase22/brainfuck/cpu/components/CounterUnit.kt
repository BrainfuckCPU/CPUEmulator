package io.github.chase22.brainfuck.cpu.components

import io.github.chase22.brainfuck.cpu.CPU
import io.github.chase22.brainfuck.cpu.base.ByteInt

class CounterUnit : ClockReceiver {
    var currentValue = ByteInt(0)

    fun increment() = currentValue++
    fun decrement() = currentValue--

    fun reset() {
        currentValue = ByteInt(0)
    }

    override fun onClockTick(cycleCount: UInt) {
        if (ControlLines.counterUnitIn) currentValue = CPU.databus.currentValue
        if (ControlLines.counterUnitUp) increment()
        if (ControlLines.counterUnitDown) decrement()
    }
}