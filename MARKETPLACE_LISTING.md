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

> Detect, track, and visualize flaky tests in your IDE. Monitors test runs, calculates flakiness scores, shows gutter icons, and provides a dashboard with quarantine marking.

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
- 🎯 **Gutter Icons** — Color-coded icons on Java/Kotlin test methods (stable tests get none):
  - ✅ **Stable** (0%)
  - ⚠️ **Suspicious** (1–15%)
  - 🔶 **Flaky** (16–50%)
  - 🔴 **Highly Unstable** (51–100%)
- 📋 **Flaky Tests Dashboard** — Dedicated tool window with table, sparkline visualization, and trend indicators
- 🚫 **Quarantine Mode** — Mark known-flaky tests to distinguish from real failures
- ⚙️ **Configurable Thresholds** — Customize what counts as suspicious, flaky, or highly unstable

#### More

- 🤖 **Auto-Quarantine** — Automatically quarantine tests above a configured score
- 📚 **Configurable History Retention**

---

### 💚 Free & open source

All features are free for everyone — no license needed. Source code under Apache-2.0: https://github.com/JirakJ/flaky-test-detector

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
3. **Check gutter icons** — After a few runs, unstable Java/Kotlin test methods get a flakiness icon
4. **Open dashboard** — View the Flaky Tests tool window (`View → Tool Windows → Flaky Tests`) for the full picture
5. **Configure** — Adjust thresholds and settings in `Settings → Tools → Flaky Test Detector`

---

## 📸 Screenshots & Media

| #  | Screenshot Description                                                                 |
|----|----------------------------------------------------------------------------------------|
| 1  | **Gutter icons next to tests** — Shows ✅ stable, ⚠️ suspicious, 🔶 flaky, and 🔴 highly unstable icons beside `@Test` annotations |
| 2  | **Flaky Tests dashboard** — Tool window with test table, sparkline history, flakiness scores, and trend indicators |
| 3  | **Quarantine mode** — Test marked as quarantined with distinct visual treatment in the dashboard |
| 4  | **Settings panel** — Configuration page with threshold sliders, history retention, and auto-quarantine toggle |

---

## 🏷️ Marketplace Categories

| Type          | Category                |
|---------------|-------------------------|
| **Primary**   | Testing                 |
| **Secondary** | Code Quality / CI/CD    |

---

## 🔍 Keywords (SEO)

`flaky tests`, `test reliability`, `test flakiness`, `flaky test detection`, `test dashboard`, `quarantine tests`, `test history`, `test stability`, `test quality`, `CI failures`, `unreliable tests`, `test monitoring`, `test suite health`, `junit`, `testing`

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
- [ ] Compatibility range tested on minimum supported IDE version (2024.3)
- [ ] Plugin verified with Plugin Verifier (`runPluginVerifier` Gradle task)
- [ ] CHANGELOG.md is up to date for the release version
- [ ] Support email is monitored and responsive
- [ ] Privacy policy URL is set
- [ ] Plugin signed with JetBrains Marketplace certificate
