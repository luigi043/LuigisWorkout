# 🏋️‍♂️ Luigi's Workout

[![Android Version](https://img.shields.io/badge/Android-8.0%2B-green.svg)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9-blue.svg)](https://kotlinlang.org/)
[![MVVM](https://img.shields.io/badge/Architecture-MVVM-orange.svg)](https://developer.android.com/topic/libraries/architecture/viewmodel)
[![SafeToOffline](https://img.shields.io/badge/Offline-First-blueviolet.svg)](https://developer.android.com/training/data-storage/room)

**Luigi's Workout** is a premium, offline-first Android application designed for targeted weight loss (115kg to ~95kg goal). It combines structured 12-week training phases, precision calorie tracking with a Portuguese supermarket focus, and local progress monitoring.

---

## ✨ Key Features

### 🥗 Smart Nutrition & Tracking
- **Precision Meal Log:** Track Breakfast, Lunch, Dinner, Snacks, and Drinks with full macro-nutrient breakdown (Protein, Carbs, Fats).
- **Portuguese Supermarket Focus:** Recipes optimized for budget-friendly shopping at **Lidl, Pingo Doce, Continente, and Aldi**.
- **Shopping List:** Integrated tool to manage your grocery needs directly from the app's recipe database.
- **Water Tracker:** Interactive daily progress indicator to hit hydration targets.
- **Creatine Window:** Specific reminders and recipes for slow-digesting protein and creatine timing.

### 🏠 Structured 12-Week Workout Plan
- **Evolutionary Training:** A 12-week plan divided into three metabolic phases:
    1. **Foundation (Weeks 1-4):** Building strength and form (3 sets, 10-12 reps).
    2. **Intensification (Weeks 5-8):** Increasing volume and drop sets (4 sets, 8-10 reps).
    3. **Metabolic Peak (Weeks 9-12):** Maximum fat loss with supersets and HIIT bike intervals.
- **5-Day Split:** Push, Pull, Legs, Shoulders & Arms, and Full Body Conditioning.
- **MuscleWiki Integration:** One-tap access to professional exercise demonstrations for every movement.
- **Interactive Timer:** Built-in workout timer with set-by-set tracking.

### 📈 Progress & Analytics
- **Visual Trends:** Dynamic charts for Weight Evolution and Daily Calorie consumption (30-day history).
- **Body Metrics:** Automatic BMI calculation and start-vs-goal weight tracking.
- **Motivational Engine:** Dynamic tips based on training volume and nutritional consistency.
- **Local Reminders:** Smart notifications for meals and training sessions via WorkManager.

---

## 🛠 Tech Stack

- **Language:** Kotlin 1.9 + Coroutines (Asynchronous State Management).
- **Architecture:** MVVM (Model-View-ViewModel) + LiveData for reactive UI.
- **UI:** XML with **Material 3** Components and ViewBinding.
- **Database:** **Room** (SQLite) for 100% offline functionality and persistent user history.
- **Navigation:** Jetpack Navigation Component with Single-Activity Architecture.
- **Background Tasks:** **WorkManager** for reliable daily notifications and boot-resilient scheduling.
- **Charts:** **MPAndroidChart** for data visualization.
- **Splash Screen:** Jetpack SplashScreen API for a modern entry experience.

---

## 📂 Project Structure

```text
LuigisWorkout/
├── app/
│   ├── src/main/java/.../
│   │   ├── data/           # Models, Room Entities, DAOs, and AppContent
│   │   ├── ui/             # Fragments (Home, Workout, MealPlan, Progress, ShoppingList)
│   │   ├── viewmodel/      # State management and Business logic
│   │   ├── notifications/  # WorkManager Workers and Notification channels
│   │   └── utils/          # Extensions, Formatters, and Constants
│   └── src/main/res/       # Material 3 Themes, Nav Graph, and Drawables
└── build.gradle            # Modern Gradle (KSP for Room, Version 17 JVM)
```

---

## 🚀 Getting Started

1. **Clone the repository:**
   ```bash
   git clone https://github.com/luigi043/LuigisWorkout.git
   ```
2. **Open in Android Studio:**
   - Go to `File > Open` and select the project folder.
   - Wait for Gradle to sync (Java 17 required).
3. **Run on Device/Emulator:**
   - Minimum SDK: API 26 (Android 8.0)
   - Target SDK: API 34 (Android 14)

---

## 📅 2024 Roadmap

- [ ] **Sync functionality:** Firebase/Supabase Integration for cross-device cloud backups.
- [ ] **Barcode scanner:** ML Kit integration to quickly add food items from labels.
- [ ] **Jetpack Compose migration:** Refactoring the UI for enhanced fluidity and animations.
- [ ] **WearOS Extension:** Track workout sets directly from your wrist.

---

## 👨‍💻 Author

**Luigi** — Passionate about fitness, coding, and healthy living in Portugal.
