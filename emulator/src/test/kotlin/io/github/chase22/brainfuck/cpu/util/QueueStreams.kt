package io.github.chase22.brainfuck.cpu.util

import java.io.InputStream
import java.io.OutputStream
import java.util.concurrent.LinkedBlockingQueue

class QueueOutputStream : OutputStream() {
    private val queue = LinkedBlockingQueue<Int>()

    fun drainToString(): String {
        val array = mutableListOf<Int>()
        queue.drainTo(array)
        return String(array.map(Int::toChar).toCharArray())
    }

    fun clear() = queue.clear()
    fun take() = queue.take()

    override fun write(b: Int) {
        queue.add(b)
    }
}

class QueueInputStream : InputStream() {
    private val queue = LinkedBlockingQueue<Char>()

    fun write(string: String) {
        queue.addAll(string.toList())
    }

    override fun read(): Int = queue.take().code
}