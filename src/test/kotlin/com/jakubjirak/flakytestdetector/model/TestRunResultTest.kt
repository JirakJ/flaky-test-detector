package com.jakubjirak.flakytestdetector.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class TestRunResultTest {

    @Test
    fun `default outcome is PASS`() {
        val result = TestRunResult()
        assertEquals(TestOutcome.PASS, result.outcome)
    }

    @Test
    fun `default duration is zero`() {
        val result = TestRunResult()
        assertEquals(0L, result.durationMs)
    }

    @Test
    fun `stack trace is null by default`() {
        val result = TestRunResult()
        assertNull(result.stackTrace)
    }

    @Test
    fun `commit hash is null by default`() {
        val result = TestRunResult()
        assertNull(result.commitHash)
    }

    @Test
    fun `result stores all fields correctly`() {
        val result = TestRunResult(
            timestamp = 12345L,
            outcome = TestOutcome.FAIL,
            durationMs = 500L,
            stackTrace = "java.lang.AssertionError",
            commitHash = "abc123"
        )
        assertEquals(12345L, result.timestamp)
        assertEquals(TestOutcome.FAIL, result.outcome)
        assertEquals(500L, result.durationMs)
        assertEquals("java.lang.AssertionError", result.stackTrace)
        assertEquals("abc123", result.commitHash)
    }
}
