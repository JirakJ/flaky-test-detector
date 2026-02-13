package com.jakubjirak.flakytestdetector.core

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.Service

@Service(Service.Level.APP)
class FreemiumGate {

    enum class LicenseStatus {
        FREE, TRIAL, PRO, EXPIRED
    }

    @Suppress("UNUSED_PARAMETER")
    fun isPaidFeatureAvailable(featureId: String): Boolean {
        val status = getLicenseStatus()
        return status == LicenseStatus.PRO || status == LicenseStatus.TRIAL
    }

    fun getLicenseStatus(): LicenseStatus {
        // TODO: Integrate with JetBrains Marketplace Licensing API
        return LicenseStatus.FREE
    }

    fun canTrackTest(currentCount: Int): Boolean {
        return when (getLicenseStatus()) {
            LicenseStatus.FREE, LicenseStatus.EXPIRED -> currentCount < MAX_FREE_TESTS
            LicenseStatus.TRIAL, LicenseStatus.PRO -> true
        }
    }

    fun getMaxTrackedTests(): Int {
        return when (getLicenseStatus()) {
            LicenseStatus.FREE, LicenseStatus.EXPIRED -> MAX_FREE_TESTS
            LicenseStatus.TRIAL, LicenseStatus.PRO -> Int.MAX_VALUE
        }
    }

    fun getHistoryRetentionDays(): Int {
        return when (getLicenseStatus()) {
            LicenseStatus.FREE, LicenseStatus.EXPIRED -> MAX_FREE_HISTORY_DAYS
            LicenseStatus.TRIAL, LicenseStatus.PRO -> Int.MAX_VALUE
        }
    }

    companion object {
        @Suppress("MagicNumber")
        const val MAX_FREE_TESTS = 500

        @Suppress("MagicNumber")
        const val MAX_FREE_HISTORY_DAYS = 30

        const val FEATURE_TREND_CHART = "trend_chart"
        const val FEATURE_MODULE_AGGREGATION = "module_aggregation"
        const val FEATURE_EXPORT = "export"
        const val FEATURE_GIT_CORRELATION = "git_correlation"
        const val FEATURE_AUTO_QUARANTINE = "auto_quarantine"
        const val FEATURE_EXTENDED_SPARKLINE = "extended_sparkline"

        fun getInstance(): FreemiumGate =
            ApplicationManager.getApplication().getService(FreemiumGate::class.java)
    }
}
