package io.github.chase22.brainfuck.cpu

import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.event.ActionEvent
import javax.swing.JButton
import javax.swing.JPanel

class BottomPanel(private val onButtonPress: (ActionEvent) -> Unit) : JPanel(GridBagLayout()) {
    init {
        val streamOutputField = StreamOutputField()
        add(
            streamOutputField,
            GridBagConstraints().apply { gridx = 0; gridy = 0; fill = GridBagConstraints.HORIZONTAL })
        CPU.outputStream = streamOutputField.stream

        add(JButton("Step").apply {
            addActionListener(onButtonPress)
        }, GridBagConstraints().apply { gridx = 0; gridy = 1; fill = GridBagConstraints.HORIZONTAL })
    }
}