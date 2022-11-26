package io.github.chase22.brainfuck.cpu.components

data class ControlLines(
    var counterUnitIn: Boolean = false,
    var counterUnitOut: Boolean = false,
    var counterUnitUp: Boolean = false,
    var counterUnitDown: Boolean = false,

    var programCounterUp: Boolean = false,
    var programCounterDown: Boolean = false,

    var microstepCounterHold: Boolean = false,
    var microstepCounterReset: Boolean = false,

    var tapeMemoryCounterUp: Boolean = false,
    var tapeMemoryCounterDown: Boolean = false,

    var tapeMemoryIn: Boolean = false,
    var tapeMemoryOut: Boolean = false,

    var ioUnitIn: Boolean = false,
    var ioUnitOut: Boolean = false,

    var loopCounterUp: Boolean = false,
    var loopCounterDown: Boolean = false,

    var halt: Boolean = false,
    var reset: Boolean = false,
) {
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
        halt = false
        reset = false
    }

    fun apply(other: ControlLines) {
        counterUnitUp = other.counterUnitUp
        counterUnitDown = other.counterUnitDown
        counterUnitIn = other.counterUnitIn
        counterUnitOut = other.counterUnitOut
        tapeMemoryIn = other.tapeMemoryIn
        tapeMemoryOut = other.tapeMemoryOut
        programCounterUp = other.programCounterUp
        programCounterDown = other.programCounterDown
        microstepCounterHold = other.microstepCounterHold
        microstepCounterReset = other.microstepCounterReset
        tapeMemoryCounterUp = other.tapeMemoryCounterUp
        tapeMemoryCounterDown = other.tapeMemoryCounterDown
        ioUnitIn = other.ioUnitIn
        ioUnitOut = other.ioUnitOut
        loopCounterUp = other.loopCounterUp
        loopCounterDown = other.loopCounterDown
        halt = other.halt
        reset = other.reset
    }
}