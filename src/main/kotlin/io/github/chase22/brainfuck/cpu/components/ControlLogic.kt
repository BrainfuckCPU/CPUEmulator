package io.github.chase22.brainfuck.cpu.components

import io.github.chase22.brainfuck.cpu.CPU
import io.github.chase22.brainfuck.cpu.InstructionSet.*

class ControlLogic {
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
        if (CPU.tapeMemory.currentValue.value == 0) {
            loopCounterUp()
            while (CPU.loopCounter.isInLoop) {
                programCounterUp()
                if (CPU.programMemory.currentInstruction == LOOP_START) {
                    loopCounterUp()
                } else if (CPU.programMemory.currentInstruction == LOOP_END) {
                    loopCounterDown()
                }
            }
        }
    }

    private fun loopEnd() {
        if (CPU.tapeMemory.currentValue.value != 0) {
            loopCounterUp()
            while (CPU.loopCounter.isInLoop) {
                programCounterDown()
                if (CPU.programMemory.currentInstruction == LOOP_START) {
                    loopCounterDown()
                } else if (CPU.programMemory.currentInstruction == LOOP_END) {
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