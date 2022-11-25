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
        when {
            ControlLines.counterUnitIn -> currentValue = CPU.databus.currentValue
            ControlLines.counterUnitUp -> increment()
            ControlLines.counterUnitDown -> decrement()
        }
    }
}