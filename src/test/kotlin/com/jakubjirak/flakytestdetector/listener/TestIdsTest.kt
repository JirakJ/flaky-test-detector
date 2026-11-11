package com.jakubjirak.flakytestdetector.listener

import com.jakubjirak.flakytestdetector.model.FlakinessCategory
import com.jakubjirak.flakytestdetector.model.FlakinessThresholds
import com.jakubjirak.flakytestdetector.model.TestOutcome
import com.jakubjirak.flakytestdetector.model.TestRecord
import com.jakubjirak.flakytestdetector.model.TestRunResult
import com.jakubjirak.flakytestdetector.ui.FlakyGutterIconProvider
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TestIdsTest {

    @Test
    fun `parses JUnit location urls`() {
        assertEquals(TestId("com.example.FooTest", "shouldWork"), TestIds.parse("java:test://com.example.FooTest/shouldWork", "shouldWork"))
        assertEquals("com.example.FooTest.shouldWork", TestIds.parse("java:test://com.example.FooTest/shouldWork", "x")!!.key)
    }

    @Test
    fun `parameterised invocations fold into their method`() {
        assertEquals("sum", TestIds.parse("java:test://com.example.MathTest/sum(int, int)[2]", "sum")!!.methodName)
        assertEquals("sum", TestIds.parse("java:test://com.example.MathTest/sum[1]", "sum")!!.methodName)
    }

    @Test
    fun `falls back to the test name without a location`() {
        assertEquals(TestId("com.example.FooTest", "bar"), TestIds.parse(null, "com.example.FooTest.bar"))
        assertNull(TestIds.parse(null, "bar"))
        assertNull(TestIds.parse("java:suite://com.example.FooTest", "FooTest"))
    }

    @Test
    fun `outcome mapping prefers ignored and interrupted over pass`() {
        assertEquals(TestOutcome.SKIP, TestIds.outcome(passed = true, ignored = true, interrupted = false, defect = false))
        assertEquals(TestOutcome.SKIP, TestIds.outcome(passed = false, ignored = false, interrupted = true, defect = true))
        assertEquals(TestOutcome.PASS, TestIds.outcome(passed = true, ignored = false, interrupted = false, defect = false))
        assertEquals(TestOutcome.FAIL, TestIds.outcome(passed = false, ignored = false, interrupted = false, defect = true))
        assertEquals(TestOutcome.ERROR, TestIds.outcome(passed = false, ignored = false, interrupted = false, defect = false))
    }

    @Test
    fun `category thresholds are configurable`() {
        val strict = FlakinessThresholds(suspicious = 1.0, flaky = 5.0, highlyUnstable = 20.0)
        assertEquals(FlakinessCategory.FLAKY, FlakinessCategory.fromScore(10.0, strict))
        assertEquals(FlakinessCategory.SUSPICIOUS, FlakinessCategory.fromScore(10.0))
        assertEquals(FlakinessCategory.HIGHLY_UNSTABLE, FlakinessCategory.fromScore(25.0, strict))
        assertEquals(FlakinessCategory.STABLE, FlakinessCategory.fromScore(0.0, FlakinessThresholds(0.0, 5.0, 20.0)))
        assertEquals(FlakinessCategory.STABLE, FlakinessCategory.fromScore(3.0, FlakinessThresholds(5.0, 10.0, 20.0)))
    }

    @Test
    fun `gutter tooltip describes score, runs and quarantine`() {
        val record = TestRecord(
            "c.T.m", "c.T", "m",
            results = mutableListOf(TestRunResult(outcome = TestOutcome.PASS), TestRunResult(outcome = TestOutcome.FAIL)),
            quarantined = true,
        )
        val tooltip = FlakyGutterIconProvider.tooltip(record, FlakinessCategory.HIGHLY_UNSTABLE)
        assertEquals("Flaky: 100.0% (Highly Unstable) — 2 runs — quarantined", tooltip)
        FlakinessCategory.entries.forEach { assertTrue(FlakyGutterIconProvider.iconFor(it).iconWidth > 0) }
    }
}
