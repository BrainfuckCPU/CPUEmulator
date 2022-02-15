package io.github.chase22.brainfuck.cpu.util

import java.io.InputStream
import java.io.OutputStream
import java.util.concurrent.LinkedBlockingQueue

class QueueOutputStream : OutputStream() {
    private val queue = LinkedBlockingQueue<Char>()

    fun drainToString(): String {
        val charArray = mutableListOf<Char>()
        queue.drainTo(charArray)
        return String(charArray.toCharArray())
    }

    fun clear() {
        queue.clear()
    }

    override fun write(b: Int) {
        queue.add(b.toChar())
    }
}

class QueueInputStream : InputStream() {
    private val queue = LinkedBlockingQueue<Char>()

    fun write(string: String) {
        queue.addAll(string.toList())
    }

    override fun read(): Int = queue.take().code
}