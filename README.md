# Flaky Test Detector

[![Build](https://github.com/JirakJ/flaky-test-detector/workflows/Build/badge.svg)](https://github.com/JirakJ/flaky-test-detector/actions)
[![Version](https://img.shields.io/badge/version-2026.2.1-blue.svg)](https://github.com/JirakJ/flaky-test-detector/releases)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**Detect, track, and visualize flaky tests directly in your JetBrains IDE.**

Flaky Test Detector automatically monitors your test runs, calculates flakiness scores, and shows you exactly which tests are unreliable — with gutter icons, a dedicated dashboard, and actionable insights.

---

## ✨ Features

### Free Tier
- 🔍 **Automatic Test History Tracking** — records every test run result (pass/fail/error/skip)
- 📊 **Flakiness Score Calculation** — percentage-based score showing test reliability
- 🎯 **Gutter Icons** — colored icons next to `@Test` annotations showing flakiness level
- 📋 **Flaky Tests Dashboard** — tool window with table, sparkline visualization, and trend indicators
- 🔄 **Rerun Flaky Tests** — one-click rerun of all known flaky tests
- 🚫 **Quarantine Mode** — mark known-flaky tests to distinguish from real failures
- ⚙️ **Configurable Thresholds** — customize what counts as "suspicious", "flaky", or "highly unstable"
- 📈 Up to **500 tracked tests**, **30 days** history retention

### Pro Tier ($5/month)
- 📊 **Flakiness Trend Charts** — visualize how test stability changes over time
- 📦 **Module-level Aggregation** — see flakiness by module/package
- 🏥 **Suite Health Score** — overall percentage of stable tests
- 📤 **Export Reports** — JSON, CSV, HTML exports for CI/team sharing
- 🔗 **Git Correlation** — detect which commit made a test flaky
- 🤖 **Auto-Quarantine Rules** — automatically quarantine tests above threshold
- ♾️ **Unlimited** tracked tests and history retention

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

1. **Run your tests** as usual (JUnit, TestNG, Pytest, Jest — any framework)
2. **Open the Flaky Tests tool window** (View → Tool Windows → Flaky Tests)
3. **Run tests again** — after 3+ runs, flakiness scores appear
4. **Check gutter icons** — colored indicators appear next to `@Test` annotations

---

## ⚙️ Configuration

**Settings → Tools → Flaky Test Detector**

| Setting | Default | Description |
|---------|:-------:|-------------|
| Enable plugin | ✅ | Master switch |
| History retention | 90 days | How long to keep test results |
| Minimum runs for scoring | 3 | Tests need N runs before getting a score |
| Suspicious threshold | 15% | Score above this = ⚠️ suspicious |
| Flaky threshold | 30% | Score above this = 🔶 flaky |
| Highly unstable threshold | 50% | Score above this = 🔴 highly unstable |
| Gutter icons | ✅ | Show flakiness icons in editor |
| Auto-quarantine | ❌ | Auto-quarantine above threshold (Pro) |

---

## 📊 Flakiness Score

The score is calculated as:

```
score = (result_changes / (total_runs - 1)) × 100%
```

Where `result_changes` = number of times the outcome differs from the previous run.

| Score | Category | Icon |
|:-----:|----------|:----:|
| 0% | Stable | ✅ |
| 1-15% | Suspicious | ⚠️ |
| 16-50% | Flaky | 🔶 |
| 51-100% | Highly Unstable | 🔴 |

---

## ⌨️ Keyboard Shortcuts

| Action | Shortcut |
|--------|----------|
| Rerun Flaky Tests | `Alt+Shift+F` |

---

## 🛠️ Building from Source

```bash
git clone https://github.com/JirakJ/flaky-test-detector.git
cd flaky-test-detector
./gradlew buildPlugin
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

## 📄 License

MIT License — see [LICENSE](LICENSE) for details.

## 👤 Author

**Jakub Jirák** — [GitHub](https://github.com/JirakJ)
