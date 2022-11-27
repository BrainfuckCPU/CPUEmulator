package io.github.chase22.brainfuck.cpu.base

import kotlin.math.floor

class ByteInt(value: Int) : Number() {

    var value: Int = clampValue(value)
        set(newValue) {
            field = clampValue(newValue)
        }

    private fun clampValue(value: Int): Int = value - (floor(value.toDouble() / 256) * 256).toInt()

    operator fun plus(that: Number) = ByteInt(value + that.toInt())
    operator fun minus(that: Number) = ByteInt(value - that.toInt())
    operator fun inc(): ByteInt {
        this.value++
        return this
    }

    operator fun dec(): ByteInt {
        this.value--
        return this
    }

    operator fun unaryPlus() = this.value

    override fun toString() = toString(10)
    fun toString(radix: Int = 10) = value.toString(radix)

    override fun hashCode(): Int = value.hashCode()

    override fun equals(other: Any?): Boolean {
        if (other is ByteInt) {
            return value == other.value
        }
        if (other is Number) {
            return value == other
        }
        return false
    }

    override fun toInt(): Int = value
    override fun toByte(): Byte = value.toByte()
    override fun toChar(): Char = value.toChar()
    override fun toDouble(): Double = value.toDouble()
    override fun toFloat(): Float = value.toFloat()
    override fun toLong(): Long = value.toLong()
    override fun toShort(): Short = value.toShort()

}