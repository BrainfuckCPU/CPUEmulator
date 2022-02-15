package io.github.chase22.brainfuck.cpu.components

import io.github.chase22.brainfuck.cpu.CPU
import io.github.chase22.brainfuck.cpu.base.ByteInt

class IOUnit : ClockReceiver {
    fun read(): ByteInt = ByteInt(CPU.inputStream.read())

    override fun onClockTick(cycleCount: UInt) {
        if (ControlLines.ioUnitIn) {
            CPU.outputStream.write(CPU.databus.currentValue.value)
        }
    }
}