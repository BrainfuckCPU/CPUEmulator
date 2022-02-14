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
            CPU.programCounter.countUp()
        }
    }

    private fun loopStart() {
        if (CPU.tapeMemory.currentValue.value == 0) {
            var loopCounter = 1
            while (loopCounter != 0) {
                CPU.programCounter.countUp()
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
                CPU.programCounter.countDown()
                if (CPU.programMemory.currentInstruction == LOOP_START) {
                    loopCounter--
                } else if (CPU.programMemory.currentInstruction == LOOP_END) {
                    loopCounter++
                }
            }
        }
    }

    fun increment() {
        memOutCuIn()
        CPU.counterUnit.increment()
        CuOutMemIn()
    }

    fun decrement() {
        memOutCuIn()
        CPU.counterUnit.decrement()
        CuOutMemIn()
    }

    fun shiftLeft() {
        CPU.tapeMemoryCounter.countDown()
    }

    fun shiftRight() {
        CPU.tapeMemoryCounter.countUp()
    }

    fun input() {
        CPU.tapeMemory.currentValue = CPU.ioUnit.read()
    }

    fun output() {
        CPU.ioUnit.write(CPU.tapeMemory.currentValue)
    }

    private fun memOutCuIn() {
        CPU.counterUnit.currentValue = CPU.tapeMemory.currentValue
    }

    private fun CuOutMemIn() {
        CPU.tapeMemory.currentValue = CPU.counterUnit.currentValue
    }
}