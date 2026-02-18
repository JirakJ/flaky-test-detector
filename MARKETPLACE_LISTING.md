# Flaky Test Detector — JetBrains Marketplace Listing

---

## 📝 Plugin Information

| Field            | Value                                |
|------------------|--------------------------------------|
| **Name**         | Flaky Test Detector                  |
| **Plugin ID**    | com.jakubjirak.flakytestdetector     |
| **Version**      | 2026.2.10                            |
| **Category**     | Testing                              |
| **Tags**         | Flaky Tests, Test Reliability, Test Quality, CI/CD, Test Dashboard |

---

## 🎯 Short Description

> Detect, track, and visualize flaky tests in your IDE. Monitors test runs, calculates flakiness scores, shows gutter icons, and provides a dashboard with quarantine mode and rerun support.

_(185 characters)_

---

## 📖 Full Description

### 🎯 Stop Chasing Phantom Failures — Know Exactly Which Tests Are Flaky

**The Problem:** Flaky tests are the silent productivity killer. They erode trust in your test suite, waste CI minutes, and train developers to ignore failures. But finding which tests are flaky requires running them many times and tracking results — something nobody does manually.

**The Solution:** Flaky Test Detector automatically records every test run, calculates a **Flakiness Score (0–100%)** for each test, and surfaces unreliable tests with gutter icons, a dedicated dashboard, and quarantine mode. You see exactly which tests are flaky, how flaky they are, and whether they're getting better or worse.

---

### ✨ Key Features

#### 🆓 Free — Track and Visualize Flaky Tests

- 🔍 **Automatic Test History Tracking** — Records every test run result (pass/fail/error/skip) automatically
- 📊 **Flakiness Score** — Percentage-based reliability score for every test
- 🎯 **Gutter Icons** — Color-coded icons next to `@Test` annotations:
  - ✅ **Stable** (0%)
  - ⚠️ **Suspicious** (1–15%)
  - 🔶 **Flaky** (16–50%)
  - 🔴 **Highly Unstable** (51–100%)
- 📋 **Flaky Tests Dashboard** — Dedicated tool window with table, sparkline visualization, and trend indicators
- 🔄 **Rerun Flaky Tests** — One-click rerun of all known flaky tests (`Alt+Shift+F`)
- 🚫 **Quarantine Mode** — Mark known-flaky tests to distinguish from real failures
- ⚙️ **Configurable Thresholds** — Customize what counts as suspicious, flaky, or highly unstable
- 📈 **Up to 500 tracked tests** with 30 days history retention

#### 💎 Pro — $5/month

- 📊 **Flakiness Trend Charts** — Visualize how test stability changes over time
- 📦 **Module-Level Aggregation** — See flakiness rolled up by module and package
- 🏥 **Suite Health Score** — Overall percentage of stable tests in your project
- 📤 **Export Reports** — JSON, CSV, and HTML exports for CI and team sharing
- 🔗 **Git Correlation** — Detect which commit introduced flakiness
- 🤖 **Auto-Quarantine Rules** — Automatically quarantine tests above a configured threshold
- ♾️ **Unlimited Tracked Tests** — No 500-test cap
- 📚 **Unlimited History Retention** — No 30-day limit

---

### 💰 Pricing Model

| Tier     | Price      | Highlights                                              |
|----------|------------|---------------------------------------------------------|
| **Free** | $0         | Flakiness scores, gutter icons, dashboard, rerun, quarantine, 500 tests / 30 days |
| **Pro**  | $5/month   | Trend charts, module aggregation, suite health, export, git correlation, auto-quarantine, unlimited |

---

### 🎯 Perfect For

- **QA engineers** tracking test reliability and fighting flakiness systematically
- **CI/CD teams** wasting build minutes on unreliable tests
- **Developers** who've learned to ignore test failures because "that one's always flaky"
- **Engineering managers** wanting visibility into test suite health metrics

---

### ⚙️ Requirements

| Requirement        | Version                          |
|---------------------|----------------------------------|
| **JetBrains IDE**  | 2024.3+ (build 232+)            |
| **Java**           | 17+                             |

---

### 🚀 Quick Start

