# Flaky Test Detector — Business Requirements Specification (BRS)

**Verze:** 1.0
**Datum:** 2026-02-13
**Autor:** Jakub Jirák

---

## 1. Executive Summary

Flaky Test Detector je JetBrains IDE plugin, který řeší jeden z nejvíc frustrujících problémů ve vývoji softwaru — nestabilní testy. Plugin sleduje historii testových běhů, počítá flakiness skóre, a dává vývojářům jasná data pro rozhodování: fixovat, karanténovat, nebo ignorovat.

**Business model:** Freemium — free tier pro individuální vývojáře, Pro pro pokročilé funkce a export.

---

## 2. Problém

### 2.1 Situace na trhu
- **73% vývojářů** reportuje flaky testy jako top frustraci (Google Engineering Practices Survey)
- Flaky testy stojí průměrný tým **4-8 hodin/týden** na investigaci false failures
- **Žádný JetBrains plugin** neřeší flaky test detection systematicky
- Existující řešení (Launchable, BuildPulse) jsou CI-side SaaS — ne IDE-integrated

### 2.2 Cílová skupina
| Segment | Velikost | Pain level | Willingness to pay |
|---------|:--------:|:----------:|:------------------:|
| Java/Kotlin vývojáři | ~5M | Vysoký | Střední |
| Python vývojáři | ~3M | Vysoký | Střední |
| JavaScript/TS vývojáři | ~4M | Střední | Nízký |
| QA/Test engineers | ~2M | Velmi vysoký | Vysoký |
| Tech leads | ~1M | Vysoký | Vysoký |

### 2.3 Konkurenční analýza
| Řešení | Typ | Cena | IDE integrace | Naše výhoda |
|--------|-----|------|:-------------:|-------------|
| **Launchable** | CI SaaS | $$$$ | ❌ | IDE-native, okamžitý feedback |
| **BuildPulse** | CI SaaS | $$$ | ❌ | Lokální, žádný vendor lock-in |
| **Flaky test retries** (Gradle/Maven) | Build tool | Free | Částečná | Tracking + analytics, ne jen retry |
| **Manual tracking** | Spreadsheet | Free | ❌ | Automatizace, nulový overhead |
| **JetBrains built-in** | IDE | Free | ✅ | Nemá flakiness tracking |

**Závěr:** Blue ocean v IDE-native flaky test detection.

---

## 3. Byznys cíle

### 3.1 KPIs (první rok)
| Metrika | Target | Stretch |
|---------|:------:|:-------:|
| Downloads | 15,000 | 30,000 |
| MAU | 5,000 | 10,000 |
| Pro konverze | 3% | 5% |
| MRR | $750 | $2,500 |
| Rating | 4.0+ | 4.5+ |
| Retention (30d) | 40% | 55% |

### 3.2 Revenue projekce (konzervativní)
| Měsíc | Downloads | Pro subs | MRR |
|:-----:|:---------:|:--------:|:---:|
| 3 | 3,000 | 45 | $225 |
| 6 | 8,000 | 160 | $800 |
| 12 | 15,000 | 375 | $1,875 |
| 18 | 25,000 | 750 | $3,750 |

---

## 4. Monetizační model

### 4.1 Tier struktura
| Feature | Free | Pro ($5/mo) |
|---------|:----:|:-----------:|
| Test run history tracking | ✅ | ✅ |
| Flakiness score calculation | ✅ | ✅ |
| Gutter icons | ✅ | ✅ |
| Flaky Tests tool window | ✅ | ✅ |
| Test runner integration | ✅ | ✅ |
| Manual quarantine | ✅ | ✅ |
| Rerun flaky tests | ✅ | ✅ |
| Max tracked tests | 500 | Unlimited |
| History retention | 30 dní | Unlimited |
| Sparkline (last 10 runs) | ✅ | Last 50 |
| Flakiness trend chart | ❌ | ✅ |
| Module-level aggregation | ❌ | ✅ |
| Suite health score | ❌ | ✅ |
| Export (JSON/CSV/HTML) | ❌ | ✅ |
| Git correlation | ❌ | ✅ |
| Auto-quarantine rules | ❌ | ✅ |
| CI report generation | ❌ | ✅ |
| Priority support | ❌ | ✅ |

### 4.2 Freemium gate placement
Free tier je **plně funkční** pro denní use case. Pro tier přidává:
- **Scale** (unlimited tests/history)
- **Analytics** (trends, aggregace)
- **Integration** (Git, CI export)
- **Automation** (auto-quarantine)

Princip: "vidíš problém zadarmo, řešíš ho efektivně za peníze."

---

## 5. Go-to-Market strategie

### 5.1 Launch plan
1. **Pre-launch (2 týdny):**
   - Blog post: "The Hidden Cost of Flaky Tests"
   - Twitter/LinkedIn teasers se screenshoty
   - Reddit r/java, r/kotlin, r/programming

2. **Launch:**
   - JetBrains Marketplace publikace
   - Product Hunt launch
   - Dev.to article
   - Hacker News Show HN

3. **Post-launch (ongoing):**
   - Weekly "Flaky Test of the Week" social content
   - YouTube: "How to Fix Your Flakiest Tests" series
   - Conference lightning talks

### 5.2 Content marketing
- "How Google Deals with Flaky Tests (and how you can too)"
- "I Reduced Our CI Time by 40% by Fixing Flaky Tests"
- "The Flaky Test Detector: Turning Test Frustration into Actionable Data"

### 5.3 Synergie s portfoliem
Flaky Test Detector doplňuje:
- **Dead Code & Drift Radar** — mrtvé testy jako podmnožina
- **Slow Build Hotspots** — flaky testy zpomalují CI (re-runs)
- **Technical Debt Accountant** — flaky testy jako tech debt s $ hodnotou

---

## 6. Rizika a mitigace

| Riziko | Pravděpodobnost | Dopad | Mitigace |
|--------|:---------------:|:-----:|----------|
| JetBrains přidá built-in flakiness tracking | Střední | Vysoký | Být first mover, budovat user base, přidat CI integration |
| Nízká konverze na Pro | Střední | Střední | A/B test gate placement, přidat team features |
| Performance problémy s velkou historií | Nízká | Střední | Lazy loading, indexing, configurable retention |
| Uživatelé chtějí CI-side řešení | Střední | Nízký | Free CI export v budoucí verzi |

---

## 7. Úspěšnostní kritéria

### MVP (v1.0)
- [ ] Plugin nainstalován, test run history se zaznamenává
- [ ] Flakiness skóre se počítá a zobrazuje v gutteru
- [ ] Tool window ukazuje přehled flaky testů
- [ ] Settings page funguje
- [ ] Build prochází, testy zelené

### Product-Market Fit indikátory
- 40%+ 30-day retention
- 4.0+ rating na Marketplace
- Organický růst downloads > 20% MoM
- Pro konverze > 3%

---

## 8. Timeline

| Fáze | Obsah |
|------|-------|
| Sprint 1 | Core: history tracking, score calculation, persistence |
| Sprint 2 | UI: gutter icons, tool window, settings |
| Sprint 3 | Integration: test runner, quarantine, rerun |
| Sprint 4 | Pro: trends, export, Git correlation |
| Sprint 5 | Polish: performance, docs, marketplace listing |
