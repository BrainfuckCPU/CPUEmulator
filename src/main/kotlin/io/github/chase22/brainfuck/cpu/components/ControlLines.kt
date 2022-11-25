package io.github.chase22.brainfuck.cpu.components

object ControlLines {
    var counterUnitIn: Boolean = false
    var counterUnitOut: Boolean = false
    var counterUnitUp: Boolean = false
    var counterUnitDown: Boolean = false

    var programCounterUp: Boolean = false
    var programCounterDown: Boolean = false

    var microstepCounterHold: Boolean = false
    var microstepCounterReset: Boolean = false

    var tapeMemoryCounterUp: Boolean = false
    var tapeMemoryCounterDown: Boolean = false

    var tapeMemoryIn: Boolean = false
    var tapeMemoryOut: Boolean = false

    var ioUnitIn: Boolean = false
    var ioUnitOut: Boolean = false

    var loopCounterUp: Boolean = false
    var loopCounterDown: Boolean = false

    fun reset() {
        counterUnitUp = false
        counterUnitDown = false
        counterUnitIn = false
        counterUnitOut = false
        tapeMemoryIn = false
        tapeMemoryOut = false
        programCounterUp = false
        programCounterDown = false
        microstepCounterHold = false
        microstepCounterReset = false
        tapeMemoryCounterUp = false
        tapeMemoryCounterDown = false
        ioUnitIn = false
        ioUnitOut = false
        loopCounterUp = false
        loopCounterDown = false
    }
}