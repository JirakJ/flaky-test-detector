package com.jakubjirak.flakytestdetector.core

import com.jakubjirak.flakytestdetector.model.TestOutcome
import com.jakubjirak.flakytestdetector.model.TestRunResult
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class FlakinessCalculatorTest {

    private val calculator = FlakinessCalculator()

    @Test
    fun `score is zero for all passing tests`() {
        val results = listOf(
            TestRunResult(outcome = TestOutcome.PASS),
            TestRunResult(outcome = TestOutcome.PASS),
            TestRunResult(outcome = TestOutcome.PASS)
        )
        assertEquals(0.0, calculator.calculateScore(results))
    }

    @Test
    fun `score is zero for all failing tests`() {
        val results = listOf(
            TestRunResult(outcome = TestOutcome.FAIL),
            TestRunResult(outcome = TestOutcome.FAIL),
            TestRunResult(outcome = TestOutcome.FAIL)
        )
        assertEquals(0.0, calculator.calculateScore(results))
    }

    @Test
    fun `score is 100 for alternating pass and fail`() {
        val results = listOf(
            TestRunResult(outcome = TestOutcome.PASS),
            TestRunResult(outcome = TestOutcome.FAIL),
            TestRunResult(outcome = TestOutcome.PASS),
            TestRunResult(outcome = TestOutcome.FAIL),
            TestRunResult(outcome = TestOutcome.PASS)
        )
        assertEquals(100.0, calculator.calculateScore(results))
    }

    @Test
    fun `score is 50 for single change in middle`() {
        val results = listOf(
            TestRunResult(outcome = TestOutcome.PASS),
            TestRunResult(outcome = TestOutcome.PASS),
            TestRunResult(outcome = TestOutcome.FAIL)
        )
        assertEquals(50.0, calculator.calculateScore(results))
    }

    @Test
    fun `score is zero for single result`() {
        val results = listOf(TestRunResult(outcome = TestOutcome.PASS))
        assertEquals(0.0, calculator.calculateScore(results))
    }

    @Test
    fun `score is zero for empty results`() {
        assertEquals(0.0, calculator.calculateScore(emptyList()))
    }

    @Test
    fun `skipped results are excluded from calculation`() {
        val results = listOf(
            TestRunResult(outcome = TestOutcome.PASS),
            TestRunResult(outcome = TestOutcome.SKIP),
            TestRunResult(outcome = TestOutcome.PASS),
            TestRunResult(outcome = TestOutcome.SKIP),
            TestRunResult(outcome = TestOutcome.PASS)
        )
        assertEquals(0.0, calculator.calculateScore(results))
    }

    @Test
    fun `meetsMinimumRuns returns true when enough runs`() {
        val results = listOf(
            TestRunResult(outcome = TestOutcome.PASS),
            TestRunResult(outcome = TestOutcome.PASS),
            TestRunResult(outcome = TestOutcome.PASS)
        )
        assertTrue(calculator.meetsMinimumRuns(results, 3))
    }

    @Test
    fun `meetsMinimumRuns returns false when not enough runs`() {
        val results = listOf(
            TestRunResult(outcome = TestOutcome.PASS),
            TestRunResult(outcome = TestOutcome.SKIP)
        )
        assertFalse(calculator.meetsMinimumRuns(results, 3))
    }

    @Test
    fun `categorize returns correct category for scores`() {
        assertEquals(
            com.jakubjirak.flakytestdetector.model.FlakinessCategory.STABLE,
            calculator.categorize(0.0)
        )
        assertEquals(
            com.jakubjirak.flakytestdetector.model.FlakinessCategory.SUSPICIOUS,
            calculator.categorize(10.0)
        )
        assertEquals(
            com.jakubjirak.flakytestdetector.model.FlakinessCategory.FLAKY,
            calculator.categorize(30.0)
        )
        assertEquals(
            com.jakubjirak.flakytestdetector.model.FlakinessCategory.HIGHLY_UNSTABLE,
            calculator.categorize(75.0)
        )
    }

    @Test
    fun `trend is stable when not enough data`() {
        val results = listOf(
            TestRunResult(outcome = TestOutcome.PASS),
            TestRunResult(outcome = TestOutcome.FAIL)
        )
        assertEquals(
            FlakinessCalculator.TrendDirection.STABLE,
            calculator.calculateTrend(results)
        )
    }

    private fun runs(vararg outcomes: TestOutcome) = outcomes.map { TestRunResult(outcome = it) }

    @Test
    fun `trend detects worsening and improving windows`() {
        val p = TestOutcome.PASS
        val f = TestOutcome.FAIL
        assertEquals(
            FlakinessCalculator.TrendDirection.WORSENING,
            calculator.calculateTrend(runs(p, p, p, p, p, p, f, p, f, p))
        )
        assertEquals(
            FlakinessCalculator.TrendDirection.IMPROVING,
            calculator.calculateTrend(runs(p, f, p, f, p, p, p, p, p, p))
        )
        assertEquals(
            FlakinessCalculator.TrendDirection.STABLE,
            calculator.calculateTrend(runs(p, f, p, f, p, f, p, f, p, f))
        )
    }

    @Test
    fun `instance and static scores agree`() {
        val results = runs(TestOutcome.PASS, TestOutcome.FAIL, TestOutcome.FAIL)
        assertEquals(FlakinessCalculator.score(results), calculator.calculateScore(results))
        assertEquals(50.0, FlakinessCalculator.score(results))
    }
}
