# Flaky Test Detector — Software Requirements Specification (SRS)

**Verze:** 1.0
**Datum:** 2026-02-13
**Autor:** Jakub Jirák

---

## 1. Úvod

### 1.1 Účel
Flaky Test Detector je JetBrains IDE plugin, který detekuje, sleduje a vizualizuje nestabilní (flaky) testy — testy, které někdy projdou a jindy selžou bez změny kódu. Plugin analyzuje historii testovacích běhů přímo v IDE a poskytuje vývojářům jasný přehled o spolehlivosti jejich test suite.

### 1.2 Rozsah
Plugin se integruje do JetBrains IDE (IntelliJ IDEA, WebStorm, PyCharm, GoLand, Rider) a pracuje s výsledky testů z IDE test runneru. Sbírá historii výsledků, počítá flakiness skóre a zobrazuje výsledky v gutteru, tool window a test runneru.

### 1.3 Definice
- **Flaky test** — test jehož výsledek (pass/fail) se mění mezi běhy bez změny kódu
- **Flakiness score** — procentuální míra nestability testu (0% = stabilní, 100% = vždy jiný výsledek)
- **Run history** — chronologická sekvence výsledků testu (pass/fail/skip/error)
- **Quarantine** — stav testu kdy je automaticky označen jako nestabilní a neblokuje CI

### 1.4 Verzování
`2026.2.1` — formát `YYYY.M.R` kde R = pořadí release v měsíci

---

## 2. Celkový popis

### 2.1 Perspektiva produktu
Flaky testy jsou jeden z nejvíc frustrujících problémů v softwarovém vývoji. Způsobují:
- Ztrátu důvěry v test suite
- Zbytečné re-runy CI pipeline
- Developer frustrace a ignorování testů
- Plýtvání časem při debuggování false failures

Žádný existující JetBrains plugin neřeší detekci flaky testů systematicky. Built-in test runner zobrazuje pouze poslední výsledek.

### 2.2 Uživatelské skupiny
| Skupina | Popis | Potřeby |
|---------|-------|---------|
| Individual developer | Vývojář pracující na projektu | Vidět které testy jsou flaky, rozhodnout se zda fixovat |
| Tech lead | Vedoucí týmu | Celkový přehled health test suite, prioritizace fixů |
| QA engineer | Tester | Odlišit skutečné regrese od flaky failures |

### 2.3 Omezení
- Plugin pracuje s lokální historií testových běhů (ne CI server)
- Vyžaduje JetBrains IDE 2023.2+ (platformVersion 232)
- Historie se ukládá per-project lokálně (JSON)
- Free tier: max 500 testů sledovaných, 30 dní historie

---

## 3. Funkční požadavky

### 3.1 Test Run History Tracking (FR-001)
**Priorita:** Kritická
**Popis:** Plugin automaticky zaznamenává výsledek každého testu po každém test runu v IDE.

**Detaily:**
- Listener na `SMTRunnerEventsListener` pro zachycení výsledků
- Ukládá: test FQN, výsledek (PASS/FAIL/ERROR/SKIP), timestamp, duration, stack trace (při fail)
- Perzistence do JSON souboru v `.idea/flaky-test-detector/` adresáři
- Automatická rotace: výchozí 90 dní historie (konfigurovatelné)
- Podpora JUnit 4/5, TestNG, Kotest, Pytest, Jest (framework-agnostic přes SMTRunner)

**Akceptační kritéria:**
- Po spuštění testů se výsledky automaticky uloží
- Historie přežije restart IDE
- Žádný měřitelný dopad na výkon test runneru

### 3.2 Flakiness Score Calculation (FR-002)
**Priorita:** Kritická
**Popis:** Plugin počítá flakiness skóre pro každý test.

**Algoritmus:**
```
flakiness_score = (počet_změn_výsledku / (celkový_počet_běhů - 1)) * 100

kde změna_výsledku = výsledek[i] != výsledek[i-1]
```

**Kategorizace:**
| Skóre | Kategorie | Barva | Ikona |
|:-----:|-----------|-------|-------|
| 0% | Stabilní | Zelená | ✅ |
| 1-15% | Podezřelý | Žlutá | ⚠️ |
| 16-50% | Flaky | Oranžová | 🔶 |
| 51-100% | Vysoce nestabilní | Červená | 🔴 |

**Minimum běhů:** Skóre se počítá až po 3+ bězích (konfigurovatelné)

### 3.3 Gutter Icons (FR-003)
**Priorita:** Vysoká
**Popis:** V gutteru editoru vedle `@Test` anotace zobrazit ikonu indikující flakiness úroveň.

**Detaily:**
- Ikona odpovídá kategorii z FR-002
- Tooltip: "Flaky: 45% (9/20 runs changed result)"
- Click: otevře detail panel s historií
- Ikony se aktualizují po každém test runu
- Podpora: Java, Kotlin, Python, JavaScript/TypeScript, Go

### 3.4 Flaky Test Dashboard — Tool Window (FR-004)
**Priorita:** Vysoká
**Popis:** Dedikované tool window "Flaky Tests" s přehledem všech sledovaných testů.

**Obsahuje:**
- Tabulka: Test name | Class | Flakiness % | Last 10 runs (sparkline) | Trend | Actions
- Filtry: All / Flaky only / Stable only / By module
- Sortování: by flakiness score, by name, by last failure
- Sparkline: vizuální sekvence posledních N výsledků (zelená/červená tečky)
- Trend šipka: zlepšuje se / zhoršuje se / stabilní
- Akce: "Run again", "Go to test", "Quarantine", "Add to ignore"

