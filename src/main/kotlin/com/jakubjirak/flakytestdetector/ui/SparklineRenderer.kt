package com.jakubjirak.flakytestdetector.ui

import com.jakubjirak.flakytestdetector.model.FlakinessCategory
import com.jakubjirak.flakytestdetector.model.TestOutcome
import com.jakubjirak.flakytestdetector.model.TestRunResult
import java.awt.Color
import java.awt.Component
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import javax.swing.JPanel
import javax.swing.JTable
import javax.swing.table.TableCellRenderer

@Suppress("MagicNumber")
class SparklineRenderer : TableCellRenderer {

    private var results: List<TestRunResult> = emptyList()

    override fun getTableCellRendererComponent(
        table: JTable,
        value: Any?,
        isSelected: Boolean,
        hasFocus: Boolean,
        row: Int,
        column: Int
    ): Component {
        @Suppress("UNCHECKED_CAST")
        results = (value as? List<TestRunResult>) ?: emptyList()

        return object : JPanel() {
            override fun paintComponent(g: Graphics) {
                super.paintComponent(g)
                val g2 = g as Graphics2D
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

                if (isSelected) {
                    background = table.selectionBackground
                } else {
                    background = table.background
                }

                val displayResults = results.takeLast(DISPLAY_COUNT)
                if (displayResults.isEmpty()) return

                val dotSize = 8
                val spacing = 12
                val startX = 4
                val centerY = height / 2

                displayResults.forEachIndexed { index, result ->
                    val color = when (result.outcome) {
                        TestOutcome.PASS -> FlakinessCategory.STABLE.color
                        TestOutcome.FAIL -> FlakinessCategory.HIGHLY_UNSTABLE.color
                        TestOutcome.ERROR -> Color(156, 39, 176)
                        TestOutcome.SKIP -> Color.GRAY
                    }
                    g2.color = color
                    g2.fillOval(startX + index * spacing, centerY - dotSize / 2, dotSize, dotSize)
                }
            }
        }
    }

    companion object {
        private const val DISPLAY_COUNT = 10
    }
}
