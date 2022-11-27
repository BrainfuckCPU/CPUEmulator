package io.github.chase22.brainfuck.cpu

import io.github.chase22.brainfuck.cpu.components.ControlLines
import java.awt.BorderLayout
import javax.swing.BoxLayout
import javax.swing.JPanel
import javax.swing.JRadioButton

class LightsPanel(controlLines: ControlLines, flagRegister: FlagRegister) : UpdatableJPPanel(BorderLayout()) {
    init {
        add(ControlLineLights(controlLines), BorderLayout.WEST)
        add(FlagRegisterLights(flagRegister), BorderLayout.EAST)
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

class FlagRegisterLights(private val flagRegister: FlagRegister) : Lights(
    listOf(
        "isInLoop" to flagRegister::isInLoop,
        "currentZero" to flagRegister::currentZero,
        "isCurrentLoopStart" to flagRegister::isCurrentLoopStart,
        "isCurrentLoopEnd" to flagRegister::isCurrentLoopEnd,

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