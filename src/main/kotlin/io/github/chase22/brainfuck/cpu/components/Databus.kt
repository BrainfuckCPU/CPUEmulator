package io.github.chase22.brainfuck.cpu.components

import io.github.chase22.brainfuck.cpu.base.ByteInt

class Databus(
    private val tapeMemory: Memory,
    private val counterUnit: CounterUnit,
    private val ioUnit: IOUnit,
    private val controlLines: ControlLines
) {
    val currentValue: ByteInt
        get() = when {
            controlLines.tapeMemoryOut -> tapeMemory.currentValue
            controlLines.counterUnitOut -> counterUnit.currentValue
            controlLines.ioUnitOut -> ioUnit.read()
            else -> ByteInt(0)
        }
}