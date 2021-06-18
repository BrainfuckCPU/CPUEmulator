package io.github.chase22.brainfuck.cpu.components

import io.github.chase22.brainfuck.cpu.base.ByteInt

class CounterUnit {
    var currentValue = ByteInt(0)

    fun increment() = currentValue++
    fun decrement() = currentValue--

    fun reset() {
        currentValue = ByteInt(0)
    }
}