# Flaky Test Detector — JetBrains Marketplace Listing

## Plugin Name
Flaky Test Detector

## Tagline
Detect, track, and eliminate flaky tests — right in your IDE.

## Short Description
Automatically tracks test run history, calculates flakiness scores, and shows unreliable tests with gutter icons and a dedicated dashboard. Stop wasting time on false failures.

## Full Description (HTML)
```html
<p>
  <b>Stop wasting time investigating false test failures.</b> Flaky Test Detector automatically
  monitors your test runs, calculates reliability scores, and gives you clear, actionable data
  about which tests you can trust — and which ones need fixing.
</p>

<h3>🔍 How It Works</h3>
<ol>
  <li>Run your tests as usual — the plugin silently records every result</li>
  <li>After 3+ runs, flakiness scores appear as gutter icons next to @Test</li>
  <li>Open the Flaky Tests dashboard for a complete overview</li>
  <li>Quarantine or fix — your choice, backed by data</li>
</ol>

<h3>✨ Free Features</h3>
<ul>
  <li><b>Automatic History Tracking</b> — records pass/fail/error/skip for every test run</li>
  <li><b>Flakiness Score</b> — percentage showing how often a test changes its result</li>
  <li><b>Gutter Icons</b> — colored indicators (⚠️🔶🔴) on unstable Java/Kotlin test methods</li>
  <li><b>Flaky Tests Dashboard</b> — sortable table with sparkline and trend</li>
  <li><b>Quarantine Mode</b> — mark known-flaky tests</li>
  <li><b>Configurable Thresholds</b> — define what "flaky" means for your project</li>
</ul>

<h3>More</h3>
<ul>
  <li><b>Auto-Quarantine</b> — automatically quarantine tests above a configured score</li>
  <li><b>History Retention</b> — configurable number of days</li>
</ul>

<h3>📊 Flakiness Categories</h3>
<table>
  <tr><th>Score</th><th>Category</th><th>Icon</th></tr>
  <tr><td>0%</td><td>Stable</td><td>✅</td></tr>
  <tr><td>1-15%</td><td>Suspicious</td><td>⚠️</td></tr>
  <tr><td>16-50%</td><td>Flaky</td><td>🔶</td></tr>
  <tr><td>51-100%</td><td>Highly Unstable</td><td>🔴</td></tr>
</table>

<h3>🔧 Works With</h3>
<p>
  Any test framework supported by JetBrains IDEs: JUnit 4/5, TestNG, Kotest, Pytest, Jest,
  Mocha, Go test, and more. Framework-agnostic via IDE test runner integration.
</p>

<h3>🔗 Part of the Developer Productivity Toolkit</h3>
<p>
  Combine with <b>Dead Code & Drift Radar</b>, <b>Slow Build Hotspots</b>, and
  <b>Technical Debt Accountant</b> for comprehensive code health monitoring.
</p>
```

## Tags
testing, flaky-tests, test-quality, code-quality, test-runner, developer-tools, productivity

## Category
Code tools

## Pricing
Free and open source (Apache-2.0) — all features unlocked, no license needed. Source: https://github.com/JirakJ/flaky-test-detector

## Suggested Screenshots
1. Gutter icons showing flakiness levels next to @Test annotations
2. Flaky Tests dashboard with sparkline and trend indicators
3. Settings panel with configurable thresholds
4. Tooltip showing detailed flakiness information
5. Quarantined test visual treatment
