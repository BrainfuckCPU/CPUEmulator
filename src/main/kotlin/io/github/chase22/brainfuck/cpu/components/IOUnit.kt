package io.github.chase22.brainfuck.cpu.components

import io.github.chase22.brainfuck.cpu.CPU
import io.github.chase22.brainfuck.cpu.base.ByteInt

class IOUnit: ClockReceiver {
    fun read(): ByteInt = ByteInt(readLine()?.first()?.code ?: 0)

    override fun onClockTick(cycleCount: UInt) {
        if (ControlLines.ioUnitIn) {
            print(CPU.databus.currentValue.toChar())
        }
    }
}