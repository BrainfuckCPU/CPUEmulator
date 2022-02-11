package io.github.chase22.brainfuck.cpu.components

import io.github.chase22.brainfuck.cpu.CPU
import io.github.chase22.brainfuck.cpu.InstructionSet.*

class ControlLogic {
    fun run() {
        while(true) {
            when (CPU.programMemory.currentInstruction) {
                PLUS -> increment()
                MINUS -> decrement()
                NEXT -> shiftLeft()
                PREVIOUS -> shiftRight()
                OUTPUT -> output()
                INPUT -> input()
                LOOP_START -> loopStart()
                LOOP_END -> loopEnd()
                else -> break
            }
            CPU.programCounter.countUp()
        }
    }

    private fun loopStart() {
        if (CPU.tapeMemory.currentValue.value == 0) {
            while (CPU.programMemory.currentInstruction != LOOP_END) {
                CPU.programCounter.countUp()
            }
        }
    }

    private fun loopEnd() {
        if (CPU.tapeMemory.currentValue.value != 0) {
            while (CPU.programMemory.currentInstruction != LOOP_START) {
                CPU.programCounter.countDown()
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