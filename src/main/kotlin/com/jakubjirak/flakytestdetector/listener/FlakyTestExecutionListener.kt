package com.jakubjirak.flakytestdetector.listener

import com.intellij.execution.testframework.AbstractTestProxy
import com.intellij.execution.testframework.TestStatusListener
import com.intellij.openapi.project.Project
import com.jakubjirak.flakytestdetector.model.TestOutcome
import com.jakubjirak.flakytestdetector.model.TestRunResult
import com.jakubjirak.flakytestdetector.settings.FlakyTestSettings
import com.jakubjirak.flakytestdetector.storage.AutoQuarantinePolicy
import com.jakubjirak.flakytestdetector.storage.FlakyTestStorage

/** Records the outcome of every leaf test when a test run finishes. */
class FlakyTestExecutionListener : TestStatusListener() {

    override fun testSuiteFinished(root: AbstractTestProxy?) = Unit

    // The project-aware overload: results go to the project that ran the tests,
    // not to whichever project happens to be open first.
    override fun testSuiteFinished(root: AbstractTestProxy?, project: Project?) {
        if (root == null || project == null || project.isDisposed) return
        val settings = FlakyTestSettings.getInstance()
        if (!settings.enabled) return
        val policy = AutoQuarantinePolicy(settings.autoQuarantine, settings.autoQuarantineThreshold, settings.minimumRuns)
        val storage = FlakyTestStorage.getInstance(project)
        val now = System.currentTimeMillis()
        root.allTests.filter { it.isLeaf }.forEach { record(it, storage, policy, now) }
    }

    private fun record(test: AbstractTestProxy, storage: FlakyTestStorage, policy: AutoQuarantinePolicy, now: Long) {
        val id = TestIds.parse(test.locationUrl, test.name ?: return) ?: return
        val outcome = TestIds.outcome(
            passed = test.isPassed, ignored = test.isIgnored, interrupted = test.isInterrupted, defect = test.isDefect
        )
        val result = TestRunResult(
            timestamp = now,
            outcome = outcome,
            durationMs = test.duration ?: 0L,
            stackTrace = test.errorMessage?.takeIf { outcome == TestOutcome.FAIL || outcome == TestOutcome.ERROR },
        )
        storage.recordTestResult(id.key, id.className, id.methodName, result, policy = policy)
    }
}

/** Identity of a test method as recorded in history. */
data class TestId(val className: String, val methodName: String) {
    /** The id under which the test's history is stored. */
    val key: String get() = "$className.$methodName"
}

/** Pure helpers for turning test-runner data into history entries. */
object TestIds {

    /**
     * Parses a test location such as `java:test://com.example.FooTest/testBar` (JUnit/TestNG,
     * Java or Kotlin). Falls back to `Class.method` style [testName] when there is no URL.
     * Parameterised invocations (`testBar(int)[2]`) are folded into their method.
     */
    fun parse(locationUrl: String?, testName: String): TestId? {
        val path = locationUrl?.substringAfter("://", "")?.takeIf { it.isNotEmpty() }
        val (cls, method) = if (path != null && '/' in path) {
            path.substringBeforeLast('/') to path.substringAfterLast('/')
        } else {
            testName.substringBeforeLast('.', "") to testName.substringAfterLast('.')
        }
        val methodName = method.substringBefore('(').substringBefore('[').trim()
        val className = cls.replace('/', '.').trim()
        if (className.isEmpty() || methodName.isEmpty()) return null
        return TestId(className, methodName)
    }

    fun outcome(passed: Boolean, ignored: Boolean, interrupted: Boolean, defect: Boolean): TestOutcome = when {
        ignored || interrupted -> TestOutcome.SKIP
        passed -> TestOutcome.PASS
        defect -> TestOutcome.FAIL
        else -> TestOutcome.ERROR
    }
}