1. **Install** — Search "Flaky Test Detector" in `Settings → Plugins → Marketplace`
2. **Run your tests** — Execute tests as usual; the plugin automatically records results
3. **Check gutter icons** — After a few runs, flakiness icons appear next to `@Test` annotations
4. **Open dashboard** — View the Flaky Tests tool window (`View → Tool Windows → Flaky Tests`) for the full picture
5. **Rerun flaky tests** — Press `Alt+Shift+F` to rerun all tests with non-zero flakiness scores
6. **Configure** — Adjust thresholds and settings in `Settings → Tools → Flaky Test Detector`

---

## 📸 Screenshots & Media

| #  | Screenshot Description                                                                 |
|----|----------------------------------------------------------------------------------------|
| 1  | **Gutter icons next to tests** — Shows ✅ stable, ⚠️ suspicious, 🔶 flaky, and 🔴 highly unstable icons beside `@Test` annotations |
| 2  | **Flaky Tests dashboard** — Tool window with test table, sparkline history, flakiness scores, and trend indicators |
| 3  | **Quarantine mode** — Test marked as quarantined with distinct visual treatment in the dashboard |
| 4  | **Rerun flaky tests action** — `Alt+Shift+F` triggering a rerun of all known flaky tests |
| 5  | **Flakiness trend chart** — Line chart showing flakiness score trajectory over time for selected tests (Pro) |
| 6  | **Module-level aggregation** — Dashboard grouped by module/package showing aggregated flakiness percentages (Pro) |
| 7  | **Git correlation view** — Shows which commit introduced or resolved flakiness for a test (Pro) |
| 8  | **Settings panel** — Configuration page with threshold sliders, history retention, and auto-quarantine toggle |

---

## 🏷️ Marketplace Categories

| Type          | Category                |
|---------------|-------------------------|
| **Primary**   | Testing                 |
| **Secondary** | Code Quality / CI/CD    |

---

## 🔍 Keywords (SEO)

`flaky tests`, `test reliability`, `test flakiness`, `flaky test detection`, `test dashboard`, `quarantine tests`, `test history`, `test stability`, `test quality`, `CI failures`, `test rerun`, `unreliable tests`, `test monitoring`, `test suite health`, `junit`, `testing`

---

## 📋 Compatibility

### Compatible JetBrains IDEs

| IDE                        | Compatible |
|----------------------------|:----------:|
| IntelliJ IDEA Community    | ✅          |
| IntelliJ IDEA Ultimate     | ✅          |
| PyCharm                    | ✅          |
| WebStorm                   | ✅          |
| GoLand                     | ✅          |
| PhpStorm                   | ✅          |
| Rider                      | ✅          |
| CLion                      | ✅          |
| RubyMine                   | ✅          |
| Android Studio             | ✅          |

**Minimum IDE Version:** 2024.3 (build 232)

---

## 📞 Vendor Information

| Field        | Value                              |
|--------------|------------------------------------|
| **Vendor**   | Jakub Jirak                        |
| **Email**    | jirak@jakubjirak.com               |
| **Website**  | https://jakubjirak.com             |
| **Support**  | support@jakubjirak.com             |

---

## ✅ Pre-Launch Checklist

- [ ] Plugin icon meets JetBrains Marketplace guidelines (SVG, 40x40 & 80x80)
- [ ] Short description is under 255 characters
- [ ] Full description renders correctly in Marketplace preview
- [ ] Screenshots are high-resolution (1280×800 or higher), PNG format
- [ ] Plugin ID matches `gradle.properties` and `plugin.xml`
- [ ] `plugin.xml` vendor email and URL are correct
- [ ] Pricing tiers configured in JetBrains Marketplace seller dashboard
- [ ] Product code `PFLAKYTESTDTCTR` is registered
- [ ] License agreement / EULA uploaded
- [ ] Compatibility range tested on minimum supported IDE version (2024.3)
- [ ] Plugin verified with Plugin Verifier (`runPluginVerifier` Gradle task)
- [ ] CHANGELOG.md is up to date for the release version
- [ ] Support email is monitored and responsive
- [ ] Privacy policy URL is set
- [ ] Plugin signed with JetBrains Marketplace certificate
