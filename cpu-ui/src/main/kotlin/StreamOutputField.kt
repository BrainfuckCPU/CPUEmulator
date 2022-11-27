package io.github.chase22.brainfuck.cpu

import java.io.OutputStream
import javax.swing.JTextArea

class StreamOutputField : JTextArea() {
    val stream = TextAreaOutputStream(this)

    init {
        rows = 10
    }
}

class TextAreaOutputStream(private val textArea: JTextArea) : OutputStream() {
    override fun write(b: Int) {
        textArea.append(b.toChar().toString())
    }

}