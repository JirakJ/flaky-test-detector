package com.jakubjirak.flakytestdetector.model

import java.awt.Color

/**
 * Category boundaries for a flakiness score (0–100 % of consecutive runs whose outcome flipped).
 * A score above [highlyUnstable] is HIGHLY_UNSTABLE, above [flaky] FLAKY, at least [suspicious]
 * (and above 0) SUSPICIOUS, anything else STABLE.
 */
data class FlakinessThresholds(
    val suspicious: Double = 1.0,
    val flaky: Double = 15.0,
    val highlyUnstable: Double = 50.0,
) {
    companion object {
        val DEFAULT = FlakinessThresholds()
    }
}

@Suppress("MagicNumber")
enum class FlakinessCategory(val displayName: String, val color: Color) {
    STABLE("Stable", Color(76, 175, 80)),
    SUSPICIOUS("Suspicious", Color(255, 193, 7)),
    FLAKY("Flaky", Color(255, 152, 0)),
    HIGHLY_UNSTABLE("Highly Unstable", Color(244, 67, 54));

    companion object {
        fun fromScore(score: Double, thresholds: FlakinessThresholds = FlakinessThresholds.DEFAULT): FlakinessCategory =
            when {
                score > thresholds.highlyUnstable -> HIGHLY_UNSTABLE
                score > thresholds.flaky -> FLAKY
                score > 0.0 && score >= thresholds.suspicious -> SUSPICIOUS
                else -> STABLE
            }
    }
}
