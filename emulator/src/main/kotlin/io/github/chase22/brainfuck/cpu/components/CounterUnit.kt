package io.github.chase22.brainfuck.cpu.components

import io.github.chase22.brainfuck.cpu.CPU
import io.github.chase22.brainfuck.cpu.base.ByteInt

class CounterUnit(private val controlLines: ControlLines) : ClockReceiver {
    var currentValue = ByteInt(0)

    fun increment() = currentValue++
    fun decrement() = currentValue--

    fun reset() {
        currentValue = ByteInt(0)
    }

    override fun onClockTick(cycleCount: UInt) {
        when {
            controlLines.counterUnitIn -> currentValue = CPU.databus.currentValue
            controlLines.counterUnitUp -> increment()
            controlLines.counterUnitDown -> decrement()
        }
    }
}