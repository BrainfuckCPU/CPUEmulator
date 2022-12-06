package io.github.chase22.brainfuck.cpu

import io.github.chase22.brainfuck.cpu.Command.*
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


    multiInstruction(PLUS) {
        conditional({ it.isInLoop && it.loopForward }) {
            nextInstructionMicrostep(0)
        }
        conditional({ it.isInLoop && !it.loopForward }) {
            microStep(0) {
                programCounterDown()
                microstepCounterReset()
            }
        }
        always {
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
    }

    multiInstruction(MINUS) {
        conditional({ it.isInLoop && it.loopForward }) {
            nextInstructionMicrostep(0)
        }
        conditional({ it.isInLoop && !it.loopForward }) {
            microStep(0) {
                programCounterDown()
                microstepCounterReset()
            }
        }
        always {
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
    }

    multiInstruction(NEXT) {
        conditional({ it.isInLoop && it.loopForward }) {
            nextInstructionMicrostep(0)
        }
        conditional({ it.isInLoop && !it.loopForward }) {
            microStep(0) {
                programCounterDown()
                microstepCounterReset()
            }
        }

        always {
            microStep(0) {
                tapeMemoryCounterUp()
            }
            nextInstructionMicrostep(1)
        }
    }

    multiInstruction(PREVIOUS) {
        conditional({ it.isInLoop && it.loopForward }) {
            nextInstructionMicrostep(0)
        }
        conditional({ it.isInLoop && !it.loopForward }) {
            microStep(0) {
                programCounterDown()
                microstepCounterReset()
            }
        }
        always {
            microStep(0) {
                tapeMemoryCounterDown()
            }
            nextInstructionMicrostep(1)
        }
    }

    multiInstruction(OUTPUT) {
        conditional({ it.isInLoop && it.loopForward }) {
            nextInstructionMicrostep(0)
        }
        conditional({ it.isInLoop && !it.loopForward }) {
            microStep(0) {
                programCounterDown()
                microstepCounterReset()
            }
        }

        always {
            microStep(0) {
                tapeMemoryOut()
                ioUnitIn()
            }
            nextInstructionMicrostep(1)
        }
    }

    instruction(INPUT) {
        microStep(0) {
            ioUnitOut()
            tapeMemoryIn()
        }
        nextInstructionMicrostep(1)
    }

    multiInstruction(HALT_ON_ZERO) {
        conditional({ it.currentZero }) {
            microStep(0) { halt() }
        }
        always {
            nextInstructionMicrostep(0)
        }
    }

    multiInstruction(LOOP_START) {
        conditional({ it.currentZero }) {
            microStep(0) {
                loopCounterUp()
            }
            nextInstructionMicrostep(1)
        }

        always {
            nextInstructionMicrostep(0)
        }
    }

    multiInstruction(LOOP_END) {
        conditional({ !it.currentZero && it.loopForward }) {
            microStep(0) {
                loopCounterDown()
            }
            nextInstructionMicrostep(1)
        }

        conditional({ !it.currentZero && !it.loopForward }) {
            microStep(0) {
                loopCounterDown()
            }
            microStep(1) {
                programCounterDown()
                microstepCounterReset()
            }
        }

        always {
            nextInstructionMicrostep(0)
        }
    }
}.build()

class InstructionMemory(val instructions: Map<Int, List<Instruction>>) {
    operator fun get(instruction: Int, microstep: Int): ControlLines =
        instructions[instruction]?.find { it.condition(CPU.flagRegister) }?.microsteps?.get(microstep)
            ?: throw IllegalArgumentException("Unknown instruction $instruction $microstep")
}

class Instruction(
    val command: Command,
    val microsteps: Map<Int, ControlLines>,
    val condition: InstructionCondition
)

class InstructionMemoryBuilder(private val setup: InstructionMemoryBuilder.() -> Unit) {
    private val instructions = mutableMapOf<Command, List<Instruction>>()

    fun instruction(instruction: Command, setup: InstructionBuilder.() -> Unit) {
        val buildInstruction = InstructionBuilder(instruction).apply(setup).build()

        addInstructions(listOf(buildInstruction))
    }

    fun multiInstruction(command: Command, setup: MultiInstructionBuilder.() -> Unit) {
        addInstructions(MultiInstructionBuilder(command).apply(setup).build())
    }

    private fun addInstructions(instructions: List<Instruction>) {
        this.instructions.merge(instructions.first().command, instructions) { old, new -> old + new }
    }

    fun build() = InstructionMemory(apply(setup).instructions.mapKeys { it.key.ordinal }.toMap())
}

class MultiInstructionBuilder(private val command: Command) {
    private val subInstructions = mutableListOf<Instruction>()

    fun always(setup: InstructionBuilder.() -> Unit) {
        if (subInstructions.any { it.condition == InstructionBuilder.ALWAYS_TRUE_CONDITION }) {
            throw IllegalStateException("Multiple Always true conditions in ${command.command}")
        }
        subInstructions.add(InstructionBuilder(command).apply(setup).build())
    }

    fun conditional(condition: InstructionCondition, setup: InstructionBuilder.() -> Unit) {
        subInstructions.add(InstructionBuilder(command).apply(setup).build(condition))
    }

    fun build(): List<Instruction> = subInstructions.toList()
}

class InstructionBuilder(private val command: Command) {
    private val microsteps = mutableMapOf<Int, ControlLines>()

    fun build(condition: InstructionCondition = ALWAYS_TRUE_CONDITION): Instruction =
        Instruction(command, microsteps, condition)

    fun microStep(microstep: Int, setup: ControlLinesBuilder.() -> Unit) {
        if (microsteps.putIfAbsent(microstep, ControlLinesBuilder().apply(setup).build()) != null) {
            throw IllegalArgumentException("Duplicate microstep $microstep for command $command")
        }
    }

    fun nextInstructionMicrostep(microstep: Int) = microStep(microstep) {
        programCounterUp()
        microstepCounterReset()
    }

    companion object {
        val ALWAYS_TRUE_CONDITION: InstructionCondition = { true }
    }
}

typealias InstructionCondition = (FlagRegister) -> Boolean

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