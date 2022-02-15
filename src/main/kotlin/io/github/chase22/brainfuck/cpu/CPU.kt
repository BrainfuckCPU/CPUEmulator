package io.github.chase22.brainfuck.cpu

import io.github.chase22.brainfuck.cpu.base.LoopCounter
import io.github.chase22.brainfuck.cpu.base.ProgramCounter
import io.github.chase22.brainfuck.cpu.base.TapeMemoryCounter
import io.github.chase22.brainfuck.cpu.components.*
import java.io.InputStream
import java.io.OutputStream

object CPU {
    val tapeMemoryCounter = TapeMemoryCounter()
    val programCounter = ProgramCounter()
    val loopCounter = LoopCounter()

    val tapeMemory = TapeMemory(tapeMemoryCounter)
    val programMemory = ProgramMemory(programCounter)

    val counterUnit = CounterUnit()
    val ioUnit = IOUnit()

    val databus = Databus(tapeMemory, counterUnit, ioUnit)

    private val controlLogic = ControlLogic()

    var debugOutput: Boolean = false
    var outputStream: OutputStream = System.out
    var inputStream: InputStream = System.`in`

    var cycleCount = 0.toUInt()

    private val clockReceivers = listOf(counterUnit, ioUnit, loopCounter, programCounter, tapeMemory, tapeMemoryCounter)

    fun run() {
        controlLogic.run()
        println()
    }

    fun tick() {
        clockReceivers.forEach { it.onClockTick(cycleCount) }
        cycleCount++
        ControlLines.reset()
    }

    fun loadProgram(program: String) {
        reset()
        programMemory.load(assemble(program))
    }

    fun reset() {
        tapeMemoryCounter.reset()
        programCounter.reset()
        tapeMemory.reset()
        counterUnit.reset()
        cycleCount = 0.toUInt()
    }
}