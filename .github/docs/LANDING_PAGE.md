# Flaky Test Detector

**Stop guessing which tests you can trust.**

---

## The Problem

Flaky tests erode confidence in your test suite. When tests pass and fail randomly, developers stop trusting the results and start ignoring failures. This leads to real bugs slipping through. Without tracking test reliability over time, you can't distinguish between genuine failures and random flakiness.

---

## Key Features

### Automatic Test Tracking
Every test run is recorded automatically — pass, fail, error, or skip. No manual setup required. Just run your tests as usual.

### Flakiness Score
Each test gets a reliability score based on result consistency. The formula tracks how often results change between runs: `(result_changes / (total_runs - 1)) × 100%`.

### Gutter Icons
See test reliability right in your editor. Color-coded icons appear next to @Test annotations showing whether each test is stable, suspicious, flaky, or highly unstable.

### Flaky Tests Dashboard
A dedicated tool window with a sortable table of all tracked tests. Includes sparkline visualizations showing test stability patterns over time.

### One-Click Rerun
Found a suspicious test? Rerun it directly from the dashboard without navigating to the source file.

### Quarantine Mode
Mark known-flaky tests so they don't block development. Quarantined tests are tracked separately and can be reviewed when ready.

### Configurable Thresholds
Define what "suspicious," "flaky," and "highly unstable" mean for your project. Set custom percentage boundaries.

### Git Correlation (Pro)
Automatically detect which commit made a test flaky. Correlates test result changes with git history.

---

## How It Works

1. **Install the plugin** — Available for all JetBrains IDEs from the Marketplace.
2. **Run your tests** — The plugin automatically records results for every test execution.
3. **Review flakiness** — After 3+ runs, gutter icons appear and the dashboard populates with scores.
4. **Take action** — Quarantine flaky tests, rerun suspicious ones, or investigate root causes.

---

## Pricing

| Feature | Free | Pro ($5/month) |
|---------|------|-----------------|
| Automatic test tracking | ✅ | ✅ |
| Flakiness score | ✅ | ✅ |
| Gutter icons | ✅ | ✅ |
| Dashboard with sparklines | ✅ | ✅ |
| One-click rerun | ✅ | ✅ |
| Quarantine mode | ✅ | ✅ |
| Configurable thresholds | ✅ | ✅ |
| Up to 500 tests, 30-day history | ✅ | ✅ |
| Unlimited tests and history | ❌ | ✅ |
| Flakiness trend charts | ❌ | ✅ |
| Module/package aggregation | ❌ | ✅ |
| Suite health score | ❌ | ✅ |
| Git correlation | ❌ | ✅ |
| Auto-quarantine rules | ❌ | ✅ |
| Export reports (JSON/CSV/HTML) | ❌ | ✅ |
| CI/CD integration | ❌ | ✅ |

---

## Testimonials

> *"We identified 23 flaky tests in our first week. Fixing them cut our CI failure rate in half."*
> — **QA Lead** *(placeholder)*

> *"The quarantine feature lets us keep moving while we fix flaky tests on the side."*
> — **Senior Developer** *(placeholder)*

---

## FAQ

**Q: How many test runs are needed before scores appear?**
A: A minimum of 3 runs per test is required before the flakiness score is calculated.

**Q: Does it work with all test frameworks?**
A: It works with any test framework supported by JetBrains IDEs (JUnit, TestNG, pytest, Jest, etc.).

**Q: What's the difference between free and pro?**
A: Free tracks up to 500 tests with 30-day history. Pro offers unlimited tests, trend charts, git correlation, and CI integration.

**Q: Can I use it in CI/CD pipelines?**
A: Pro tier includes CI/CD integration for tracking flakiness across pipeline runs.

**Q: Does quarantine mode affect test execution?**
A: Quarantined tests are still executed but their results are tracked separately so they don't block your workflow.

---

## Get Started

Install Flaky Test Detector from the JetBrains Marketplace and take control of your test reliability.

[Install from JetBrains Marketplace](#) | [View Documentation](#) | [Report Issues](#)
