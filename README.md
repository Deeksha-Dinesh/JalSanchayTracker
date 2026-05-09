# Jal-Sanchay Tracker 💧
### Android App — Rainwater Harvesting Tracker (Project #86)

---

## Quick Start in Android Studio

1. **Open Project**: File → Open → select the `JalSanchayTracker` folder
2. **Sync Gradle**: Click "Sync Now" when prompted (downloads all dependencies)
3. **Add Launcher Icons**: Right-click `res` → New → Image Asset → create `ic_launcher`
4. **Run**: Connect device or start emulator → click ▶ Run

---

## Project Structure

```
app/src/main/java/com/jalsanchay/tracker/
├── data/
│   ├── db/
│   │   ├── AppDatabase.kt        ← Room singleton
│   │   ├── RainfallDao.kt        ← All DB queries
│   │   └── RainfallEntry.kt      ← Table entity
│   └── repository/
│       └── WaterRepository.kt    ← Data access layer
├── ui/
│   ├── setup/    SetupFragment.kt       ← Roof + tank setup
│   ├── entry/    DataEntryFragment.kt   ← Log rainfall (mm)
│   ├── dashboard/ DashboardFragment.kt  ← Tank visual + stats
│   ├── report/   MonthlyReportFragment.kt ← Monthly totals
│   └── tips/     TipsFragment.kt        ← 10 harvesting tips
├── viewmodel/
│   └── WaterViewModel.kt         ← Central ViewModel
├── adapter/
│   ├── RainfallEntryAdapter.kt   ← RecyclerView for entries
│   └── TipsAdapter.kt            ← RecyclerView for tips
├── utils/
│   ├── WaterCalculator.kt        ← Core formula logic
│   ├── InputValidator.kt         ← Validation with sealed class
│   └── TipsData.kt               ← 10 tips static data
└── MainActivity.kt               ← NavHostFragment + BottomNav
```

---

## Core Formula

```
Litres Saved = Roof Area (sq.ft) × Rainfall (mm) × 0.0929 × Runoff Coefficient
```

Where:
- `0.0929` converts sq.ft → sq.metres
- Runoff Coefficient: `0.85` (concrete), `0.75` (metal), `0.60` (gravel)

---

## 5 Screens

| Screen | Purpose |
|--------|---------|
| **Setup** | Enter roof area, tank capacity, roof type |
| **Dashboard** | Tank fill visual, total litres, monthly savings, bar chart |
| **Log Rainfall** | Manual mm entry with live litres preview |
| **Monthly Report** | Full list of entries, totals, avg rainfall |
| **Tips** | 10 expert rainwater harvesting tips |

---

## Dependencies Used

| Library | Purpose |
|---------|---------|
| Room 2.6.1 | Local database for historical entries |
| ViewModel + LiveData | Lifecycle-aware UI state |
| Navigation Component | Fragment navigation + bottom nav |
| MPAndroidChart | Bar chart on dashboard |
| Material Components | UI components & theming |
| Kotlin Coroutines | Async DB operations |

---

## Success Criteria (from spec)

- ✅ Tank visual fills based on data entered
- ✅ Monthly report with total water saved
- ✅ Input validation (non-numeric inputs handled gracefully)
- ✅ Tips section for better water harvesting

---

## Notes

- **Launcher Icons**: Generate via Android Studio → right-click `res` → New → Image Asset
- **Min SDK**: API 24 (Android 7.0) — covers ~94% of active devices
- **Database**: Auto-created on first launch, persists across app restarts
