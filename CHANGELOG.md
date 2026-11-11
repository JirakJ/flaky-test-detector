# Changelog

## [Unreleased]

## [2026.9.0] - 2026-11-11

### Fixed
- **Gutter icons never appeared**: the provider was not registered, and it looked tests up by simple class name while history used fully-qualified names. It is now registered for Java and Kotlin (K2-compatible) and matches correctly.
- Results were stored in whichever project was opened first; they now go to the project that ran the tests.
- Ignored tests are recorded as skipped instead of passed; parameterised invocations are grouped under their method.
- The Suspicious / Flaky / Highly unstable thresholds and auto-quarantine settings had no effect; both are now applied.
- History storage is thread-safe and the dashboard refreshes on the UI thread.

### Removed
- *Rerun Flaky Tests* (only logged a message) and *Quarantine Test* (toggled an arbitrary test) actions; quarantine is toggled from the dashboard.
- Description items for features that do not exist (trend charts, git correlation, module aggregation, report export, CI/CD integration).

## [2026.8.0] - 2026-11-10

### Changed

- The plugin is now completely free — all features unlocked, no license needed.
- Source code is now open source under Apache-2.0: https://github.com/JirakJ/flaky-test-detector

## [2026.3.6] - 2026-04-17

### Changed
- Enabled freemium licensing via `<product-descriptor optional="true">`. Plugin remains fully functional without a license; premium features may be introduced in future minor releases.


## [2026.2.16] - 2026-02-28

### Changed
- Refreshed plugin icon for better visibility and modern look
- Updated dependencies and platform compatibility

## [2026.2.5] - 2026-02-14

### Fixed
- Removed deprecated API usage (postStartupActivity → projectActivity, StatusBarWidget.getPresentation)
- No experimental API usage


## [2026.2.4] - 2026-02-13

### Added
- Added ProGuard obfuscation for code protection


## [2026.2.3] - 2026-02-13

### Changed
- Version bump for JetBrains Marketplace update


## [2026.2.2] - 2026-02-13

### Changed
- Version bump for JetBrains Marketplace publication


## [2026.2.1] - 2026-02-13

### Added
- Initial release
- Automatic test run history tracking (SMTRunner/TestStatusListener)
- Flakiness score calculation with configurable thresholds
- Gutter icons showing flakiness level next to @Test annotations
- Flaky Tests tool window with table, sparkline visualization, and trend indicators
- Quarantine mode for known-flaky tests
- Rerun all flaky tests action (Alt+Shift+F)
- Configurable settings (retention, thresholds, minimum runs)
- Unlimited test tracking and history retention
- Support for IntelliJ Platform 232.0+ (2023.2+)
