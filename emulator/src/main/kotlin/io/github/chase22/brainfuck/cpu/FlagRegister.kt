package io.github.chase22.brainfuck.cpu

class FlagRegister {
    val isInLoop: Boolean
        get() = CPU.loopCounter.isInLoop

    val isCurrentLoopStart: Boolean
        get() = CPU.programMemory.currentInstruction == InstructionSet.LOOP_START

    val isCurrentLoopEnd: Boolean
        get() = CPU.programMemory.currentInstruction == InstructionSet.LOOP_END

    val currentZero: Boolean
        get() = CPU.tapeMemory.currentValue.value == 0
}