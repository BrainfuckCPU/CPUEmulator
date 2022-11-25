package io.github.chase22.brainfuck.cpu.components

import io.github.chase22.brainfuck.cpu.CPU
import io.github.chase22.brainfuck.cpu.FlagRegister
import io.github.chase22.brainfuck.cpu.InstructionSet.*

class ControlLogic(private val flagRegister: FlagRegister) {
    fun run() {
        while (true) {
            val memoryBefore = "${CPU.tapeMemoryCounter.currentValue} : ${CPU.tapeMemory.currentValue}"
            when (CPU.programMemory.currentInstruction) {
                PLUS -> increment()
                MINUS -> decrement()
                NEXT -> shiftRight()
                PREVIOUS -> shiftLeft()
                OUTPUT -> output()
                INPUT -> input()
                LOOP_START -> loopStart()
                LOOP_END -> loopEnd()
                else -> break
            }
            if (CPU.debugOutput) {
                println("${CPU.programCounter.currentValue}: ${CPU.programMemory.currentInstruction.command} - $memoryBefore -> ${CPU.tapeMemoryCounter.currentValue} : ${CPU.tapeMemory.currentValue}")
            }
            programCounterUp()
        }
    }

    private fun loopStart() {
        if (flagRegister.currentZero) {
            loopCounterUp()
            while (flagRegister.isInLoop) {
                programCounterUp()
                if (flagRegister.isCurrentLoopStart) {
                    loopCounterUp()
                } else if (flagRegister.isCurrentLoopEnd) {
                    loopCounterDown()
                }
            }
        }
    }

    private fun loopEnd() {
        if (!flagRegister.currentZero) {
            loopCounterUp()
            while (flagRegister.isInLoop) {
                programCounterDown()
                if (flagRegister.isCurrentLoopStart) {
                    loopCounterDown()
                } else if (flagRegister.isCurrentLoopEnd) {
                    loopCounterUp()
                }
            }
        }
    }

    fun increment() {
        tapeMemoryToCounterUnit()
        counterUnitUp()
        counterUnitToTapeMemory()

    }

    fun decrement() {
        tapeMemoryToCounterUnit()
        counterUnitDown()
        counterUnitToTapeMemory()
    }

    private fun shiftLeft() {
        ControlLines.tapeMemoryCounterDown = true
        CPU.tick()
    }

    private fun shiftRight() {
        ControlLines.tapeMemoryCounterUp = true
        CPU.tick()
    }

    private fun input() {
        ControlLines.tapeMemoryIn = true
        ControlLines.ioUnitOut = true
        CPU.tick()
    }

    private fun output() {
        ControlLines.tapeMemoryOut = true
        ControlLines.ioUnitIn = true
        CPU.tick()
    }

    private fun tapeMemoryToCounterUnit() {
        ControlLines.tapeMemoryOut = true
        ControlLines.counterUnitIn = true
        CPU.tick()
    }

    private fun counterUnitToTapeMemory() {
        ControlLines.counterUnitOut = true
        ControlLines.tapeMemoryIn = true
        CPU.tick()
    }

    private fun programCounterDown() {
        ControlLines.programCounterDown = true
        CPU.tick()
    }

    private fun programCounterUp() {
        ControlLines.programCounterUp = true
        CPU.tick()
    }

    private fun loopCounterUp() {
        ControlLines.loopCounterUp = true
        CPU.tick()
    }

    private fun loopCounterDown() {
        ControlLines.loopCounterDown = true
        CPU.tick()
    }

    private fun counterUnitUp() {
        ControlLines.counterUnitUp = true
        CPU.tick()
    }

    private fun counterUnitDown() {
        ControlLines.counterUnitDown = true
        CPU.tick()
    }
}