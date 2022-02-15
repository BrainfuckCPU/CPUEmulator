package io.github.chase22.brainfuck.cpu

import io.github.chase22.brainfuck.cpu.base.ProgramCounter
import io.github.chase22.brainfuck.cpu.base.TapeMemoryCounter
import io.github.chase22.brainfuck.cpu.components.*
import kotlin.reflect.full.createType
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties

object CPU {
    val tapeMemoryCounter = TapeMemoryCounter()
    val programCounter = ProgramCounter()

    val tapeMemory = TapeMemory(tapeMemoryCounter)
    val programMemory = ProgramMemory(programCounter)

    val counterUnit = CounterUnit()
    val ioUnit = IOUnit()

    val databus = Databus(tapeMemory, counterUnit, ioUnit)

    private val controlLogic = ControlLogic()

    var debugOutput: Boolean = false

    var cycleCount = 0.toUInt()

    private val clockReceivers: List<ClockReceiver> by lazy {
        CPU::class.memberProperties
            .filter { it.returnType.isSubtypeOf(ClockReceiver::class.createType()) }
            .map {
                it.get(CPU) as ClockReceiver
            }
    }

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