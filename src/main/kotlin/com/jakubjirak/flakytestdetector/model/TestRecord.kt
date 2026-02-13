package com.jakubjirak.flakytestdetector.model

data class TestRecord(
    val testId: String,
    val className: String,
    val methodName: String,
    val module: String? = null,
    val results: MutableList<TestRunResult> = mutableListOf(),
    var quarantined: Boolean = false,
    val tags: MutableSet<String> = mutableSetOf()
) {
    val flakinessScore: Double
        get() {
            if (results.size < 2) return 0.0
            val meaningful = results.filter { it.outcome != TestOutcome.SKIP }
            if (meaningful.size < 2) return 0.0
            var changes = 0
            for (i in 1 until meaningful.size) {
                if (meaningful[i].outcome != meaningful[i - 1].outcome) {
                    changes++
                }
            }
            @Suppress("MagicNumber")
            return (changes.toDouble() / (meaningful.size - 1)) * 100.0
        }
}
