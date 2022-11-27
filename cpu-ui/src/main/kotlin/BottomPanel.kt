package io.github.chase22.brainfuck.cpu

import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.event.ActionEvent
import javax.swing.JButton
import javax.swing.JPanel

class BottomPanel(
    private val onStepPressed: (ActionEvent) -> Unit,
    private val onResetPressed: (ActionEvent) -> Unit
) : JPanel(GridBagLayout()), Updatable {

    private val stepButton = JButton("Step").apply {
        addActionListener(onStepPressed)
    }

    init {
        val streamOutputField = StreamOutputField()
        add(
            streamOutputField,
            GridBagConstraints().apply { gridx = 0; gridy = 0; gridwidth = 2; fill = GridBagConstraints.HORIZONTAL })
        CPU.outputStream = streamOutputField.stream

        add(
            stepButton,
            GridBagConstraints().apply { gridx = 0; gridy = 1; fill = GridBagConstraints.HORIZONTAL; weightx = 1.0 }
        )

        add(JButton("Reset").apply {
            addActionListener(onResetPressed)
        }, GridBagConstraints().apply { gridx = 1; gridy = 1; fill = GridBagConstraints.HORIZONTAL; weightx = 1.0 })
    }

    override fun update() {
        stepButton.isEnabled = !CPU.controlLines.halt
    }
}