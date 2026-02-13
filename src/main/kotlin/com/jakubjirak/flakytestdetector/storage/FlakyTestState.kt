package com.jakubjirak.flakytestdetector.storage

data class FlakyTestState(
    var testRecordsJson: String = "[]",
    var lastCleanupTimestamp: Long = 0L
)
