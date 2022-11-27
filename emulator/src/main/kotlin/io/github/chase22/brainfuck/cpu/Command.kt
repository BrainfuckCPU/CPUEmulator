package io.github.chase22.brainfuck.cpu

enum class Command(val command: String) {
    STOP("stop"),
    RESET("reset"),
    PLUS("+"),
    MINUS("-"),
    NEXT(">"),
    PREVIOUS("<"),
    OUTPUT("."),
    INPUT(","),
    LOOP_START("["),
    LOOP_END("]"),
    HALT_ON_ZERO("|");

    companion object {
        val values: Array<Command> by lazy { values() }

        fun fromCommand(command: String): Command = values
            .find { it.command == command } ?: throw IllegalArgumentException("No Instruction for Command $command")

        fun fromOrdinal(ordinal: Int): Command =
            values.find { it.ordinal == ordinal } ?: throw IndexOutOfBoundsException(ordinal)
    }

    override fun toString(): String {
        return "$command \t $ordinal \t ${ordinal.toString(2).padStart(4, '0')}"
    }
}