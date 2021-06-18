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
        println("\t" + (0..9).joinToString("\t"))
        println(memory.toList().chunked(10).map { it.joinToString("\t") }.mapIndexed {idx, value ->
            "${idx*10}\t$value"
        }.joinToString("\n"))
    }
}