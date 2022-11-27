package io.github.chase22.brainfuck.cpu

import io.github.chase22.brainfuck.cpu.components.ControlLines
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.BoxLayout
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JRadioButton
import javax.swing.JScrollPane

fun main() {
    MainFrame()
}

class MainFrame : JFrame("CPU"), Updatable {
    private val lights = ControlLineLights(CPU.controlLines)
    private val tapeTable = MemoryTable(CPU.tapeMemory, CPU.tapeMemoryCounter)
    private val programTable = MemoryTable(CPU.programMemory, CPU.programCounter)

    private val registers = Registers()

    private val updatables: List<Updatable> = listOf(lights, tapeTable, programTable, registers)

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        size = Dimension(600, 400)
        isVisible = true
        layout = BorderLayout()

        CPU.loadProgram("++.")

        add(lights, BorderLayout.CENTER)
        add(JScrollPane(tapeTable), BorderLayout.WEST)
        add(JScrollPane(programTable), BorderLayout.EAST)
        add(BottomPanel {
            CPU.step()
            update()
        }, BorderLayout.SOUTH)

        add(registers, BorderLayout.NORTH)
    }

    override fun update() {
        updatables.forEach(Updatable::update)
    }
}

class ControlLineLights(private val controlLines: ControlLines) :
    Lights(
        listOf(
            "counterUnitUp" to controlLines::counterUnitUp,
            "counterUnitDown" to controlLines::counterUnitDown,
            "counterUnitIn" to controlLines::counterUnitIn,
            "counterUnitOut" to controlLines::counterUnitOut,
            "tapeMemoryIn" to controlLines::tapeMemoryIn,
            "tapeMemoryOut" to controlLines::tapeMemoryOut,
            "programCounterUp" to controlLines::programCounterUp,
            "programCounterDown" to controlLines::programCounterDown,
            "microstepCounterHold" to controlLines::microstepCounterHold,
            "microstepCounterReset" to controlLines::microstepCounterReset,
            "tapeMemoryCounterUp" to controlLines::tapeMemoryCounterUp,
            "tapeMemoryCounterDown" to controlLines::tapeMemoryCounterDown,
            "ioUnitIn" to controlLines::ioUnitIn,
            "ioUnitOut" to controlLines::ioUnitOut,
            "loopCounterUp" to controlLines::loopCounterUp,
            "loopCounterDown" to controlLines::loopCounterDown,
            "halt" to controlLines::halt,
            "reset" to { controlLines.reset }
        )
    )

open class Lights(
    lights: List<Pair<String, () -> Boolean>>
) : JPanel(), Updatable {
    init {
        val layout = BoxLayout(this, BoxLayout.PAGE_AXIS)
        this.layout = layout
    }

    private val lights = lights.map {
        Light(it.first, it.second).also { light ->
            add(light)
        }
    }

    override fun update() {
        lights.forEach(Light::update)
    }
}

class Light(label: String, private val state: () -> Boolean) : JPanel(), Updatable {
    private val radioButton = JRadioButton(label, state())
        .apply { isEnabled = false }
        .also(::add)

    override fun update() {
        radioButton.isSelected = state()
    }
}

interface Updatable {
    fun update()
}