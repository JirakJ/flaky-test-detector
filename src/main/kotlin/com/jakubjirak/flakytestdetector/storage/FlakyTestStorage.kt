package com.jakubjirak.flakytestdetector.storage

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.StoragePathMacros
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.jakubjirak.flakytestdetector.model.TestRecord
import com.jakubjirak.flakytestdetector.model.TestRunResult
import com.jakubjirak.flakytestdetector.settings.FlakyTestSettings
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.TimeUnit

/** Auto-quarantine rule: quarantine once a test has [minimumRuns] runs and a score of at least [threshold]. */
data class AutoQuarantinePolicy(val enabled: Boolean, val threshold: Double, val minimumRuns: Int) {
    fun shouldQuarantine(record: TestRecord): Boolean =
        enabled && record.results.size >= minimumRuns && record.flakinessScore >= threshold

    companion object {
        val DISABLED = AutoQuarantinePolicy(false, 0.0, 0)
    }
}

/**
 * Test history kept per project. Pure and thread-safe (test events arrive on the EDT,
 * retention cleanup runs in a background startup activity); listeners are notified
 * outside the lock.
 */
open class TestHistory {
    private val lock = Any()
    private val records = mutableListOf<TestRecord>()
    private val listeners = CopyOnWriteArrayList<() -> Unit>()

    fun addListener(listener: () -> Unit) {
        listeners += listener
    }

    fun removeListener(listener: () -> Unit) {
        listeners -= listener
    }

    protected fun notifyListeners() = listeners.forEach { it() }

    fun getRecords(): List<TestRecord> = synchronized(lock) { records.map { it.copy(results = it.results.toMutableList()) } }

    fun getRecordCount(): Int = synchronized(lock) { records.size }

    fun getRecord(testId: String): TestRecord? = getRecords().find { it.testId == testId }

    /** Records whose simple class name and method match what the editor sees. */
    fun findBySimpleName(simpleClassName: String, methodName: String): TestRecord? =
        getRecords().find { it.methodName == methodName && it.simpleClassName == simpleClassName }

    fun recordTestResult(
        testId: String,
        className: String,
        methodName: String,
        result: TestRunResult,
        policy: AutoQuarantinePolicy = AutoQuarantinePolicy.DISABLED,
    ) {
        synchronized(lock) {
            val record = records.find { it.testId == testId }
                ?: TestRecord(testId = testId, className = className, methodName = methodName)
                    .also { records += it }
            record.results += result
            if (!record.quarantined && policy.shouldQuarantine(record)) record.quarantined = true
        }
        notifyListeners()
    }

    fun toggleQuarantine(testId: String) {
        val changed = synchronized(lock) {
            records.find { it.testId == testId }?.let { it.quarantined = !it.quarantined } != null
        }
        if (changed) notifyListeners()
    }

    fun getFlakyTests(): List<TestRecord> = getRecords().filter { it.flakinessScore > 0.0 }

    fun clearAllHistory() {
        synchronized(lock) { records.clear() }
        notifyListeners()
    }

    /** Drops results older than [retentionDays] (relative to [now]) and records left without results. */
    fun performRetentionCleanup(retentionDays: Int, now: Long = System.currentTimeMillis()) {
        val cutoff = now - TimeUnit.DAYS.toMillis(retentionDays.toLong())
        synchronized(lock) {
            records.forEach { record -> record.results.removeAll { it.timestamp < cutoff } }
            records.removeAll { it.results.isEmpty() }
        }
        notifyListeners()
    }

    protected fun replaceAll(loaded: List<TestRecord>) {
        synchronized(lock) {
            records.clear()
            records += loaded
        }
    }
}

@State(
    name = "FlakyTestStorage",
    storages = [Storage(StoragePathMacros.WORKSPACE_FILE)]
)
@Service(Service.Level.PROJECT)
class FlakyTestStorage : TestHistory(), PersistentStateComponent<FlakyTestState> {

    private var state = FlakyTestState()
    private val gson: Gson = GsonBuilder().create()

    override fun getState(): FlakyTestState {
        state.testRecordsJson = gson.toJson(getRecords())
        return state
    }

    override fun loadState(state: FlakyTestState) {
        this.state = state
        try {
            val type = object : TypeToken<List<TestRecord>>() {}.type
            replaceAll(gson.fromJson<List<TestRecord>>(state.testRecordsJson, type).orEmpty())
        } catch (e: JsonParseException) {
            LOG.warn("Failed to load flaky test records: ${e.message}", e)
        }
    }

    fun performRetentionCleanup() {
        performRetentionCleanup(FlakyTestSettings.getInstance().historyRetentionDays)
        state.lastCleanupTimestamp = System.currentTimeMillis()
    }

    companion object {
        private val LOG = Logger.getInstance(FlakyTestStorage::class.java)

        fun getInstance(project: Project): FlakyTestStorage =
            project.getService(FlakyTestStorage::class.java)
    }
}
