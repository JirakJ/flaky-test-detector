package com.jakubjirak.flakytestdetector.ui

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory
import com.jakubjirak.flakytestdetector.storage.FlakyTestStorage
import java.awt.BorderLayout
import java.awt.FlowLayout
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel

class FlakyTestToolWindowFactory : ToolWindowFactory, DumbAware {

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val storage = FlakyTestStorage.getInstance(project)

        val mainPanel = JPanel(BorderLayout())

        val summaryLabel = JLabel()
        updateSummaryLabel(storage, summaryLabel)
        val summaryPanel = JPanel(FlowLayout(FlowLayout.LEFT))
        summaryPanel.add(summaryLabel)
        mainPanel.add(summaryPanel, BorderLayout.NORTH)

        val flakyTestPanel = FlakyTestPanel(storage)
        mainPanel.add(flakyTestPanel, BorderLayout.CENTER)

        val actionsPanel = createActionsPanel(storage, flakyTestPanel)
        mainPanel.add(actionsPanel, BorderLayout.SOUTH)

        storage.addListener {
            updateSummaryLabel(storage, summaryLabel)
        }

        val content = ContentFactory.getInstance().createContent(mainPanel, "Dashboard", false)
        toolWindow.contentManager.addContent(content)
    }

    private fun updateSummaryLabel(storage: FlakyTestStorage, label: JLabel) {
        val totalTests = storage.getRecordCount()
        val flakyTests = storage.getFlakyTests().size
        val quarantined = storage.getRecords().count { it.quarantined }
        label.text = "Total: $totalTests  |  Flaky: $flakyTests  |  Quarantined: $quarantined"
    }

    private fun createActionsPanel(storage: FlakyTestStorage, flakyTestPanel: FlakyTestPanel): JPanel {
        val panel = JPanel(FlowLayout(FlowLayout.LEFT))

        val refreshButton = JButton("Refresh")
        refreshButton.addActionListener {
            flakyTestPanel.refreshTable()
        }

        val quarantineButton = JButton("Toggle Quarantine")
        quarantineButton.addActionListener {
            val testId = flakyTestPanel.getSelectedTestId()
            if (testId != null) {
                storage.toggleQuarantine(testId)
            }
        }

        panel.add(refreshButton)
        panel.add(quarantineButton)
        return panel
    }
}