### 3.5 Test Runner Integration (FR-005)
**Priorita:** Vysoká
**Popis:** Rozšíření standardního test runner okna o flakiness informace.

**Detaily:**
- Sloupec "Flakiness" v test tree
- Ikona vedle názvu testu
- Kontextové menu: "View flakiness history", "Quarantine test"
- Po selhání testu: notification "This test has 65% flakiness — likely a flaky failure"

### 3.6 Quarantine Mode (FR-006)
**Priorita:** Střední
**Popis:** Možnost "karanténovat" flaky test — označit ho jako known-flaky.

**Detaily:**
- Quarantined testy se vizuálně odliší (přeškrtnuté, šedá barva)
- Volitelně: generování `@Disabled("Flaky - quarantined")` anotace
- Seznam karantén v settings
- Auto-quarantine: automaticky karanténovat testy s flakiness > X% (konfigurovatelné, default off)

### 3.7 Rerun Flaky Tests (FR-007)
**Priorita:** Střední
**Popis:** Akce "Rerun all flaky tests" — spustí jen testy s nenulovou flakiness.

**Detaily:**
- Toolbar button v Flaky Tests tool window
- Spustí testy v existujícím test runneru
- Výsledky se přidají do historie
- Shortcut: Alt+Shift+F

### 3.8 Statistics & Reports (FR-008)
**Priorita:** Střední (Pro)
**Popis:** Rozšířená statistika a export reportů.

**Detaily (Pro tier):**
- Flakiness trend chart (časová osa)
- Top 10 nejhorších flaky testů
- Module-level aggregace
- Suite health score (% stabilních testů)
- Export: JSON, CSV, HTML report
- CI integration: generování JUnit XML s flakiness annotations

### 3.9 Git Correlation (FR-009)
**Priorita:** Nízká (Pro)
**Popis:** Korelace flakiness s Git historií.

**Detaily:**
- "Test became flaky after commit X" detekce
- Blame integration: kdo naposledy upravil flaky test
- Branch comparison: je test flaky jen na tomto branch?

### 3.10 Settings & Configuration (FR-010)
**Priorita:** Kritická
**Popis:** Konfigurace pluginu.

**Nastavení:**
- Enable/disable plugin
- History retention: 30/60/90/180 dní
- Minimum runs for scoring: 3-10
- Flakiness thresholds (podezřelý, flaky, vysoce nestabilní)
- Auto-quarantine: on/off + threshold
- Gutter icons: on/off
- Notification level: all/flaky only/none
- Data storage path

---

## 4. Nefunkční požadavky

### 4.1 Výkon (NFR-001)
- Zaznamenání výsledků: < 50ms per test (async)
- Skóre výpočet: < 200ms pro 1000 testů
- Tool window refresh: < 500ms
- Startup: < 100ms (lazy loading)
- Paměť: max 50MB pro 10K testů s 90-dní historií

### 4.2 Kompatibilita (NFR-002)
- IntelliJ Platform 232.0+ (2023.2+)
- Bez horní hranice kompatibility
- Java 17+ runtime
- Podporované IDE: IntelliJ IDEA, WebStorm, PyCharm, GoLand, Rider, CLion

### 4.3 Bezpečnost (NFR-003)
- Žádná data neopouští lokální stroj (Free tier)
- Stack traces se ukládají lokálně
- Žádná telemetrie bez souhlasu

### 4.4 Lokalizace (NFR-004)
- UI v angličtině
- Resource bundle pro budoucí lokalizaci

---

## 5. Datový model

```
TestRecord {
    testId: String (FQN)
    className: String
    methodName: String
    module: String?
    results: List<TestRunResult>
    flakinesScore: Double
    quarantined: Boolean
    tags: Set<String>
}

TestRunResult {
    timestamp: Instant
    outcome: PASS | FAIL | ERROR | SKIP
    duration: Duration
    stackTrace: String?
    commitHash: String?
}
```

---

## 6. UI Wireframes

### 6.1 Tool Window Layout
```
┌─────────────────────────────────────────────────┐
│ Flaky Tests                    🔄 ▶️ ⚙️ 📊     │
├─────────────────────────────────────────────────┤
│ Filter: [All ▾] Module: [All ▾] Search: [____] │
├──────────────────┬───────┬──────────┬───────────┤
│ Test             │ Score │ Last 10  │ Trend     │
├──────────────────┼───────┼──────────┼───────────┤
│ 🔴 testPayment  │  67%  │ ●●○●○●○● │ ↗ worse   │
│ 🔶 testLogin    │  33%  │ ●●●○●●●○ │ → stable  │
│ ⚠️ testSearch   │  12%  │ ●●●●●●○● │ ↘ better  │
│ ✅ testLogout   │   0%  │ ●●●●●●●● │ → stable  │
└──────────────────┴───────┴──────────┴───────────┘
```

### 6.2 Gutter Icon Tooltip
```
┌─────────────────────────────┐
│ ⚠️ Flaky Test               │
│ Score: 33% (Flaky)          │
│ Last 10: ●●●○●●●○●●        │
│ Total runs: 24              │
│ First seen flaky: 2026-01-28│
│ [View History] [Quarantine] │
└─────────────────────────────┘
```
