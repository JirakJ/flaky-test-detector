package com.jakubjirak.flakytestdetector.storage

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.StoragePathMacros
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.jakubjirak.flakytestdetector.model.TestOutcome
import com.jakubjirak.flakytestdetector.model.TestRecord
import com.jakubjirak.flakytestdetector.model.TestRunResult
import com.jakubjirak.flakytestdetector.settings.FlakyTestSettings
import java.util.concurrent.TimeUnit

@State(
    name = "FlakyTestStorage",
    storages = [Storage(StoragePathMacros.WORKSPACE_FILE)]
)
@Service(Service.Level.PROJECT)
class FlakyTestStorage(private val project: Project) : PersistentStateComponent<FlakyTestState> {

    private var state = FlakyTestState()
    private val records = mutableListOf<TestRecord>()
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()
    private val listeners = mutableListOf<() -> Unit>()

    override fun getState(): FlakyTestState {
        state.testRecordsJson = gson.toJson(records)
        return state
    }

    override fun loadState(state: FlakyTestState) {
        this.state = state
        try {
            val type = object : TypeToken<List<TestRecord>>() {}.type
            val loaded: List<TestRecord> = gson.fromJson(state.testRecordsJson, type) ?: emptyList()
            records.clear()
            records.addAll(loaded)
        } catch (e: Exception) {
            LOG.warn("Failed to load flaky test records: ${e.message}", e)
        }
    }

    fun addListener(listener: () -> Unit) {
        listeners.add(listener)
    }

    private fun notifyListeners() {
        listeners.forEach { it() }
    }

    fun getRecords(): List<TestRecord> = records.toList()

    fun getRecordCount(): Int = records.size

    fun getRecord(testId: String): TestRecord? = records.find { it.testId == testId }

    fun recordTestResult(
        testId: String,
        className: String,
        methodName: String,
        result: TestRunResult,
        module: String? = null
    ) {
        val existing = records.find { it.testId == testId }
        if (existing != null) {
            existing.results.add(result)
        } else {
            val record = TestRecord(
                testId = testId,
                className = className,
                methodName = methodName,
                module = module,
                results = mutableListOf(result)
            )
            records.add(record)
        }
        notifyListeners()
    }

    fun toggleQuarantine(testId: String) {
        val record = records.find { it.testId == testId } ?: return
        record.quarantined = !record.quarantined
        notifyListeners()
    }

    fun getFlakyTests(): List<TestRecord> {
        return records.filter { it.flakinessScore > 0.0 }
    }

    fun clearAllHistory() {
        records.clear()
        notifyListeners()
    }

    @Suppress("MagicNumber")
    fun performRetentionCleanup() {
        val settings = FlakyTestSettings.getInstance()
        val retentionMs = TimeUnit.DAYS.toMillis(settings.historyRetentionDays.toLong())
        val cutoff = System.currentTimeMillis() - retentionMs

        records.forEach { record ->
            record.results.removeAll { it.timestamp < cutoff }
        }
        records.removeAll { it.results.isEmpty() }
        state.lastCleanupTimestamp = System.currentTimeMillis()
        notifyListeners()
    }

    fun getFlakyTestsAboveThreshold(threshold: Double): List<TestRecord> {
        return records.filter { it.flakinessScore > threshold }
    }

    fun getStableTests(): List<TestRecord> {
        return records.filter { it.flakinessScore == 0.0 && it.results.size >= 2 }
    }

    fun getTestsByOutcome(outcome: TestOutcome): List<TestRecord> {
        return records.filter { record ->
            record.results.lastOrNull()?.outcome == outcome
        }
    }

    companion object {
        private val LOG = Logger.getInstance(FlakyTestStorage::class.java)

        fun getInstance(project: Project): FlakyTestStorage =
            project.getService(FlakyTestStorage::class.java)
    }
}
