package com.jakubjirak.flakytestdetector.core

import com.jakubjirak.flakytestdetector.model.FlakinessCategory
import com.jakubjirak.flakytestdetector.model.FlakinessThresholds
import com.jakubjirak.flakytestdetector.model.TestOutcome
import com.jakubjirak.flakytestdetector.model.TestRunResult

class FlakinessCalculator {

    fun calculateScore(results: List<TestRunResult>): Double = score(results)

    fun categorize(score: Double, thresholds: FlakinessThresholds = FlakinessThresholds.DEFAULT): FlakinessCategory =
        FlakinessCategory.fromScore(score, thresholds)

    fun meetsMinimumRuns(results: List<TestRunResult>, minimumRuns: Int): Boolean {
        return results.filter { it.outcome != TestOutcome.SKIP }.size >= minimumRuns
    }

    fun calculateTrend(results: List<TestRunResult>, windowSize: Int = TREND_WINDOW): TrendDirection {
        val meaningful = results.filter { it.outcome != TestOutcome.SKIP }
        if (meaningful.size < windowSize * 2) return TrendDirection.STABLE

        val recentHalf = meaningful.takeLast(windowSize)
        val olderHalf = meaningful.dropLast(windowSize).takeLast(windowSize)

        val recentScore = calculateScore(recentHalf)
        val olderScore = calculateScore(olderHalf)

        return when {
            recentScore > olderScore + TREND_THRESHOLD -> TrendDirection.WORSENING
            recentScore < olderScore - TREND_THRESHOLD -> TrendDirection.IMPROVING
            else -> TrendDirection.STABLE
        }
    }

    enum class TrendDirection(val symbol: String, val displayName: String) {
        IMPROVING("\u2198", "Improving"),
        STABLE("\u2192", "Stable"),
        WORSENING("\u2197", "Worsening")
    }

    companion object {
        private const val PERCENT = 100.0

        /** Share (0–100 %) of consecutive non-skipped runs whose outcome flipped. */
        fun score(results: List<TestRunResult>): Double {
            val meaningful = results.filter { it.outcome != TestOutcome.SKIP }
            if (meaningful.size < 2) return 0.0
            val changes = meaningful.zipWithNext().count { (a, b) -> a.outcome != b.outcome }
            return changes.toDouble() / (meaningful.size - 1) * PERCENT
        }

        @Suppress("MagicNumber")
        private const val TREND_WINDOW = 5

        @Suppress("MagicNumber")
        private const val TREND_THRESHOLD = 5.0
    }
}
