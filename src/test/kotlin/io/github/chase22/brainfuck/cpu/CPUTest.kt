package io.github.chase22.brainfuck.cpu

import io.github.chase22.brainfuck.cpu.util.QueueInputStream
import io.github.chase22.brainfuck.cpu.util.QueueOutputStream
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

private const val HELLO_WORLD_CODE =
    ">++++++++[<+++++++++>-]<.>++++[<+++++++>-]<+.+++++++..+++.>>++++++[<+++++++>-]<++.------------.>++++++[<+++++++++>-]<+.<.+++.------.--------.>>>++++[<++++++++>-]<+."

class CPUTest {

    @BeforeEach
    fun setup() {
        testOutputStream.clear()
    }

    @Test
    fun testSimpleLoops() {
        CPU.debugOutput = true
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

    @Test
    fun testHelloWorld() {
        CPU.loadProgram(HELLO_WORLD_CODE)
        CPU.run()
        assertEquals("Hello, World!", testOutputStream.drainToString())
    }

    @Test
    fun testInput() {
        testInputStream.write("A")
        CPU.loadProgram(",")
        CPU.run()
        assertEquals('A'.code, CPU.tapeMemory.memory[0].value)
    }

    companion object {
        private val testInputStream = QueueInputStream()
        private val testOutputStream = QueueOutputStream()

        @BeforeAll
        @JvmStatic
        fun setupAll() {
            CPU.inputStream = testInputStream
            CPU.outputStream = testOutputStream
        }
    }
}