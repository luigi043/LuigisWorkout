# 🏋️‍♂️ Luigi's Workout

[![Android Version](https://img.shields.io/badge/Android-8.0%2B-green.svg)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9-blue.svg)](https://kotlinlang.org/)
[![MVVM](https://img.shields.io/badge/Architecture-MVVM-orange.svg)](https://developer.android.com/topic/libraries/architecture/viewmodel)
[![SafeToOffline](https://img.shields.io/badge/Offline-First-blueviolet.svg)](https://developer.android.com/training/data-storage/room)

**Luigi's Workout** is a premium, offline-first Android application designed to help users lose weight through a combination of daily calorie tracking, structured home-based workout plans, and cost-effective recipe suggestions from local Portuguese supermarkets.

---

## ✨ Features

### 🥗 Calorie & Meal Tracking
- **Daily Food Log:** Track every meal (Breakfast, Lunch, Dinner, Snacks) with detailed macro-nutrients (Protein, Carbs, Fats).
- **Local Portuguese Focus:** Integrated database with products and pricing from **Continente, Pingo Doce, and Lidl**.
- **Smart Recipes:** Curated list of high-protein, budget-friendly recipes with step-by-step instructions.
- **Water Tracker:** Stay hydrated with a daily progress indicator.

### 🏠 Home Workout Plans
- **Progressive Training:** Structured workout days (Leg Day, Upper Body Push, HIIT) designed for home environments.
- **Exercise Library:** Detailed instructions for each movement including difficulty levels and target muscle groups.
- **Interactive Timer:** Built-in workout timer to manage rest periods and sets.
- **Daily Reminders:** Local notifications via WorkManager to maintain your routine.

### 📈 Progress Insights
- **Weight Tracking:** Log daily weight and monitor BMI changes.
- **Visual Analytics:** Interactive charts using **MPAndroidChart** for weight trends and calorie consumption.
- **Motivation:** Dynamic motivational tips to keep you inspired every day.

---

## 🛠 Tech Stack

- **Language:** Kotlin + Coroutines (Asynchronous programming).
- **UI Framework:** XML with ViewBinding (Material Components 3).
- **Architecture:** MVVM (Model-View-ViewModel) with LiveData.
- **Database:** Room (SQLite) for 100% offline functionality.
- **Navigation:** Jetpack Navigation Component.
- **Background Tasks:** WorkManager for reliable daily notifications.
- **JSON Handling:** Gson for data serialization.
- **Charts:** MPAndroidChart for visual progress tracking.

---

## 📂 Project Structure

```text
LuigisWorkout/
├── app/
│   ├── src/main/java/.../
│   │   ├── data/           # Models (Recipe, Exercise), Room DB, AppContent
│   │   ├── ui/             # Fragments (Home, Workout, MealPlan, Progress)
│   │   ├── viewmodel/      # ViewModel logic and State management
│   │   ├── notifications/  # WorkManager and Notification logic
│   │   └── utils/          # Extensions and helper classes
│   └── src/main/res/       # Layouts, Nav Graph, and Drawables
└── build.gradle            # Project dependencies (Room, Navigation, etc.)
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
