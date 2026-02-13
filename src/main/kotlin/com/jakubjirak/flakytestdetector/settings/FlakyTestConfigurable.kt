package com.jakubjirak.flakytestdetector.settings

import com.intellij.openapi.options.Configurable
import java.awt.FlowLayout
import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.JCheckBox
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JSpinner
import javax.swing.JTextField
import javax.swing.SpinnerNumberModel

class FlakyTestConfigurable : Configurable {

    private var panel: JPanel? = null
    private var enabledCheckbox: JCheckBox? = null
    private var retentionSpinner: JSpinner? = null
    private var minimumRunsSpinner: JSpinner? = null
    private var suspiciousField: JTextField? = null
    private var flakyField: JTextField? = null
    private var highlyUnstableField: JTextField? = null
    private var gutterIconsCheckbox: JCheckBox? = null
    private var autoQuarantineCheckbox: JCheckBox? = null
    private var autoQuarantineThresholdField: JTextField? = null

    override fun getDisplayName(): String = "Flaky Test Detector"

    @Suppress("MagicNumber")
    override fun createComponent(): JComponent {
        val settings = FlakyTestSettings.getInstance()

        panel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            border = BorderFactory.createEmptyBorder(10, 10, 10, 10)
        }

        val enabledPanel = JPanel(FlowLayout(FlowLayout.LEFT)).apply {
            enabledCheckbox = JCheckBox("Enable Flaky Test Detector", settings.enabled)
            add(enabledCheckbox)
        }

        val retentionPanel = JPanel(FlowLayout(FlowLayout.LEFT)).apply {
            add(JLabel("History Retention (days):"))
            retentionSpinner = JSpinner(SpinnerNumberModel(settings.historyRetentionDays, 7, 365, 1))
            add(retentionSpinner)
        }

        val minRunsPanel = JPanel(FlowLayout(FlowLayout.LEFT)).apply {
            add(JLabel("Minimum Runs for Scoring:"))
            minimumRunsSpinner = JSpinner(SpinnerNumberModel(settings.minimumRuns, 2, 20, 1))
            add(minimumRunsSpinner)
        }

        val thresholdPanel = JPanel(FlowLayout(FlowLayout.LEFT)).apply {
            add(JLabel("Suspicious (%):"))
            suspiciousField = JTextField(settings.suspiciousThreshold.toString(), 5)
            add(suspiciousField)
            add(JLabel("Flaky (%):"))
            flakyField = JTextField(settings.flakyThreshold.toString(), 5)
            add(flakyField)
            add(JLabel("Highly Unstable (%):"))
            highlyUnstableField = JTextField(settings.highlyUnstableThreshold.toString(), 5)
            add(highlyUnstableField)
        }

        val gutterPanel = JPanel(FlowLayout(FlowLayout.LEFT)).apply {
            gutterIconsCheckbox = JCheckBox("Show gutter icons next to @Test", settings.gutterIconsEnabled)
            add(gutterIconsCheckbox)
        }

        val quarantinePanel = JPanel(FlowLayout(FlowLayout.LEFT)).apply {
            autoQuarantineCheckbox = JCheckBox("Auto-quarantine enabled", settings.autoQuarantine)
            add(autoQuarantineCheckbox)
            add(JLabel("Threshold (%):"))
            autoQuarantineThresholdField = JTextField(settings.autoQuarantineThreshold.toString(), 5)
            add(autoQuarantineThresholdField)
        }

        panel?.add(enabledPanel)
        panel?.add(retentionPanel)
        panel?.add(minRunsPanel)
        panel?.add(thresholdPanel)
        panel?.add(gutterPanel)
        panel?.add(quarantinePanel)

        return panel!!
    }

    @Suppress("MagicNumber")
    override fun isModified(): Boolean {
        val settings = FlakyTestSettings.getInstance()
        return enabledCheckbox?.isSelected != settings.enabled ||
            (retentionSpinner?.value as? Int) != settings.historyRetentionDays ||
            (minimumRunsSpinner?.value as? Int) != settings.minimumRuns ||
            suspiciousField?.text?.toDoubleOrNull() != settings.suspiciousThreshold ||
            flakyField?.text?.toDoubleOrNull() != settings.flakyThreshold ||
            highlyUnstableField?.text?.toDoubleOrNull() != settings.highlyUnstableThreshold ||
            gutterIconsCheckbox?.isSelected != settings.gutterIconsEnabled ||
            autoQuarantineCheckbox?.isSelected != settings.autoQuarantine ||
            autoQuarantineThresholdField?.text?.toDoubleOrNull() != settings.autoQuarantineThreshold
    }

    @Suppress("MagicNumber")
    override fun apply() {
        val settings = FlakyTestSettings.getInstance()
        settings.enabled = enabledCheckbox?.isSelected ?: true
        settings.historyRetentionDays = (retentionSpinner?.value as? Int) ?: 30
        settings.minimumRuns = (minimumRunsSpinner?.value as? Int) ?: 3
        settings.suspiciousThreshold = suspiciousField?.text?.toDoubleOrNull() ?: 1.0
        settings.flakyThreshold = flakyField?.text?.toDoubleOrNull() ?: 15.0
        settings.highlyUnstableThreshold = highlyUnstableField?.text?.toDoubleOrNull() ?: 50.0
        settings.gutterIconsEnabled = gutterIconsCheckbox?.isSelected ?: true
        settings.autoQuarantine = autoQuarantineCheckbox?.isSelected ?: false
        settings.autoQuarantineThreshold = autoQuarantineThresholdField?.text?.toDoubleOrNull() ?: 80.0
    }

    @Suppress("MagicNumber")
    override fun reset() {
        val settings = FlakyTestSettings.getInstance()
        enabledCheckbox?.isSelected = settings.enabled
        retentionSpinner?.value = settings.historyRetentionDays
        minimumRunsSpinner?.value = settings.minimumRuns
        suspiciousField?.text = settings.suspiciousThreshold.toString()
        flakyField?.text = settings.flakyThreshold.toString()
        highlyUnstableField?.text = settings.highlyUnstableThreshold.toString()
        gutterIconsCheckbox?.isSelected = settings.gutterIconsEnabled
        autoQuarantineCheckbox?.isSelected = settings.autoQuarantine
        autoQuarantineThresholdField?.text = settings.autoQuarantineThreshold.toString()
    }
}
