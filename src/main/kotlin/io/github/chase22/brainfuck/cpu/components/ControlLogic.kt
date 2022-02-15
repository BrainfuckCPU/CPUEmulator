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
            ControlLines.programCounterUp = true
            CPU.tick()
        }
    }

    private fun loopStart() {
        if (CPU.tapeMemory.currentValue.value == 0) {
            var loopCounter = 1
            while (loopCounter != 0) {
                ControlLines.programCounterUp = true
                CPU.tick()
                if (CPU.programMemory.currentInstruction == LOOP_START) {
                    loopCounter++
                } else if (CPU.programMemory.currentInstruction == LOOP_END) {
                    loopCounter--
                }
            }
        }
    }

    private fun loopEnd() {
        if (CPU.tapeMemory.currentValue.value != 0) {
            var loopCounter = 1
            while (loopCounter != 0) {
                ControlLines.programCounterDown = true
                CPU.tick()
                if (CPU.programMemory.currentInstruction == LOOP_START) {
                    loopCounter--
                } else if (CPU.programMemory.currentInstruction == LOOP_END) {
                    loopCounter++
                }
            }
        }
    }

    fun increment() {
        ControlLines.tapeMemoryOut = true
        ControlLines.counterUnitIn = true
        CPU.tick()

        ControlLines.counterUnitUp = true
        CPU.tick()

        ControlLines.counterUnitOut = true
        ControlLines.tapeMemoryIn = true
        CPU.tick()

    }

    fun decrement() {
        ControlLines.tapeMemoryOut = true
        ControlLines.counterUnitIn = true
        CPU.tick()

        ControlLines.counterUnitDown = true
        CPU.tick()

        ControlLines.counterUnitOut = true
        ControlLines.tapeMemoryIn = true
        CPU.tick()
    }

    fun shiftLeft() {
        ControlLines.tapeMemoryCounterDown = true
        CPU.tick()
    }

    fun shiftRight() {
        ControlLines.tapeMemoryCounterUp = true
        CPU.tick()
    }

    fun input() {
        ControlLines.tapeMemoryIn = true
        ControlLines.ioUnitOut = true
        CPU.tick()
    }

    fun output() {
        ControlLines.tapeMemoryOut = true
        ControlLines.ioUnitIn = true
        CPU.tick()
    }
}