# 🏋️‍♂️ Luigi's Workout

[![Android Version](https://img.shields.io/badge/Android-8.0%2B-green.svg)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9-blue.svg)](https://kotlinlang.org/)
[![MVVM](https://img.shields.io/badge/Architecture-MVVM-orange.svg)](https://developer.android.com/topic/libraries/architecture/viewmodel)
[![SafeToOffline](https://img.shields.io/badge/Offline-First-blueviolet.svg)](https://developer.android.com/training/data-storage/room)

**Luigi's Workout** is a premium, offline-first Android application designed to help users lose weight through a combination of daily calorie tracking, structured home-based workout plans, and cost-effective recipe suggestions from local Portuguese supermarkets.

---

## ✨ Features

### 🥗 Calorie & Meal Tracking
- **Daily Food Log:** Track every meal (Pequeno-Almoço, Almoço, Jantar, Lanches) with a focus on macro-nutrients.
- **Local Portuguese Focus:** Integrated database with products from **Continente, Pingo Doce, and Lidl**.
- **Healthy Recipes:** A curated list of quick, high-protein recipes.
- **Water Tracker:** Stay hydrated with a daily progress indicator.

### 🏠 Home Workout Plans
- **12-Week Progressive Plan:** From beginner to advanced, requiring zero equipment.
- **Interactive Timer:** Built-in workout timer with pause/resume functionality.
- **Daily Reminders:** Morning/afternoon notifications to keep you on track.

### 📈 Progress Insights
- **Weight Tracking:** Log your weight and monitor BMI changes over time.
- **Visual Analytics:** Interactive charts (MPAndroidChart) for weight and daily calorie trends.
- **Streaks:** Gamification to encourage daily consistency.

---

## 🛠 Tech Stack

- **UI Framework:** XML with ViewBinding (Material Components 3).
- **Architecture:** MVVM (Model-View-ViewModel) for clean separation of concerns.
- **Database:** Room (SQLite) for 100% offline functionality.
- **Background Tasks:** WorkManager for reliable daily notifications and scheduling.
- **Dependency Injection:** Dagger Hilt (planned/prepared) / ViewModel Factory.
- **Charts:** MPAndroidChart for visual progress tracking.

---

## 📂 Project Structure

```text
LuigisWorkout/
├── app/
│   ├── src/main/java/.../data/          # Entities, DAOs, Repository
│   ├── src/main/java/.../ui/            # Fragments and Activities
│   ├── src/main/java/.../viewmodel/     # Business logic and state
│   └── src/main/res/layout/             # XML UI Layouts
├── gradle/                              # Build configuration
└── build.gradle                         # Root project build file
```

---

## 🚀 Getting Started

1. **Clone the repository:**
   ```bash
   git clone https://github.com/luigi043/LuigisWorkout.git
   ```
2. **Open in Android Studio:**
   - Go to `File > Open` and select the project folder.
   - Wait for Gradle to sync (Internet required for first-time dependencies).
3. **Run on Device/Emulator:**
   - Minimum SDK: API 26 (Android 8.0)
   - Target SDK: API 34 (Android 14)

---

## 📊 Future Roadmap

- [ ] Sync functionality (Firebase Integration).
- [ ] Barcode scanner for food items.
- [ ] Jetpack Compose migration for enhanced UI fluidity.
- [ ] Integration with Google Fit / Health Connect.

---

## 👨‍💻 Author

**Luigi** — Passionate about fitness, coding, and healthy living in Portugal.
