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
}.build()

class InstructionMemory(val instructions: Map<Int, Instruction>) {
    operator fun get(instruction: Int, microstep: Int): ControlLines {
        return instructions[instruction]?.microsteps?.get(microstep)
            ?: throw IllegalArgumentException("Unknown instruction $instruction $microstep")
    }
}

class Instruction(
    val instruction: Int,
    val microsteps: Map<Int, ControlLines>
)

class InstructionMemoryBuilder(private val setup: InstructionMemoryBuilder.() -> Unit) {
    private val instructions = mutableListOf<Instruction>()

    fun instruction(instruction: InstructionSet, setup: InstructionBuilder.() -> Unit) {
        instructions.add(InstructionBuilder(instruction.ordinal).apply(setup).build())
    }

    fun build() = InstructionMemory(apply(setup).instructions.associateBy { it.instruction })
}

class InstructionBuilder(private val instruction: Int) {
    private val microsteps = mutableMapOf<Int, ControlLines>()

    fun build(): Instruction = Instruction(instruction, microsteps)

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