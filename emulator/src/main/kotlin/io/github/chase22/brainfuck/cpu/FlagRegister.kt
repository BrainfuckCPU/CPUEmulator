package io.github.chase22.brainfuck.cpu

class FlagRegister {
    val isInLoop: Boolean
        get() = CPU.loopCounter.isInLoop

    val loopForward: Boolean
        get() = CPU.loopCounter.currentValue.value shr 7 == 0

    val isCurrentLoopStart: Boolean
        get() = CPU.programMemory.currentInstruction == Command.LOOP_START

    val isCurrentLoopEnd: Boolean
        get() = CPU.programMemory.currentInstruction == Command.LOOP_END

    val currentZero: Boolean
        get() = CPU.tapeMemory.currentValue.value == 0
}