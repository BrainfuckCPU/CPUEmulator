package io.github.chase22.brainfuck.cpu.base

class Counter {
    var currentValue: ByteInt = ByteInt(0)
        private set

    fun countUp() = currentValue++
    fun countDown() = currentValue--
    fun reset() {
        currentValue = ByteInt(0)
    }
}