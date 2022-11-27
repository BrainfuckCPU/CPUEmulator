package io.github.chase22.brainfuck.cpu

import io.github.chase22.brainfuck.cpu.InstructionSet.*
import io.github.chase22.brainfuck.cpu.components.ControlLines

val instructionMemory = InstructionMemoryBuilder {
    instruction(STOP) {
        microStep(0) {
            halt()
        }
    }

    instruction(RESET) {
        microStep(0) {
            reset()
        }
        microStep(1) {
            programCounterUp()

        }
    }

    instruction(PLUS) {
        microStep(0) {
            tapeMemoryOut()
            counterUnitIn()
        }
        microStep(1) {
            counterUnitUp()
        }
        microStep(2) {
            counterUnitOut()
            tapeMemoryIn()
        }
        nextInstructionMicrostep(3)
    }

    instruction(MINUS) {
        microStep(0) {
            tapeMemoryOut()
            counterUnitIn()
        }
        microStep(1) {
            counterUnitDown()
        }
        microStep(2) {
            counterUnitOut()
            tapeMemoryIn()
        }
        nextInstructionMicrostep(3)
    }

    instruction(NEXT) {
        microStep(0) {
            tapeMemoryCounterUp()
        }
        nextInstructionMicrostep(1)
    }

    instruction(PREVIOUS) {
        microStep(0) {
            tapeMemoryCounterDown()
        }
        nextInstructionMicrostep(1)
    }

    instruction(OUTPUT) {
        microStep(0) {
            tapeMemoryOut()
            ioUnitIn()
        }
        nextInstructionMicrostep(1)
    }

    instruction(INPUT) {
        microStep(0) {
            ioUnitOut()
            tapeMemoryIn()
        }
        nextInstructionMicrostep(1)
    }

    instruction(HALT_ON_ZERO) {
        conditional { it.currentZero }
        microStep(0) {
            halt()
        }
    }
    instruction(HALT_ON_ZERO) {
        nextInstructionMicrostep(0)
    }
}.build()

class InstructionMemory(val instructions: Map<Int, List<Instruction>>) {
    operator fun get(instruction: Int, microstep: Int): ControlLines {
        return instructions[instruction]?.find { it.conditional(CPU.flagRegister) }?.microsteps?.get(microstep)
            ?: throw IllegalArgumentException("Unknown instruction $instruction $microstep")
    }
}

class Instruction(
    val instruction: Int,
    val microsteps: Map<Int, ControlLines>,
    val conditional: (FlagRegister) -> Boolean
)

class InstructionMemoryBuilder(private val setup: InstructionMemoryBuilder.() -> Unit) {
    private val instructions = mutableMapOf<InstructionSet, List<Instruction>>()

    fun instruction(instruction: InstructionSet, setup: InstructionBuilder.() -> Unit) {
        val buildInstruction = InstructionBuilder(instruction.ordinal).apply(setup).build()

        instructions.merge(instruction, listOf(buildInstruction)) { old, new -> old + new }
    }

    fun build() = InstructionMemory(apply(setup).instructions.mapKeys { it.key.ordinal }.toMap())
}

class InstructionBuilder(private val instruction: Int) {
    private val microsteps = mutableMapOf<Int, ControlLines>()
    private var conditional: (FlagRegister) -> Boolean = { true }

    fun build(): Instruction = Instruction(instruction, microsteps, conditional)

    fun conditional(conditional: (FlagRegister) -> Boolean) {
        this.conditional = conditional
    }

    fun microStep(microstep: Int, setup: ControlLinesBuilder.() -> Unit) {
        if (microsteps.putIfAbsent(microstep, ControlLinesBuilder().apply(setup).build()) != null) {
            throw IllegalArgumentException("Duplicate microstep $microstep for instruction $instruction")
        }
    }

    fun nextInstructionMicrostep(microstep: Int) = microStep(microstep) {
        programCounterUp()
        microstepCounterReset()
    }
}

class ControlLinesBuilder {
    private val controlLines: ControlLines = ControlLines()

    fun counterUnitUp() {
        controlLines.counterUnitUp = true
    }

    fun counterUnitDown() {
        controlLines.counterUnitDown = true
    }

    fun counterUnitIn() {
        controlLines.counterUnitIn = true
    }

    fun counterUnitOut() {
        controlLines.counterUnitOut = true
    }

    fun tapeMemoryIn() {
        controlLines.tapeMemoryIn = true
    }

    fun tapeMemoryOut() {
        controlLines.tapeMemoryOut = true
    }

    fun programCounterUp() {
        controlLines.programCounterUp = true
    }

    fun programCounterDown() {
        controlLines.programCounterDown = true
    }

    fun microstepCounterReset() {
        controlLines.microstepCounterReset = true
    }

    fun tapeMemoryCounterUp() {
        controlLines.tapeMemoryCounterUp = true
    }

    fun tapeMemoryCounterDown() {
        controlLines.tapeMemoryCounterDown = true
    }

    fun ioUnitIn() {
        controlLines.ioUnitIn = true
    }

    fun ioUnitOut() {
        controlLines.ioUnitOut = true
    }

    fun loopCounterUp() {
        controlLines.loopCounterUp = true
    }

    fun loopCounterDown() {
        controlLines.loopCounterDown = true
    }

    fun halt() {
        controlLines.halt = true
    }

    fun reset() {
        controlLines.reset = true
    }

    fun build() = controlLines
}