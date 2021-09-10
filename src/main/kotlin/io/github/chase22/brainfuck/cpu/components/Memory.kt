package io.github.chase22.brainfuck.cpu.components

import io.github.chase22.brainfuck.cpu.base.ByteInt
import io.github.chase22.brainfuck.cpu.base.Counter

class Memory(private val memoryCounter: Counter) {
    private val memory: Array<ByteInt> = Array(256) { ByteInt(0) }

    var currentValue
        get() = memory[memoryCounter.currentValue.toInt()]
        set(value) {
            memory[memoryCounter.currentValue.toInt()] = value
        }

    fun reset() {
        memory.copyOf().forEachIndexed { index, _ ->
            memory[index] = ByteInt(0)
        }
    }

    fun load(content: List<ByteInt>) {
        memory.copyOf().forEachIndexed { index, _ ->
            memory[index] = content.getOrElse(index) { ByteInt(0) }
        }
    }

    fun print() {
        println("    " + (0..9).joinToString(" ", transform = this::cell))
        println(memory.toList().chunked(10)
            .map { it.joinToString(" ", transform = this::cell) }
            .mapIndexed { idx, value ->
                "${cell(idx * 10)} $value"
            }.joinToString("\n")
        )
    }

    fun cell(value: Int): String = value.toString().padStart(3)
    fun cell(value: ByteInt): String = cell(value.toInt())
}