package com.jakubjirak.flakytestdetector.core

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class FreemiumGateTest {

    @Test
    fun `free tier can track tests under limit`() {
        assertTrue(canTrackTestFree(0))
        assertTrue(canTrackTestFree(499))
    }

    @Test
    fun `free tier cannot track tests at limit`() {
        assertFalse(canTrackTestFree(500))
        assertFalse(canTrackTestFree(1000))
    }

    @Test
    fun `free tier history is 30 days`() {
        val days = FreemiumGate.MAX_FREE_HISTORY_DAYS
        assertTrue(days == 30)
    }

    @Test
    fun `max free tests is 500`() {
        assertTrue(FreemiumGate.MAX_FREE_TESTS == 500)
    }

    @Test
    fun `feature constants are defined`() {
        assertTrue(FreemiumGate.FEATURE_TREND_CHART.isNotEmpty())
        assertTrue(FreemiumGate.FEATURE_MODULE_AGGREGATION.isNotEmpty())
        assertTrue(FreemiumGate.FEATURE_EXPORT.isNotEmpty())
        assertTrue(FreemiumGate.FEATURE_GIT_CORRELATION.isNotEmpty())
        assertTrue(FreemiumGate.FEATURE_AUTO_QUARANTINE.isNotEmpty())
        assertTrue(FreemiumGate.FEATURE_EXTENDED_SPARKLINE.isNotEmpty())
    }

    private fun canTrackTestFree(currentCount: Int): Boolean {
        return currentCount < FreemiumGate.MAX_FREE_TESTS
    }
}
