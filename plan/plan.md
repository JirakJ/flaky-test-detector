# Flaky Test Detector — Implementační plán

## Přístup
Plugin pro detekci flaky testů v JetBrains IDE. Sleduje historii testových běhů, počítá flakiness skóre, zobrazuje výsledky v gutteru a tool window. Freemium model.

## Technický stack
- Kotlin 2.1.0, JVM 17
- IntelliJ Platform Gradle Plugin 2.3.0
- Platform version: 2024.3, sinceBuild: 232
- Balíček: com.jakubjirak.flakytestdetector
- Verze: 2026.2.1

## Workplan

### Fáze 1: Scaffolding
- [ ] Gradle projekt (build.gradle.kts, gradle.properties, libs.versions.toml)
- [ ] plugin.xml se všemi EP
- [ ] Ikony (pluginIcon.svg, pluginIcon_dark.svg)
- [ ] .editorconfig, .gitignore, detekt.yml, LICENSE
- [ ] GitHub Actions workflow

### Fáze 2: Core — Data Model & Persistence
- [ ] TestRecord, TestRunResult data classes
- [ ] FlakyTestStorage — PersistentStateComponent, JSON persistence
- [ ] FlakinessCalculator — score calculation algorithm
- [ ] FreemiumGate — license check utility

### Fáze 3: Test Listener
- [ ] FlakyTestExecutionListener — SMTRunnerEventsListener
- [ ] Zachycení test výsledků (pass/fail/error/skip)
- [ ] Automatické ukládání po každém test runu

### Fáze 4: UI — Gutter Icons
- [ ] FlakyTestLineMarkerProvider — gutter ikony u @Test
- [ ] Tooltip s flakiness info
- [ ] Click → navigace do tool window

### Fáze 5: UI — Tool Window
- [ ] FlakyTestToolWindowFactory
- [ ] Tabulka: test, score, sparkline, trend, actions
- [ ] Filtry a sorting
- [ ] Toolbar: rerun, refresh, settings

### Fáze 6: Settings
- [ ] FlakyTestSettings — PersistentStateComponent
- [ ] FlakyTestConfigurable — Settings UI
- [ ] Thresholds, retention, enable/disable

### Fáze 7: Actions
- [ ] RerunFlakyTestsAction
- [ ] QuarantineTestAction
- [ ] ViewFlakinessHistoryAction
- [ ] ClearHistoryAction

### Fáze 8: Test Runner Integration
- [ ] Rozšíření test tree o flakiness info
- [ ] Notification po selhání known-flaky testu

### Fáze 9: Quality
- [ ] Unit testy pro calculator, storage, listener
- [ ] Detekt checks
- [ ] Build verification

### Fáze 10: Documentation & Release
- [ ] README.md
- [ ] CHANGELOG.md
- [ ] MARKETPLACE.md
- [ ] Git init, push to GitHub
- [ ] GitHub release
