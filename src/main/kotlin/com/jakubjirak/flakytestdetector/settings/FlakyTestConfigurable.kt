package com.jakubjirak.flakytestdetector.settings

import com.intellij.openapi.options.BoundConfigurable
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.bindIntValue
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel

@Suppress("MagicNumber")
class FlakyTestConfigurable : BoundConfigurable("Flaky Test Detector") {

    private val settings get() = FlakyTestSettings.getInstance()

    override fun createPanel() = panel {
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
        group("Flakiness Thresholds") {
            row("Suspicious (%):") {
                textField()
                    .bindText(
                        { settings.suspiciousThreshold.toString() },
                        { settings.suspiciousThreshold = it.toDoubleOrNull() ?: 1.0 }
                    )
                    .comment("Failure rate above this percentage marks test as suspicious")
            }
            row("Flaky (%):") {
                textField()
                    .bindText(
                        { settings.flakyThreshold.toString() },
                        { settings.flakyThreshold = it.toDoubleOrNull() ?: 15.0 }
                    )
                    .comment("Failure rate above this percentage marks test as flaky")
            }
            row("Highly unstable (%):") {
                textField()
                    .bindText(
                        { settings.highlyUnstableThreshold.toString() },
                        { settings.highlyUnstableThreshold = it.toDoubleOrNull() ?: 50.0 }
                    )
                    .comment("Failure rate above this percentage marks test as highly unstable")
            }
        }
        group("Auto-Quarantine") {
            row {
                checkBox("Enable auto-quarantine")
                    .bindSelected(settings::autoQuarantine)
                    .comment("Automatically quarantine tests that exceed the failure threshold")
            }
            row("Quarantine threshold (%):") {
                textField()
                    .bindText(
                        { settings.autoQuarantineThreshold.toString() },
                        { settings.autoQuarantineThreshold = it.toDoubleOrNull() ?: 80.0 }
                    )
                    .comment("Tests with failure rate above this threshold will be automatically quarantined")
            }
        }
    }
}
