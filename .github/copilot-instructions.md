# Flaky Test Detector — Copilot Instructions

## Project Overview
JetBrains IDE plugin for detecting, tracking, and visualizing flaky tests. Automatically records test run history, calculates flakiness scores, and provides actionable insights directly in the IDE.

## Tech Stack
- **Language:** Kotlin 2.1.0
- **Build:** Gradle 8.12, Kotlin DSL, IntelliJ Platform Gradle Plugin 2.3.0
- **Platform:** IntelliJ Platform 2024.3+ (sinceBuild=232)
- **JVM:** 17
- **JSON:** Gson 2.11.0
- **Tests:** JUnit 5
- **Linting:** Detekt

## Architecture
```
com.jakubjirak.flakytestdetector/
├── model/          # TestRecord, TestRunResult, TestOutcome, FlakinessCategory
├── core/           # FlakinessCalculator, FreemiumGate
├── storage/        # FlakyTestStorage (PersistentStateComponent), FlakyTestState
├── listener/       # FlakyTestExecutionListener (TestStatusListener)
├── ui/             # ToolWindowFactory, FlakyTestPanel, SparklineRenderer, GutterIconProvider
├── settings/       # FlakyTestSettings, FlakyTestConfigurable
├── actions/        # RerunFlakyTests, QuarantineTest, ViewHistory, ClearHistory
└── FlakyTestStartupActivity.kt
```

## Conventions
- Package: `com.jakubjirak.flakytestdetector`
- Logger: always in `companion object` (never as instance property)
- Freemium: all paid features behind `FreemiumGate.isPaidFeatureAvailable()`
- Storage: PersistentStateComponent in WORKSPACE_FILE
- Settings: application-level PersistentStateComponent
- Icons: pluginIcon.svg + pluginIcon_dark.svg in META-INF

## Build & Test
```bash
./gradlew build        # compile + detekt + test
./gradlew test         # tests only
./gradlew detekt       # static analysis
./gradlew verifyPlugin # plugin structure check
./gradlew buildPlugin  # create distributable ZIP
```

## Freemium Boundaries
- **Free:** max 500 tracked tests, 30-day history, basic sparkline, manual quarantine
- **Pro:** unlimited tests, unlimited history, trend charts, export, Git correlation, auto-quarantine
