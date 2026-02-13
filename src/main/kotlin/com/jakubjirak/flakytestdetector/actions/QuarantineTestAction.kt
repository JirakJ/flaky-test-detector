package com.jakubjirak.flakytestdetector.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import com.jakubjirak.flakytestdetector.storage.FlakyTestStorage

class QuarantineTestAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val storage = FlakyTestStorage.getInstance(project)

        // Toggle quarantine on the first flaky test if no specific selection
        val flakyTests = storage.getFlakyTests()
        if (flakyTests.isNotEmpty()) {
            val testId = flakyTests.first().testId
            storage.toggleQuarantine(testId)
            val record = storage.getRecord(testId)
            LOG.info("Toggled quarantine for $testId: quarantined=${record?.quarantined}")
        }
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = e.project != null
    }

    companion object {
        private val LOG = Logger.getInstance(QuarantineTestAction::class.java)
    }
}
