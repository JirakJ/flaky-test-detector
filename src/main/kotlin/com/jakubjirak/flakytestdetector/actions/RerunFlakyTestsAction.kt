package com.jakubjirak.flakytestdetector.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import com.jakubjirak.flakytestdetector.storage.FlakyTestStorage

class RerunFlakyTestsAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val storage = FlakyTestStorage.getInstance(project)
        val flakyTests = storage.getFlakyTests()

        if (flakyTests.isEmpty()) {
            LOG.info("No flaky tests found to rerun")
            return
        }

        LOG.info("Found ${flakyTests.size} flaky tests to rerun")
        // TODO: Integrate with IntelliJ test runner to rerun selected tests
        // This requires creating a RunConfiguration programmatically
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = e.project != null
    }

    companion object {
        private val LOG = Logger.getInstance(RerunFlakyTestsAction::class.java)
    }
}
