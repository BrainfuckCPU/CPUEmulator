package io.github.chase22.brainfuck.cpu

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CPUTest {

    @Test
    fun testSimpleLoops() {
        CPU.loadProgram("++[>+<-]")
        CPU.run()
        CPU.tapeMemory.print()
        assertEquals(2, CPU.tapeMemory.memory[1].value)
    }

    @Test
    fun testNestedLoops() {
        CPU.loadProgram("++[>++++[>+<-]<-]")
        CPU.run()
        CPU.tapeMemory.print()
        assertEquals(8, CPU.tapeMemory.memory[2].value)
    }
}