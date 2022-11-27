package io.github.chase22.brainfuck.cpu

import io.github.chase22.brainfuck.cpu.base.Counter
import io.github.chase22.brainfuck.cpu.components.Memory
import javax.swing.JTable
import javax.swing.event.TableModelListener
import javax.swing.table.JTableHeader
import javax.swing.table.TableModel

class MemoryTable(private val source: Memory, private val memoryCounter: Counter) : JTable(), Updatable {
    init {
        tableHeader = JTableHeader()
        update()
    }

    override fun update() {
        model = MemoryTableModel(source, memoryCounter)
    }
}

class MemoryTableModel(private val source: Memory, private val memoryCounter: Counter) : TableModel {
    override fun getRowCount(): Int = source.memory.size

    override fun getColumnCount(): Int = 3

    override fun getColumnName(columnIndex: Int): String = when (columnIndex) {
        0 -> "Active"
        1 -> "Index"
        2 -> "Value"
        else -> "Unknown Column"
    }

    override fun getColumnClass(columnIndex: Int): Class<*> = Int::class.java

    override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean = false

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any? =
        when (columnIndex) {
            0 -> if (memoryCounter.currentValue.value == rowIndex) ">" else ""
            1 -> rowIndex
            2 -> source.memory[rowIndex].value
            else -> null
        }

    override fun setValueAt(aValue: Any?, rowIndex: Int, columnIndex: Int) {
        throw UnsupportedOperationException()
    }

    override fun addTableModelListener(l: TableModelListener?) {}

    override fun removeTableModelListener(l: TableModelListener?) {}
}