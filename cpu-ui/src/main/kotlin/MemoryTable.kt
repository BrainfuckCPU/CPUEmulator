package io.github.chase22.brainfuck.cpu

import io.github.chase22.brainfuck.cpu.base.Counter
import io.github.chase22.brainfuck.cpu.components.Memory
import java.awt.BorderLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTable
import javax.swing.event.TableModelListener
import javax.swing.table.TableModel

typealias Decoder = (Int) -> String

class MemoryTable(
    label: String,
    private val source: Memory,
    private val memoryCounter: Counter,
    val decoder: Decoder?
) :
    JPanel(BorderLayout()), Updatable {
    private val table = JTable()
    private val scrollPane = JScrollPane(table)

    init {
        add(JLabel(label), BorderLayout.NORTH)
        add(scrollPane, BorderLayout.CENTER)
        update()
    }

    override fun update() {
        table.model = MemoryTableModel(source, memoryCounter, decoder)
    }
}

class MemoryTableModel(private val source: Memory, private val memoryCounter: Counter, val decoder: Decoder? = null) :
    TableModel {
    override fun getRowCount(): Int = source.memory.size

    override fun getColumnCount(): Int = if (decoder != null) 4 else 3

    override fun getColumnName(columnIndex: Int): String = when (columnIndex) {
        0 -> "Active"
        1 -> "Index"
        2 -> "Value"
        3 -> "Decoded"
        else -> "Unknown Column"
    }

    override fun getColumnClass(columnIndex: Int): Class<*> = Int::class.java

    override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean = false

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any? =
        when (columnIndex) {
            0 -> if (memoryCounter.currentValue.value == rowIndex) ">" else ""
            1 -> rowIndex
            2 -> source.memory[rowIndex].value
            3 -> decoder?.invoke(source.memory[rowIndex].value)
            else -> null
        }

    override fun setValueAt(aValue: Any?, rowIndex: Int, columnIndex: Int) {
        throw UnsupportedOperationException()
    }

    override fun addTableModelListener(l: TableModelListener?) {}

    override fun removeTableModelListener(l: TableModelListener?) {}
}