# Screen index

The [public portfolio demo](https://luigis-workout-portfolio.vercel.app/) was checked in Edge at viewport widths of 320, 390, and 1440 CSS pixels. The automated walkthrough verifies that the app stays within 430 pixels, has no horizontal overflow, and retains entries after reload. The test logs a sample meal and a weight, runs the workout timer, saves an exercise, changes the water count, and clears the demo.

| Browser screen | Workflow | Phone | Desktop |
| --- | --- | --- | --- |
| Home | Add a glass of water | [01](browser/390-01-home.png) | [01](browser/1440-01-home.png) |
| Meals | Prefill and save a sample breakfast | [02](browser/390-02-meals.png) | [02](browser/1440-02-meals.png) |
| Workout | Choose Legs, start the timer, and check Goblet squat | [03](browser/390-03-workout.png) | [03](browser/1440-03-workout.png) |
| Progress | Save the workout and log 82.5 kg | [04](browser/390-04-progress.png) | [04](browser/1440-04-progress.png) |

Browser entries are synthetic and remain in browser storage. Android uses Room and includes the full training plan, recipes, exercise examples, shopping list, calendar, and notification permission. Browser screenshots document the demo.
