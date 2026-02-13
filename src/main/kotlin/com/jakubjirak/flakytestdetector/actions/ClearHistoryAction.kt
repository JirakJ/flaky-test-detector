package com.jakubjirak.flakytestdetector.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.ui.Messages
import com.jakubjirak.flakytestdetector.storage.FlakyTestStorage

class ClearHistoryAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        val result = Messages.showYesNoDialog(
            project,
            "Are you sure you want to clear all test history? This action cannot be undone.",
            "Clear Flaky Test History",
            Messages.getWarningIcon()
        )

        if (result == Messages.YES) {
            val storage = FlakyTestStorage.getInstance(project)
            storage.clearAllHistory()
            LOG.info("All flaky test history cleared for ${project.name}")
        }
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = e.project != null
    }

    companion object {
        private val LOG = Logger.getInstance(ClearHistoryAction::class.java)
    }
}
