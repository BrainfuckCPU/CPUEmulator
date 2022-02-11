package io.github.chase22.brainfuck.cpu

fun main() {
//    CPU.programMemory.load(assemble("+".repeat(72) + ".---."))
    CPU.programMemory.load(assemble(
        ">++++++++[<+++++++++>-]<.>++++[<+++++++>-]<+.+++++++..+++.>>++++++[<+++++++>-]<++.------------.>++++++[<+++++++++>-]<+.<.+++.------.--------.>>>++++[<++++++++>-]<+."
    ))
    CPU.run()
    CPU.tapeMemory.print()
    println("finished")
}