# Screen index

The [public portfolio demo](https://luigis-workout-portfolio.vercel.app/) was checked in Edge at viewport widths of 320, 390, and 1440 CSS pixels. The automated walkthrough verifies that the app stays within 430 pixels, has no horizontal overflow, and retains entries after reload. The test logs a sample meal and a weight, runs the workout timer, saves an exercise, changes the water count, and clears the demo.

| Browser screen | Workflow | Phone | Desktop |
| --- | --- | --- | --- |
| Home | Add a glass of water | [01](browser/390-01-home.png) | [01](browser/1440-01-home.png) |
| Meals | Prefill and save a sample breakfast | [02](browser/390-02-meals.png) | [02](browser/1440-02-meals.png) |
| Workout | Choose Legs, start the timer, and check Goblet squat | [03](browser/390-03-workout.png) | [03](browser/1440-03-workout.png) |
| Progress | Save the workout and log 82.5 kg | [04](browser/390-04-progress.png) | [04](browser/1440-04-progress.png) |

Browser entries are synthetic and remain in browser storage. Android uses Room and includes the full training plan, recipes, exercise examples, shopping list, calendar, and notification permission. Browser screenshots document the demo.

## Android walkthrough

The native Android captures use synthetic entries on an API 35 emulator. The debug and instrumentation APKs were built with Gradle 8.2.1, JDK 17, and Android SDK Platform 34. The JVM test task reported `NO-SOURCE`. The separate browser screenshots show the public demo.

The instrumentation run passed one test. A prefilled drink recipe retained its meal type and reached quantity validation with zero calories. Saving with zero quantity showed the expected field error. The [tested Android debug preview](https://github.com/luigi043/LuigisWorkout/releases/tag/android-debug-0c1742a) requires repository access.

To repeat the regression on Windows, build and install both APKs on a test emulator. Replace `emulator-5554` with its serial if needed.

```powershell
.\gradlew.bat --no-daemon --max-workers=1 "-Pkotlin.compiler.execution.strategy=in-process" assembleDebug assembleDebugAndroidTest
adb -s emulator-5554 install -r app/build/outputs/apk/debug/app-debug.apk
adb -s emulator-5554 install -r -t app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk
adb -s emulator-5554 shell am instrument -w -e class com.luigisworkout.MealLoggingTest com.luigisworkout.test/androidx.test.runner.AndroidJUnitRunner
```

| Screen or dialog | How to reach it | Capture |
| --- | --- | --- |
| Notification permission | Open the app on a fresh installation on API 33 or later | [01](native/01-notification-permission.png) |
| Home calendar and daily summary | Open the app or tap Home | [02](native/02-home.png) |
| Water tracker | Scroll down Home and add a glass | [03](native/03-home-water.png) |
| Meals and recipe categories | Tap Meals. The same layout hosts Breakfast, Lunch, Dinner, Snack and Drinks | [04](native/04-meals-breakfast.png) |
| Recipe detail | Tap Protein Oats Bowl in Breakfast | [05](native/05-recipe-detail.png) |
| Log Meal | Choose Add to Diary and check the prefilled values | [06](native/06-log-meal-prefilled.png) |
| Food search | Open Log Meal and type Oat in the food name field | [24](native/24-food-search.png) |
| Saved meal history | Save the prefilled recipe and open History on Meals | [19](native/19-meal-history.png) |
| Delete meal confirmation | Tap the delete control on the saved meal, then dismiss the dialog | [23](native/23-meal-delete-confirmation.png) |
| Shopping List | Tap List on Meals | [07](native/07-shopping-list.png) |
| Workout plan | Tap Workout and open Plan | [09](native/09-workout-plan.png) |
| Track Exercise dialog | Tap Track Progress for Treadmill Warm-Up | [16](native/16-track-exercise.png) |
| Running workout timer | Tap Start on the workout plan | [17](native/17-workout-timer.png) |
| Empty workout history | Open Workout History before saving a session | [18](native/18-workout-history.png) |
| Workout result | Check all ten exercises for the Push session and tap Finish | [20](native/20-workout-result.png) |
| Completed workout | Tap Save Result and confirm the plan offers Redo | [21](native/21-workout-completed.png) |
| Saved workout history | Open Workout History after saving the session | [22](native/22-workout-history-saved.png) |
| Exercise Library | Tap Exercises | [10](native/10-exercise-library.png) |
| Exercise example sheet | Tap Watch next to Barbell Bench Press | [15, loading](native/15-exercise-example.png) |
| Progress before weighing | Tap Progress on a fresh install | [11](native/11-progress.png) |
| Log Weight | Tap Log Weight | [12](native/12-log-weight.png) |
| Weight validation | Tap Save Weight with an empty field | [13](native/13-weight-validation.png) |
| Progress with a saved weight | Log 82.5 kg and return to Progress | [14](native/14-progress-weight-saved.png) |

The MuscleWiki sheet opened, but the embedded video stayed on its loading screen during the walkthrough. The walkthrough completed one workout day. Automatic week advancement and scheduled notification delivery were outside the verified run. The selected values in the week and meal type controls have low contrast against the dark cards in the tested theme.
