package io.github.chase22.brainfuck.cpu

import io.github.chase22.brainfuck.cpu.base.ByteInt
import kotlin.test.Test
import kotlin.test.assertEquals

class ByteIntClassSpec {

    @Test
    fun testAddition() {
        cartesianProduct(
            listOf(
                Triple(0, 0, 0),
                Triple(1, 0, 1),
                Triple(254, 1, 255),
                Triple(255, 1, 0),
                Triple(256, 1, 1),
                Triple(256, 0.5, 0),
                Triple(256, 1.5, 1)
            ),
            listOf<(a: Number) -> Number>(
                { ByteInt(it.toInt()) },
                { it.toInt() },
                { it.toFloat() },
                { it.toDouble() },
                { it.toLong() },
            )
        )
            .map { (data, mapper) -> Triple(ByteInt(data.first), mapper.invoke(data.second), ByteInt(data.third)) }
            .forEach { (a, b, result) ->
                println("$a + ${b.javaClass.simpleName}($b) = $result")
                assertEquals(result, a + b)
            }
    }

    @Test
    fun testSubtraction() {
        cartesianProduct(
            listOf(
                Triple(0, 0, 0),
                Triple(1, 0, 1),
                Triple(0, 1, 255),
                Triple(254, 1, 253),
                Triple(255, 1, 254),
                Triple(0, 0.5, 0),
                Triple(0, 1.5, 255)
            ),
            listOf<(a: Number) -> Number>(
                { ByteInt(it.toInt()) },
                { it.toInt() },
                { it.toFloat() },
                { it.toDouble() },
                { it.toLong() },
            )
        )
            .map { (data, mapper) -> Triple(ByteInt(data.first), mapper.invoke(data.second), ByteInt(data.third)) }
            .forEach { (a, b, result) ->
                println("$a - ${b.javaClass.simpleName}($b) = $result")
                assertEquals(result, a - b)
            }
    }

    @Test
    fun testAssignAddition() {
        var start = ByteInt(5)
        start += 5
        assertEquals(ByteInt(10), start)
    }

    @Test
    fun testAssignAdditionOverflow() {
        var start = ByteInt(255)
        start += 5
        assertEquals(ByteInt(4), start)
    }

    @Test
    fun testAssignSubtraction() {
        var start = ByteInt(10)
        start -= 5
        assertEquals(ByteInt(5), start)
    }

    @Test
    fun testAssignSubtractionOverflow() {
        var start = ByteInt(5)
        start -= 10
        assertEquals(ByteInt(251), start)
    }

    @Test
    fun testIncrement() {
        var start = ByteInt(5)
        start++
        assertEquals(ByteInt(6), start)
    }

    @Test
    fun testDecrement() {
        var start = ByteInt(5)
        start--
        assertEquals(ByteInt(4), start)
    }

    private fun <T, U> cartesianProduct(c1: Collection<T>, c2: Collection<U>): List<Pair<T, U>> {
        return c1.flatMap { lhsElem -> c2.map { rhsElem -> lhsElem to rhsElem } }
    }
}