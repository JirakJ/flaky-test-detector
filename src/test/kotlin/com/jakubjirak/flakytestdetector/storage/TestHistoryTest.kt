package com.jakubjirak.flakytestdetector.storage

import com.jakubjirak.flakytestdetector.model.TestOutcome
import com.jakubjirak.flakytestdetector.model.TestRecord
import com.jakubjirak.flakytestdetector.model.TestRunResult
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.concurrent.TimeUnit

class TestHistoryTest {

    private val day = TimeUnit.DAYS.toMillis(1)
    private val now = 1_800_000_000_000L
    private val history = TestHistory()

    private fun run(outcome: TestOutcome, daysAgo: Long = 0) = TestRunResult(timestamp = now - daysAgo * day, outcome = outcome)

    private fun record(id: String, vararg outcomes: TestOutcome, policy: AutoQuarantinePolicy = AutoQuarantinePolicy.DISABLED) =
        outcomes.forEach { history.recordTestResult(id, id.substringBeforeLast('.'), id.substringAfterLast('.'), run(it), policy) }

    @Test
    fun `results accumulate per test id`() {
        record("com.a.FooTest.one", TestOutcome.PASS, TestOutcome.FAIL)
        record("com.a.FooTest.two", TestOutcome.PASS)
        assertEquals(2, history.getRecordCount())
        assertEquals(2, history.getRecord("com.a.FooTest.one")!!.results.size)
        assertNull(history.getRecord("missing"))
    }

    @Test
    fun `returned records are snapshots`() {
        record("c.T.m", TestOutcome.PASS)
        history.getRecords().single().results.clear()
        assertEquals(1, history.getRecord("c.T.m")!!.results.size)
    }

    @Test
    fun `flaky tests have a non-zero score`() {
        record("c.T.stable", TestOutcome.PASS, TestOutcome.PASS)
        record("c.T.flaky", TestOutcome.PASS, TestOutcome.FAIL, TestOutcome.PASS)
        assertEquals(listOf("c.T.flaky"), history.getFlakyTests().map { it.testId })
    }

    @Test
    fun `lookup by simple class name matches what the editor sees`() {
        record("com.example.FooTest.shouldWork", TestOutcome.PASS)
        assertEquals("com.example.FooTest.shouldWork", history.findBySimpleName("FooTest", "shouldWork")?.testId)
        assertNull(history.findBySimpleName("BarTest", "shouldWork"))
    }

    @Test
    fun `quarantine toggles and notifies`() {
        var notified = 0
        history.addListener { notified++ }
        record("c.T.m", TestOutcome.PASS)
        history.toggleQuarantine("c.T.m")
        assertTrue(history.getRecord("c.T.m")!!.quarantined)
        history.toggleQuarantine("c.T.m")
        assertFalse(history.getRecord("c.T.m")!!.quarantined)
        history.toggleQuarantine("unknown")
        assertEquals(3, notified)
    }

    @Test
    fun `auto quarantine applies once minimum runs and threshold are reached`() {
        val policy = AutoQuarantinePolicy(enabled = true, threshold = 60.0, minimumRuns = 4)
        record("c.T.m", TestOutcome.PASS, TestOutcome.FAIL, TestOutcome.PASS, policy = policy)
        assertFalse(history.getRecord("c.T.m")!!.quarantined, "only 3 runs so far")
        record("c.T.m", TestOutcome.FAIL, policy = policy)
        assertTrue(history.getRecord("c.T.m")!!.quarantined)
        assertFalse(AutoQuarantinePolicy(false, 0.0, 0).shouldQuarantine(history.getRecord("c.T.m")!!))
    }

    @Test
    fun `auto quarantine never triggers for stable tests`() {
        val policy = AutoQuarantinePolicy(enabled = true, threshold = 1.0, minimumRuns = 1)
        record("c.T.m", TestOutcome.PASS, TestOutcome.PASS, TestOutcome.PASS, policy = policy)
        assertFalse(history.getRecord("c.T.m")!!.quarantined)
    }

    @Test
    fun `retention cleanup drops old results and empty records`() {
        history.recordTestResult("c.T.old", "c.T", "old", run(TestOutcome.PASS, daysAgo = 40))
        history.recordTestResult("c.T.mixed", "c.T", "mixed", run(TestOutcome.PASS, daysAgo = 40))
        history.recordTestResult("c.T.mixed", "c.T", "mixed", run(TestOutcome.FAIL, daysAgo = 1))
        history.performRetentionCleanup(retentionDays = 30, now = now)
        assertNull(history.getRecord("c.T.old"))
        assertEquals(listOf(TestOutcome.FAIL), history.getRecord("c.T.mixed")!!.results.map { it.outcome })
    }

    @Test
    fun `clear removes everything and listeners can be removed`() {
        var notified = 0
        val listener: () -> Unit = { notified++ }
        history.addListener(listener)
        record("c.T.m", TestOutcome.PASS)
        history.removeListener(listener)
        history.clearAllHistory()
        assertEquals(0, history.getRecordCount())
        assertEquals(1, notified)
    }

    @Test
    fun `record exposes simple class name and score`() {
        val r = TestRecord("a.b.C.m", "a.b.C", "m", results = mutableListOf(run(TestOutcome.PASS), run(TestOutcome.FAIL)))
        assertEquals("C", r.simpleClassName)
        assertEquals(100.0, r.flakinessScore)
    }
}
