package com.jakubjirak.flakytestdetector.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.jakubjirak.flakytestdetector.model.FlakinessThresholds

@Suppress("MagicNumber")
@State(
    name = "FlakyTestSettings",
    storages = [Storage("flakyTestDetectorSettings.xml")]
)
class FlakyTestSettings : PersistentStateComponent<FlakyTestSettings.State> {

    data class State(
        var enabled: Boolean = true,
        var historyRetentionDays: Int = 30,
        var minimumRuns: Int = 3,
        var suspiciousThreshold: Double = 1.0,
        var flakyThreshold: Double = 15.0,
        var highlyUnstableThreshold: Double = 50.0,
        var gutterIconsEnabled: Boolean = true,
        var autoQuarantine: Boolean = false,
        var autoQuarantineThreshold: Double = 80.0
    )

    private var state = State()

    override fun getState(): State = state

    override fun loadState(state: State) {
        this.state = state
    }

    var enabled: Boolean
        get() = state.enabled
        set(value) { state.enabled = value }

    var historyRetentionDays: Int
        get() = state.historyRetentionDays
        set(value) { state.historyRetentionDays = value }

    var minimumRuns: Int
        get() = state.minimumRuns
        set(value) { state.minimumRuns = value }

    var suspiciousThreshold: Double
        get() = state.suspiciousThreshold
        set(value) { state.suspiciousThreshold = value }

    var flakyThreshold: Double
        get() = state.flakyThreshold
        set(value) { state.flakyThreshold = value }

    var highlyUnstableThreshold: Double
        get() = state.highlyUnstableThreshold
        set(value) { state.highlyUnstableThreshold = value }

    var gutterIconsEnabled: Boolean
        get() = state.gutterIconsEnabled
        set(value) { state.gutterIconsEnabled = value }

    var autoQuarantine: Boolean
        get() = state.autoQuarantine
        set(value) { state.autoQuarantine = value }

    var autoQuarantineThreshold: Double
        get() = state.autoQuarantineThreshold
        set(value) { state.autoQuarantineThreshold = value }

    fun thresholds(): FlakinessThresholds = FlakinessThresholds(suspiciousThreshold, flakyThreshold, highlyUnstableThreshold)

    companion object {
        fun getInstance(): FlakyTestSettings =
            ApplicationManager.getApplication().getService(FlakyTestSettings::class.java)
    }
}
