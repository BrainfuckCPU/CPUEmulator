package io.github.chase22.brainfuck.cpu

import io.github.chase22.brainfuck.cpu.base.LoopCounter
import io.github.chase22.brainfuck.cpu.base.MicrostepCounter
import io.github.chase22.brainfuck.cpu.base.ProgramCounter
import io.github.chase22.brainfuck.cpu.base.TapeMemoryCounter
import io.github.chase22.brainfuck.cpu.components.ControlLines
import io.github.chase22.brainfuck.cpu.components.CounterUnit
import io.github.chase22.brainfuck.cpu.components.Databus
import io.github.chase22.brainfuck.cpu.components.IOUnit
import io.github.chase22.brainfuck.cpu.components.ProgramMemory
import io.github.chase22.brainfuck.cpu.components.TapeMemory
import java.io.InputStream
import java.io.OutputStream

object CPU {
    private val controlLines = ControlLines()

    val tapeMemoryCounter = TapeMemoryCounter(controlLines)
    val programCounter = ProgramCounter(controlLines)
    val microstepCounter = MicrostepCounter(controlLines)

    val loopCounter = LoopCounter(controlLines)
    val tapeMemory = TapeMemory(controlLines, tapeMemoryCounter)
    val programMemory = ProgramMemory(programCounter)

    val counterUnit = CounterUnit(controlLines)
    val ioUnit = IOUnit(controlLines)

    val databus = Databus(tapeMemory, counterUnit, ioUnit, controlLines)

    private val flagRegister = FlagRegister()

    var debugOutput: Boolean = false
    var outputStream: OutputStream = System.out
    var inputStream: InputStream = System.`in`

    var cycleCount = 0.toUInt()

    private val clockReceivers =
        listOf(counterUnit, ioUnit, loopCounter, programCounter, microstepCounter, tapeMemory, tapeMemoryCounter)

    fun run() {
        while (true) {
            println("${programMemory.currentInstruction.ordinal}, ${microstepCounter.currentValue.value}")
            controlLines.apply(instructionMemory[programMemory.currentInstruction.ordinal, microstepCounter.currentValue.value])
            println(controlLines)
            if (controlLines.halt) return
            tick()
        }
    }

    fun tick() {
        clockReceivers.forEach { it.onClockTick(cycleCount) }
        cycleCount++
        controlLines.reset()
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