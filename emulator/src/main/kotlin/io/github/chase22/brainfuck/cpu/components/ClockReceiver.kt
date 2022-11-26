package io.github.chase22.brainfuck.cpu.components

interface ClockReceiver {
    fun onClockTick(cycleCount: UInt)
}