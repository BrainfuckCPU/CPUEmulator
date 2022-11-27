package io.github.chase22.brainfuck.cpu

import io.github.chase22.brainfuck.cpu.base.ByteInt

fun assemble(code: String): List<ByteInt> = code.toCharArray().map(::convert)

fun convert(char: Char): ByteInt = Command.fromCommand(char.toString())
    .ordinal
    .let { ByteInt(it) }