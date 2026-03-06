# Changelog

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
