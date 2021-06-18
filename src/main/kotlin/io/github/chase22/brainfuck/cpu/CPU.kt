package io.github.chase22.brainfuck.cpu

import io.github.chase22.brainfuck.cpu.base.Counter
import io.github.chase22.brainfuck.cpu.components.ControlLogic
import io.github.chase22.brainfuck.cpu.components.CounterUnit
import io.github.chase22.brainfuck.cpu.components.IOUnit
import io.github.chase22.brainfuck.cpu.components.Memory

object CPU {
    val tapeMemoryCounter = Counter()
    val programCounter = Counter()

    val tapeMemory = Memory(tapeMemoryCounter)
    val programMemory = Memory(programCounter)

    val counterUnit = CounterUnit()
    val ioUnit = IOUnit()

    val controlLogic = ControlLogic()

    fun run() {
        controlLogic.run()
        println()
    }

    fun reset() {
        tapeMemoryCounter.reset()
        programCounter.reset()
        tapeMemory.reset()
        counterUnit.reset()
    }
}