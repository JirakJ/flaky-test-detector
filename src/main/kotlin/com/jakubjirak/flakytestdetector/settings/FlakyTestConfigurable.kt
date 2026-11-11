package com.jakubjirak.flakytestdetector.settings

import com.intellij.openapi.options.BoundConfigurable
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.bindIntValue
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.dsl.builder.panel

@Suppress("MagicNumber")
class FlakyTestConfigurable : BoundConfigurable("Flaky Test Detector") {

    private val settings get() = FlakyTestSettings.getInstance()

    override fun createPanel() = panel {
        generalGroup()
        thresholdsGroup()
        autoQuarantineGroup()
    }

    private fun Panel.generalGroup() {
        group("General") {
            row {
                checkBox("Enable Flaky Test Detector")
                    .bindSelected(settings::enabled)
                    .comment("Enable or disable the flaky test detection analysis")
            }
            row {
                checkBox("Show gutter icons next to @Test")
                    .bindSelected(settings::gutterIconsEnabled)
                    .comment("Display flakiness status icons in the editor gutter")
            }
            row("History retention (days):") {
                spinner(1..365)
                    .bindIntValue(settings::historyRetentionDays)
                    .comment("Number of days to keep test execution history")
            }
            row("Minimum runs for scoring:") {
                spinner(1..100)
                    .bindIntValue(settings::minimumRuns)
                    .comment("Minimum number of test runs required before calculating a flakiness score")
            }
        }
    }

    private fun Panel.thresholdsGroup() {
        group("Flakiness Thresholds") {
            row {
                comment("Flakiness score = share of consecutive runs whose outcome changed (pass ↔ fail), in %.")
            }
            row("Suspicious (%):") {
                textField()
                    .bindText(
                        { settings.suspiciousThreshold.toString() },
                        { settings.suspiciousThreshold = it.toDoubleOrNull() ?: 1.0 }
                    )
                    .comment("Scores at or above this mark a test as suspicious")
            }
            row("Flaky (%):") {
                textField()
                    .bindText(
                        { settings.flakyThreshold.toString() },
                        { settings.flakyThreshold = it.toDoubleOrNull() ?: 15.0 }
                    )
                    .comment("Scores above this mark a test as flaky")
            }
            row("Highly unstable (%):") {
                textField()
                    .bindText(
                        { settings.highlyUnstableThreshold.toString() },
                        { settings.highlyUnstableThreshold = it.toDoubleOrNull() ?: 50.0 }
                    )
                    .comment("Scores above this mark a test as highly unstable")
            }
        }
    }

    private fun Panel.autoQuarantineGroup() {
        group("Auto-Quarantine") {
            row {
                checkBox("Enable auto-quarantine")
                    .bindSelected(settings::autoQuarantine)
                    .comment("Quarantine tests automatically once they have the minimum number of runs and reach the threshold score")
            }
            row("Quarantine threshold (%):") {
                textField()
                    .bindText(
                        { settings.autoQuarantineThreshold.toString() },
                        { settings.autoQuarantineThreshold = it.toDoubleOrNull() ?: 80.0 }
                    )
                    .comment("Flakiness score (share of runs whose outcome flipped) that triggers auto-quarantine")
            }
        }
    }
}
