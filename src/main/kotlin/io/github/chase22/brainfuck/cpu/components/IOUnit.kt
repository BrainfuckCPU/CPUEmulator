package io.github.chase22.brainfuck.cpu.components

import io.github.chase22.brainfuck.cpu.base.ByteInt

class IOUnit {
    fun read(): ByteInt = ByteInt(readLine()?.first()?.toInt() ?: 0)

    fun write(value: ByteInt) = print(value.toChar())
}