package io.github.chase22.brainfuck.cpu

import io.github.chase22.brainfuck.cpu.base.LoopCounter
import io.github.chase22.brainfuck.cpu.base.MicrostepCounter
import io.github.chase22.brainfuck.cpu.base.ProgramCounter
import io.github.chase22.brainfuck.cpu.base.TapeMemoryCounter
import io.github.chase22.brainfuck.cpu.components.ControlLines
import io.github.chase22.brainfuck.cpu.components.ControlLogic
import io.github.chase22.brainfuck.cpu.components.CounterUnit
import io.github.chase22.brainfuck.cpu.components.Databus
import io.github.chase22.brainfuck.cpu.components.IOUnit
import io.github.chase22.brainfuck.cpu.components.ProgramMemory
import io.github.chase22.brainfuck.cpu.components.TapeMemory
import java.io.InputStream
import java.io.OutputStream

object CPU {
    val tapeMemoryCounter = TapeMemoryCounter()
    val programCounter = ProgramCounter()
    val microstepCounter = MicrostepCounter()

    val loopCounter = LoopCounter()
    val tapeMemory = TapeMemory(tapeMemoryCounter)
    val programMemory = ProgramMemory(programCounter)

    val counterUnit = CounterUnit()
    val ioUnit = IOUnit()

    val databus = Databus(tapeMemory, counterUnit, ioUnit)

    private val flagRegister = FlagRegister()

    private val controlLogic = ControlLogic(flagRegister)

    var debugOutput: Boolean = false
    var outputStream: OutputStream = System.out
    var inputStream: InputStream = System.`in`

    var cycleCount = 0.toUInt()

    private val clockReceivers =
        listOf(counterUnit, ioUnit, loopCounter, programCounter, microstepCounter, tapeMemory, tapeMemoryCounter)

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