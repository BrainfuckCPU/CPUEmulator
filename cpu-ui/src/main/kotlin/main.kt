package io.github.chase22.brainfuck.cpu

import java.awt.BorderLayout
import java.awt.FlowLayout
import java.awt.LayoutManager
import javax.swing.JFrame
import javax.swing.JPanel

fun main() {
    MainFrame()
}

class MainFrame : JFrame("CPU"), Updatable {
    private val lights = LightsPanel(CPU.controlLines, CPU.flagRegister)
    private val tapeTable = MemoryTable("Tape Memory", CPU.tapeMemory, CPU.tapeMemoryCounter, null)
    private val programTable =
        MemoryTable("Program Memory", CPU.programMemory, CPU.programCounter) { Command.fromOrdinal(it).command }

    private val registers = Registers()

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        isVisible = true
        layout = BorderLayout()

        CPU.loadProgram("++[-]")
        CPU.setup()

        add(lights, BorderLayout.CENTER)
        add(tapeTable, BorderLayout.WEST)
        add(programTable, BorderLayout.EAST)
        add(BottomPanel({
            CPU.tick()
            CPU.setup()
            update()
        }, { CPU.stepToNextInstruction(); update() }) { CPU.reset(); update() }, BorderLayout.SOUTH)

        add(registers, BorderLayout.NORTH)

        extendedState = MAXIMIZED_BOTH

        update()
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