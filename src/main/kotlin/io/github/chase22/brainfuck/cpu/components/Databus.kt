package io.github.chase22.brainfuck.cpu.components

import io.github.chase22.brainfuck.cpu.base.ByteInt

class Databus(
    private val tapeMemory: Memory,
    private val counterUnit: CounterUnit,
    private val ioUnit: IOUnit
) {
    val currentValue: ByteInt
        get() =
            if (ControlLines.tapeMemoryOut) {
                tapeMemory.currentValue
            } else if (ControlLines.counterUnitOut) {
                counterUnit.currentValue
            } else if (ControlLines.ioUnitOut) {
                ioUnit.read()
            } else ByteInt(0)
}