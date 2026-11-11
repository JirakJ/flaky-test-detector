package com.jakubjirak.flakytestdetector.model

import com.jakubjirak.flakytestdetector.core.FlakinessCalculator

data class TestRecord(
    val testId: String,
    val className: String,
    val methodName: String,
    val module: String? = null,
    val results: MutableList<TestRunResult> = mutableListOf(),
    var quarantined: Boolean = false,
    val tags: MutableSet<String> = mutableSetOf()
) {
    /** Share (0–100 %) of consecutive non-skipped runs whose outcome differs from the previous one. */
    val flakinessScore: Double
        get() = FlakinessCalculator.score(results)

    /** Simple class name (without package) — what the editor knows about a test method's class. */
    val simpleClassName: String
        get() = className.substringAfterLast('.')
}
