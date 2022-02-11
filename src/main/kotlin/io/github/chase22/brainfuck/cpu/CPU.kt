package io.github.chase22.brainfuck.cpu

import io.github.chase22.brainfuck.cpu.base.Counter
import io.github.chase22.brainfuck.cpu.components.*

object CPU {
    val tapeMemoryCounter = Counter()
    val programCounter = Counter()

    val tapeMemory = Memory(tapeMemoryCounter)
    val programMemory = ProgramMemory(programCounter)

    val counterUnit = CounterUnit()
    val ioUnit = IOUnit()

    private val controlLogic = ControlLogic()

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