package io.github.chase22.brainfuck.cpu

import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.LayoutManager
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JScrollPane

fun main() {
    MainFrame()
}

class MainFrame : JFrame("CPU"), Updatable {
    private val lights = LightsPanel(CPU.controlLines, CPU.flagRegister)
    private val tapeTable = MemoryTable(CPU.tapeMemory, CPU.tapeMemoryCounter)
    private val programTable = MemoryTable(CPU.programMemory, CPU.programCounter)

    private val registers = Registers()
    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        isVisible = true
        layout = BorderLayout()

        CPU.loadProgram("++.")

        add(lights, BorderLayout.CENTER)
        add(JScrollPane(tapeTable), BorderLayout.WEST)
        add(JScrollPane(programTable), BorderLayout.EAST)
        add(BottomPanel({
            CPU.step()
            update()
        }) { CPU.reset(); update() }, BorderLayout.SOUTH)

        add(registers, BorderLayout.NORTH)

        extendedState = MAXIMIZED_BOTH;
    }

    override fun update() {
        this.contentPane.components.filterIsInstance<Updatable>().forEach(Updatable::update)
    }
}

abstract class UpdatableJPPanel(layout: LayoutManager = FlowLayout()) : JPanel(layout), Updatable {
    override fun update() {
        this.components.filterIsInstance<Updatable>().forEach(Updatable::update)
    }
}

interface Updatable {
    fun update()
}