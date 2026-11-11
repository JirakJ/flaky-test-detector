# Flaky Test Detector

[![Version](https://img.shields.io/badge/version-2026.9.0-blue)](https://plugins.jetbrains.com/plugin/30205)
[![License](https://img.shields.io/badge/license-Apache--2.0-blue)](LICENSE)
[![Build](https://github.com/JirakJ/flaky-test-detector/actions/workflows/verify.yml/badge.svg)](https://github.com/JirakJ/flaky-test-detector/actions/workflows/verify.yml)

**Detect, track, and visualize flaky tests directly in your JetBrains IDE.**

Flaky Test Detector automatically monitors your test runs, calculates flakiness scores, and shows you exactly which tests are unreliable — with gutter icons, a dedicated dashboard, and actionable insights.

---

## ✨ Features

- 🔍 **Automatic Test History Tracking** — records every test run result (pass/fail/error/skip)
- 📊 **Flakiness Score Calculation** — percentage-based score showing test reliability
- 🎯 **Gutter Icons** — colored icons on Java and Kotlin `@Test` / `@ParameterizedTest` / `@RepeatedTest` methods that are not stable
- 📋 **Flaky Tests Dashboard** — tool window with a sortable, filterable table, last-10-runs sparkline and trend (improving / stable / worsening)
- 🚫 **Quarantine** — mark known-flaky tests in the dashboard (a label for triage; it does not change how tests run)
- 🤖 **Auto-Quarantine** — optionally quarantine tests automatically once they reach a score threshold
- ⚙️ **Configurable Thresholds** — customize what counts as "suspicious", "flaky", or "highly unstable"
- 🧹 **History Retention** — results older than the configured number of days are dropped on project open

---

## 📦 Installation

### From Disk
1. Download the latest `.zip` from [Releases](https://github.com/JirakJ/flaky-test-detector/releases)
2. **Settings → Plugins → ⚙️ → Install Plugin from Disk...**
3. Select the downloaded ZIP file
4. Restart IDE

### From Marketplace
1. **Settings → Plugins → Marketplace**
2. Search for **"Flaky Test Detector"**
3. Click **Install**
4. Restart IDE

---

## 🚀 Quick Start

1. **Run your tests** from the IDE as usual (any framework that reports to the IDE test runner)
2. **Open the Flaky Tests tool window** (View → Tool Windows → Flaky Tests)
3. **Run tests again** — after 3+ runs, flakiness scores appear
4. **Check gutter icons** — colored indicators appear on unstable Java/Kotlin test methods

---

## ⚙️ Configuration

**Settings → Tools → Flaky Test Detector**

| Setting | Default | Description |
|---------|:-------:|-------------|
| Enable plugin | ✅ | Master switch |
| History retention | 30 days | How long to keep test results |
| Minimum runs for scoring | 3 | Gutter icons and auto-quarantine need N runs |
| Suspicious threshold | 1% | Score at or above this = ⚠️ suspicious |
| Flaky threshold | 15% | Score above this = 🔶 flaky |
| Highly unstable threshold | 50% | Score above this = 🔴 highly unstable |
| Gutter icons | ✅ | Show flakiness icons in editor |
| Auto-quarantine | ❌ | Quarantine tests whose score reaches the auto-quarantine threshold (default 80%) |

---

## 📊 Flakiness Score

The score is calculated as:

```
score = (result_changes / (total_runs - 1)) × 100%
```

Where `result_changes` = number of times the outcome differs from the previous run.

With the default thresholds:

| Score | Category | Icon |
|:-----:|----------|:----:|
| 0% | Stable | ✅ |
| 1-15% | Suspicious | ⚠️ |
| >15-50% | Flaky | 🔶 |
| >50-100% | Highly Unstable | 🔴 |

---

## 🧭 Actions

*Tools → View Flaky Test History* opens the dashboard; *Tools → Clear Flaky Test History* deletes all recorded results for the project.

---

## 🛠️ Building from Source

```bash
git clone https://github.com/JirakJ/flaky-test-detector.git
cd flaky-test-detector
./gradlew buildPlugin
./gradlew detekt test   # static analysis + unit tests
```

The plugin ZIP will be in `build/distributions/`.

### Run Tests
```bash
./gradlew test
```

### Run Detekt
```bash
./gradlew detekt
```

---

## 🤝 Related Plugins

Flaky Test Detector is part of a comprehensive developer productivity toolkit:

- [Dead Code & Drift Radar](https://github.com/JirakJ/dead-code-drift-radar) — detect unused code and drift
- [Slow Build Hotspots](https://github.com/JirakJ/slow-build-hotspots) — find what slows your builds
- [Technical Debt Accountant](https://github.com/JirakJ/technical-debt-accountant) — quantify tech debt in dollars

---

## 👤 Author

**Jakub Jirák** — [GitHub](https://github.com/JirakJ)

## Free & open source

As of version 2026.8.0 this plugin is completely free — every feature is unlocked and no license is needed. The source code is available at https://github.com/JirakJ/flaky-test-detector under the Apache License 2.0.

## Contributing

Issues and pull requests are welcome — please open them on GitHub.

## License

Copyright 2026 Jakub Jirák. Licensed under the [Apache License, Version 2.0](LICENSE).
