package com.jakubjirak.flakytestdetector

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.jakubjirak.flakytestdetector.storage.FlakyTestStorage

class FlakyTestStartupActivity : ProjectActivity {

    override suspend fun execute(project: Project) {
        val storage = FlakyTestStorage.getInstance(project)
        storage.performRetentionCleanup()
        LOG.info("Flaky Test Detector: loaded ${storage.getRecordCount()} tracked tests for ${project.name}")
    }

    companion object {
        private val LOG = Logger.getInstance(FlakyTestStartupActivity::class.java)
    }
}
