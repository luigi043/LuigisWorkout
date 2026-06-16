# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

**Build & install (debug APK):**
```bash
./gradlew assembleDebug
./gradlew installDebug        # requires connected device/emulator
```

**Run all tests:**
```bash
./gradlew test                # unit tests
./gradlew connectedAndroidTest # instrumented tests (requires device)
```

**Run a single test class:**
```bash
./gradlew test --tests "com.luigisworkout.ExampleUnitTest"
```

**Clean build:**
```bash
./gradlew clean assembleDebug
```

On Windows use `gradlew.bat` instead of `./gradlew`.

## Architecture

Single-module Android app (min SDK 26, target SDK 34, Kotlin + KSP).

**Pattern:** MVVM — `AppContent` (static data) → Room DB (persistent data) → singleton `AppRepository` → `ViewModel` → Fragment/Activity via `LiveData`.

### Data layer

- **`AppContent.kt`** — compile-time static data: all `Recipe` objects, `WorkoutDay` plan (12-week cycle with three phases: Foundation, Intensification, Metabolic Peak), `foodDatabase` (`FoodItem` list), and motivational tips. This is the source of truth for workout and recipe content; it is never stored in Room.
- **`Entities.kt`** — seven Room entities: `MealLog`, `WeightLog`, `WorkoutLog`, `UserProfile` (singleton, `id=1`), `DailyGoal` (keyed by date string `"yyyy-MM-dd"`), `ShoppingItem`, `ExerciseLog`.
- **`AppDatabase`** — Room singleton, version 3, uses `fallbackToDestructiveMigration` for dev (schema changes wipe local data — replace with proper Room migrations before shipping a release build).
- **`AppRepository`** — single repository, instantiated once in `LuigisWorkoutApp.repository` and injected into every ViewModel through `appViewModelFactory`. All suspend functions dispatch to `Dispatchers.IO`. Returns `LiveData` for reactive queries and `suspend` functions for one-shot writes. Read-modify-write fields (e.g. `adjustWaterCount`) are atomic inside a single IO coroutine.

### Utilities (`util/`)

- **`AppClock`** — single source of truth for today's date via `java.time.LocalDate` (API 26+). Use `AppClock.todayString()` / `dateXDaysAgoString(n)` instead of constructing `SimpleDateFormat` ad-hoc.
- **`TodayDateLiveData`** — emits the current ISO date, re-emits on `ACTION_DATE_CHANGED` / `ACTION_TIME_CHANGED` / `ACTION_TIMEZONE_CHANGED`. Hand to ViewModels and `switchMap` over it for any "today" query so they refresh at midnight instead of getting stuck on yesterday's data.
- **`SingleLiveEvent`** — one-shot LiveData. Use for navigation / `finish()` / snackbar events that must not replay on rotation.

### ViewModel layer (`ViewModels.kt`)

ViewModels extend `ViewModel` (not `AndroidViewModel`) and receive the shared `AppRepository` + `TodayDateLiveData` via the `appViewModelFactory(app)` initializer-DSL factory. One ViewModel per screen:

| ViewModel | Fragment/Activity |
|---|---|
| `HomeViewModel` | `HomeFragment` |
| `MealPlanViewModel` | `MealPlanFragment` + `ShoppingListFragment` |
| `WorkoutViewModel` | `WorkoutFragment` (holds timer state) |
| `ProgressViewModel` | `ProgressFragment` |
| `LogMealViewModel` | `LogMealActivity` |
| `LogWeightViewModel` | `LogWeightActivity` |

`WorkoutViewModel` owns the workout timer as a coroutine job (`timerJob`), persisting elapsed time across tab switches via `viewModelScope` (uses `value =`, not `postValue`, since `viewModelScope` is Main).

### UI layer

**Navigation:** Single `MainActivity` hosts a `NavHostFragment`. Bottom navigation links the four top-level destinations (`homeFragment`, `mealPlanFragment`, `workoutFragment`, `progressFragment`) defined in `res/navigation/nav_graph.xml`. The toolbar and bottom nav are hidden for non-top-level destinations.

**Secondary activities:** `LogMealActivity` and `LogWeightActivity` are standalone activities launched outside the nav graph.

**ViewBinding** is enabled app-wide; all fragments and activities use generated binding classes.

**Charts:** `MPAndroidChart` (PhilJay, v3.1.0) for weight trend and calorie history in `ProgressFragment`.

**RecyclerView adapters** all use `ListAdapter` + `DiffUtil.ItemCallback` (no `notifyDataSetChanged`). Item keys: Room-id where available, otherwise `name` for static `AppContent` types.

### Notifications

`DailyNotificationWorker` is a `CoroutineWorker` that sends meal and workout reminders. Worker type is selected via `KEY_TYPE` input data (`breakfast`, `lunch`, `dinner`, `workout`). Skips silently when `POST_NOTIFICATIONS` is denied on Android 13+. Channels are registered in `LuigisWorkoutApp`: `MEAL_CHANNEL_ID`, `WORKOUT_CHANNEL_ID`, `GENERAL_CHANNEL_ID`. PendingIntents use `TaskStackBuilder` so tapping a reminder preserves the user's back stack.

## Key conventions

- Dates are always stored as `"yyyy-MM-dd"` strings, never as timestamps, in Room entities. Always source them via `AppClock` — never construct `SimpleDateFormat` in screen code.
- For "today" LiveData, always go through `todayDate.switchMap { repo.getXByDate(it) }` so the query re-runs after midnight.
- `UserProfile` is a singleton (`id = 1`); save/update logic lives in `AppRepository.saveUserProfile()` which checks for existence before insert vs. update.
- Daily counters use atomic `adjustXxx(delta)` repo methods (see `adjustWaterCount`) — never read on the main thread and write back in a separate coroutine; you'll lose increments on rapid taps.
- Adding new static workout days or recipes: edit `AppContent.workoutPlan` / `AppContent.recipes` only. No DB migration needed.
- Adding new persisted fields: bump `AppDatabase.version`, add a Room migration (or accept `fallbackToDestructiveMigration` wipes data in dev).
- One-shot UI events (finish activity, snackbar) → `SingleLiveEvent`, not `MutableLiveData<Boolean>`.
