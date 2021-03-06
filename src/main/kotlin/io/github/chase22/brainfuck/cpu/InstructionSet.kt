package io.github.chase22.brainfuck.cpu

enum class InstructionSet(val command: String) {
    STOP("stop"),
    RESET("reset"),
    PLUS("+"),
    MINUS("-"),
    NEXT(">"),
    PREVIOUS("<"),
    OUTPUT("."),
    INPUT(","),
    LOOP_START("["),
    LOOP_END("]");

    companion object {
        val values: Array<InstructionSet> by lazy { values() }

        fun fromCommand(command: String): InstructionSet = values
            .find { it.command == command } ?: throw IllegalArgumentException("No Instruction for Command $command")
    }

    override fun toString(): String {
        return "$command \t $ordinal \t ${ordinal.toString(2).padStart(4, '0')}"
    }
}