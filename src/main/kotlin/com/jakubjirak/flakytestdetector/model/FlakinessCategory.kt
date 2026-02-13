package com.jakubjirak.flakytestdetector.model

import java.awt.Color

@Suppress("MagicNumber")
enum class FlakinessCategory(
    val displayName: String,
    val lowerBound: Double,
    val upperBound: Double,
    val color: Color
) {
    STABLE("Stable", 0.0, 0.0, Color(76, 175, 80)),
    SUSPICIOUS("Suspicious", 0.01, 15.0, Color(255, 193, 7)),
    FLAKY("Flaky", 15.01, 50.0, Color(255, 152, 0)),
    HIGHLY_UNSTABLE("Highly Unstable", 50.01, 100.0, Color(244, 67, 54));

    companion object {
        fun fromScore(score: Double): FlakinessCategory = when {
            score <= STABLE.upperBound -> STABLE
            score <= SUSPICIOUS.upperBound -> SUSPICIOUS
            score <= FLAKY.upperBound -> FLAKY
            else -> HIGHLY_UNSTABLE
        }
    }
}
