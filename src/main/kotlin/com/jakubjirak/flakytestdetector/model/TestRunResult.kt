package com.jakubjirak.flakytestdetector.model

data class TestRunResult(
    val timestamp: Long = System.currentTimeMillis(),
    val outcome: TestOutcome = TestOutcome.PASS,
    val durationMs: Long = 0L,
    val stackTrace: String? = null,
    val commitHash: String? = null
)

enum class TestOutcome {
    PASS, FAIL, ERROR, SKIP
}
