package com.jakubjirak.flakytestdetector.ui

import com.jakubjirak.flakytestdetector.core.FlakinessCalculator
import com.jakubjirak.flakytestdetector.model.FlakinessCategory
import com.jakubjirak.flakytestdetector.model.TestRecord
import com.jakubjirak.flakytestdetector.storage.FlakyTestStorage
import java.awt.BorderLayout
import java.awt.FlowLayout
import javax.swing.JComboBox
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTable
import javax.swing.table.DefaultTableModel

@Suppress("MagicNumber")
class FlakyTestPanel(private val storage: FlakyTestStorage) : JPanel(BorderLayout()) {

    private val calculator = FlakinessCalculator()
    private val columns = arrayOf("Test", "Class", "Score", "Last 10", "Trend", "Quarantined")
    private val tableModel = ReadOnlyTableModel(columns, 0)
    val table = JTable(tableModel)
    private val filterCombo = JComboBox(arrayOf("All", "Flaky Only", "Stable Only", "Quarantined"))

    init {
        table.autoCreateRowSorter = true
        table.setDefaultRenderer(Any::class.java, null)

        val sparklineColumn = table.columnModel.getColumn(3)
        sparklineColumn.cellRenderer = SparklineRenderer()
        sparklineColumn.preferredWidth = 140

        val filterPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        filterPanel.add(JLabel("Filter:"))
        filterPanel.add(filterCombo)
        filterCombo.addActionListener { refreshTable() }

        add(filterPanel, BorderLayout.NORTH)
        add(com.intellij.ui.components.JBScrollPane(table), BorderLayout.CENTER)

        storage.addListener { refreshTable() }
        refreshTable()
    }

    fun refreshTable() {
        tableModel.setRowCount(0)
        val records = getFilteredRecords()

        records.forEach { record ->
            val score = record.flakinessScore
            val category = FlakinessCategory.fromScore(score)
            val trend = calculator.calculateTrend(record.results)

            tableModel.addRow(
                arrayOf(
                    record.methodName,
                    record.className,
                    "${category.displayName} (${String.format("%.1f", score)}%)",
                    record.results,
                    trend.symbol + " " + trend.displayName,
                    if (record.quarantined) "Yes" else "No"
                )
            )
        }
    }

    private fun getFilteredRecords(): List<TestRecord> {
        val allRecords = storage.getRecords()
        return when (filterCombo.selectedIndex) {
            1 -> allRecords.filter { it.flakinessScore > 0.0 }
            2 -> allRecords.filter { it.flakinessScore == 0.0 }
            3 -> allRecords.filter { it.quarantined }
            else -> allRecords
        }
    }

    fun getSelectedTestId(): String? {
        val selectedRow = table.selectedRow
        if (selectedRow < 0) return null
        val modelRow = table.convertRowIndexToModel(selectedRow)
        val methodName = tableModel.getValueAt(modelRow, 0) as String
        val className = tableModel.getValueAt(modelRow, 1) as String
        return "$className.$methodName"
    }
}

class ReadOnlyTableModel(columns: Array<String>, rowCount: Int) : DefaultTableModel(columns, rowCount) {
    override fun isCellEditable(row: Int, column: Int): Boolean = false
}
