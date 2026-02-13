package com.jakubjirak.flakytestdetector.core

import com.jakubjirak.flakytestdetector.model.FlakinessCategory
import com.jakubjirak.flakytestdetector.model.TestOutcome
import com.jakubjirak.flakytestdetector.model.TestRunResult

class FlakinessCalculator {

    @Suppress("MagicNumber")
    fun calculateScore(results: List<TestRunResult>): Double {
        val meaningful = results.filter { it.outcome != TestOutcome.SKIP }
        if (meaningful.size < 2) return 0.0

        var changes = 0
        for (i in 1 until meaningful.size) {
            if (meaningful[i].outcome != meaningful[i - 1].outcome) {
                changes++
            }
        }
        return (changes.toDouble() / (meaningful.size - 1)) * 100.0
    }

    fun categorize(score: Double): FlakinessCategory = FlakinessCategory.fromScore(score)

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
        @Suppress("MagicNumber")
        private const val TREND_WINDOW = 5

        @Suppress("MagicNumber")
        private const val TREND_THRESHOLD = 5.0
    }
}
