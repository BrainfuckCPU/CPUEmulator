package io.github.chase22.brainfuck.cpu

import io.github.chase22.brainfuck.cpu.base.ByteInt
import java.awt.Color
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.border.LineBorder

class RegisterPane(private val value: () -> ByteInt, label: String) : JPanel(), Updatable {
    private val textField = JLabel().apply { border = LineBorder(Color.lightGray, 1, true) }

    init {
        val layout = GridBagLayout()
        this.layout = layout

        add(JLabel(label), GridBagConstraints().apply { gridx = 0; gridy = 0 })
        add(textField, GridBagConstraints().apply { gridx = 0; gridy = 1; fill = GridBagConstraints.HORIZONTAL })
        textField.size = Dimension(textField.height, this.width)

        update()
    }

    override fun update() {
        textField.text = value().toString(16)
    }
}

class Registers : JPanel(FlowLayout(FlowLayout.CENTER, 10, 10)), Updatable {
    init {
        add(RegisterPane(CPU.programCounter::currentValue, "Program Counter"))
        add(RegisterPane(CPU.microstepCounter::currentValue, "Microstep Counter"))
        add(RegisterPane(CPU.tapeMemoryCounter::currentValue, "Tape Memory Counter"))
        add(RegisterPane(CPU.loopCounter::currentValue, "Loop Counter"))
        add(RegisterPane(CPU.counterUnit::currentValue, "Counter Unit"))
    }

    override fun update() {
        this.components.filterIsInstance<Updatable>().forEach(Updatable::update)
    }
}