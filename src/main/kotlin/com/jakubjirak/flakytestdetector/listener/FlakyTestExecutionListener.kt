package com.jakubjirak.flakytestdetector.listener

import com.intellij.execution.testframework.AbstractTestProxy
import com.intellij.execution.testframework.TestStatusListener
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.ProjectManager
import com.jakubjirak.flakytestdetector.model.TestOutcome
import com.jakubjirak.flakytestdetector.model.TestRunResult
import com.jakubjirak.flakytestdetector.settings.FlakyTestSettings
import com.jakubjirak.flakytestdetector.storage.FlakyTestStorage

class FlakyTestExecutionListener : TestStatusListener() {

    override fun testSuiteFinished(root: AbstractTestProxy?) {
        if (root == null) return

        val settings = FlakyTestSettings.getInstance()
        if (!settings.enabled) return

        val openProjects = ProjectManager.getInstance().openProjects
        if (openProjects.isEmpty()) return

        val project = openProjects.first()
        val storage = FlakyTestStorage.getInstance(project)

        processTestNode(root, storage)
        LOG.info("Flaky Test Detector: recorded results from test suite")
    }

    private fun processTestNode(node: AbstractTestProxy, storage: FlakyTestStorage) {
        if (node.isLeaf) {
            recordLeafTest(node, storage)
        } else {
            node.children.forEach { child ->
                processTestNode(child, storage)
            }
        }
    }

    private fun recordLeafTest(test: AbstractTestProxy, storage: FlakyTestStorage) {
        val testName = test.name ?: return
        val locationUrl = test.locationUrl

        val className = extractClassName(locationUrl, testName)
        val methodName = extractMethodName(locationUrl, testName)
        val testId = "$className.$methodName"

        val outcome = mapOutcome(test)
        val duration = test.duration ?: 0L

        val result = TestRunResult(
            timestamp = System.currentTimeMillis(),
            outcome = outcome,
            durationMs = duration,
            stackTrace = if (outcome == TestOutcome.FAIL || outcome == TestOutcome.ERROR) {
                test.errorMessage
            } else {
                null
            }
        )

        storage.recordTestResult(
            testId = testId,
            className = className,
            methodName = methodName,
            result = result
        )
    }

    private fun mapOutcome(test: AbstractTestProxy): TestOutcome {
        return when {
            test.isPassed -> TestOutcome.PASS
            test.isInterrupted -> TestOutcome.SKIP
            test.isDefect -> TestOutcome.FAIL
            else -> TestOutcome.ERROR
        }
    }

    private fun extractClassName(locationUrl: String?, testName: String): String {
        if (locationUrl == null) return testName.substringBeforeLast(".", testName)
        val withoutProtocol = locationUrl.substringAfter("://", locationUrl)
        return withoutProtocol.substringBeforeLast("/", withoutProtocol)
            .replace("/", ".")
    }

    private fun extractMethodName(locationUrl: String?, testName: String): String {
        if (locationUrl == null) return testName.substringAfterLast(".", testName)
        val withoutProtocol = locationUrl.substringAfter("://", locationUrl)
        return withoutProtocol.substringAfterLast("/", testName)
    }

    companion object {
        private val LOG = Logger.getInstance(FlakyTestExecutionListener::class.java)
    }
}
