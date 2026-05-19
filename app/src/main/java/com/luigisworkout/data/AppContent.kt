package com.luigisworkout.data

// ─── DATA MODELS ───────────────────────────────────────────────────────────────

data class Recipe(
    val id: String,
    val name: String,
    val mealType: String,       // breakfast, lunch, dinner, snack, dessert, pre-workout, post-workout
    val calories: Int,
    val protein: Float,
    val carbs: Float,
    val fat: Float,
    val prepMinutes: Int,
    val servings: Int = 1,
    val ingredients: List<String>,
    val steps: List<String>,
    val supermarket: String,    
    val tags: List<String> = emptyList(),
    val cheapestAt: String = "Any"
)

data class Exercise(
    val name: String,
    val sets: Int,
    val reps: String,           
    val restSeconds: Int,
    val muscleGroup: String,
    val description: String,
    val modification: String = "",
    val imageResId: String? = null,
    val difficulty: String = "Medium", // Beginner, Medium, Advanced
    val setsHistory: List<String> = emptyList() // To be used for progress tracking
)

data class WorkoutDay(
    val weekNumber: Int,
    val dayNumber: Int,
    val name: String,
    val type: String,           // "Strength", "Cardio", "HIIT", "Recovery"
    val estimatedMinutes: Int,
    val caloriesBurned: Int,
    val exercises: List<Exercise>,
    val focus: String = "Full Body"
)

data class FoodItem(
    val name: String,
    val calories: Int,
    val protein: Float,
    val carbs: Float,
    val fat: Float,
    val unit: String = "100g",
    val supermarket: String = "Any",
    val priceRating: String = "$" 
)

// ─── CONTENT ───────────────────────────────────────────────────────────────────

object AppContent {

    val recipes = listOf(

        // ── BREAKFASTS ──────────────────────────────────────────────────
        Recipe(
            id = "br_01",
            name = "Banana & Honey Oatmeal",
            mealType = "breakfast",
            calories = 340,
            protein = 12f, carbs = 58f, fat = 6f,
            prepMinutes = 5,
            ingredients = listOf("60g oats", "250ml milk", "1 banana", "1 tsp honey"),
            steps = listOf("Cook oats with milk.", "Top with banana slices and honey."),
            supermarket = "Lidl",
            tags = listOf("fast", "budget")
        ),
        Recipe(
            id = "br_02",
            name = "Greek Yogurt Power Bowl",
            mealType = "breakfast",
            calories = 280,
            protein = 22f, carbs = 30f, fat = 8f,
            prepMinutes = 3,
            ingredients = listOf("200g Greek Yogurt", "30g Granola", "Handful of Blueberries"),
            steps = listOf("Place yogurt in a bowl.", "Add granola and fruit."),
            supermarket = "Pingo Doce",
            tags = listOf("high-protein", "no-cook")
        ),
        Recipe(
            id = "br_03",
            name = "Avocado Toast with Egg",
            mealType = "breakfast",
            calories = 380,
            protein = 16f, carbs = 35f, fat = 22f,
            prepMinutes = 8,
            ingredients = listOf("2 slices sourdough", "1/2 Avocado", "1 Poached egg", "Red pepper flakes"),
            steps = listOf("Toast bread.", "Mash avocado on top.", "Place egg and season."),
            supermarket = "Any",
            tags = listOf("healthy-fats", "trending")
        ),
        Recipe(
            id = "br_04",
            name = "Protein Pancakes",
            mealType = "breakfast",
            calories = 420,
            protein = 35f, carbs = 40f, fat = 12f,
            prepMinutes = 15,
            ingredients = listOf("1 scoop Whey Protein", "1 egg", "1/2 cup Oats", "1/4 cup Milk"),
            steps = listOf("Blend all ingredients.", "Cook on a non-stick pan until golden."),
            supermarket = "Any",
            tags = listOf("high-protein", "muscle-build")
        ),
        Recipe(
            id = "br_05",
            name = "Scrambled Tofu Toast",
            mealType = "breakfast",
            calories = 310,
            protein = 20f, carbs = 32f, fat = 14f,
            prepMinutes = 10,
            ingredients = listOf("200g Firm Tofu", "1 tsp Turmeric", "2 slices Whole wheat bread", "Spinach"),
            steps = listOf("Crumble tofu and sauté with turmeric.", "Serve on toasted bread with spinach."),
            supermarket = "Lidl",
            tags = listOf("vegan", "protein")
        ),

        // ── LUNCHES ─────────────────────────────────────────────────────
        Recipe(
            id = "lu_01",
            name = "Turkey & Avocado Baguette",
            mealType = "lunch",
            calories = 450,
            protein = 28f, carbs = 55f, fat = 14f,
            prepMinutes = 10,
            ingredients = listOf("Whole grain baguette", "100g Turkey", "1/2 Avocado", "Lettuce"),
            steps = listOf("Slice baguette.", "Fill with ingredients."),
            supermarket = "Any",
            tags = listOf("portable", "fresh")
        ),
        Recipe(
            id = "lu_02",
            name = "Tuna Pasta Salad",
            mealType = "lunch",
            calories = 520,
            protein = 35f, carbs = 65f, fat = 12f,
            prepMinutes = 15,
            ingredients = listOf("80g Pasta", "1 can Tuna", "Corn", "Cherry tomatoes", "Olive oil"),
            steps = listOf("Boil pasta.", "Mix with tuna and veggies."),
            supermarket = "Continente",
            tags = listOf("prep-friendly")
        ),
        Recipe(
            id = "lu_03",
            name = "Quinoa Harvest Bowl",
            mealType = "lunch",
            calories = 410,
            protein = 18f, carbs = 60f, fat = 12f,
            prepMinutes = 20,
            ingredients = listOf("100g Quinoa", "Roasted sweet potato", "Spinach", "Chickpeas", "Tahini dressing"),
            steps = listOf("Cook quinoa.", "Roast potatoes.", "Combine all and drizzle dressing."),
            supermarket = "Lidl",
            tags = listOf("vegan", "superfood")
        ),
        Recipe(
            id = "lu_04",
            name = "Grilled Chicken Caesar Wrap",
            mealType = "lunch",
            calories = 480,
            protein = 32f, carbs = 42f, fat = 18f,
            prepMinutes = 12,
            ingredients = listOf("1 Tortilla wrap", "120g Grilled chicken", "Romaine lettuce", "Light Caesar dressing"),
            steps = listOf("Toss chicken and lettuce with dressing.", "Wrap tightly and serve."),
            supermarket = "Any",
            tags = listOf("quick", "protein")
        ),
        Recipe(
            id = "lu_05",
            name = "Lentil Soup",
            mealType = "lunch",
            calories = 350,
            protein = 22f, carbs = 50f, fat = 5f,
            prepMinutes = 30,
            ingredients = listOf("1 cup Red lentils", "1 Onion", "2 Carrots", "Vegetable broth"),
            steps = listOf("Sauté onion and carrots.", "Add lentils and broth.", "Simmer for 20 mins."),
            supermarket = "Pingo Doce",
            tags = listOf("healthy", "budget")
        ),
        Recipe(
            id = "lu_06",
            name = "Budget Beef & Cabbage Stir-fry",
            mealType = "lunch",
            calories = 380,
            protein = 28f, carbs = 12f, fat = 22f,
            prepMinutes = 15,
            ingredients = listOf("200g Cabbage", "150g Ground beef", "Soy sauce", "Garlic", "Ginger"),
            steps = listOf("Brown the beef in a pan.", "Add shredded cabbage and spices.", "Cook until cabbage is tender."),
            supermarket = "Lidl",
            tags = listOf("budgetbytes", "low-carb", "cheap")
        ),
        Recipe(
            id = "lu_07",
            name = "Roasted Chickpea Salad",
            mealType = "lunch",
            calories = 410,
            protein = 15f, carbs = 45f, fat = 18f,
            prepMinutes = 10,
            ingredients = listOf("1 can Chickpeas", "Cucumber", "Tomato", "Lemon juice", "Olive oil"),
            steps = listOf("Drain chickpeas.", "Chop veggies and mix everything.", "Season with lemon and salt."),
            supermarket = "Any",
            tags = listOf("vegan", "budget", "no-cook")
        ),
        Recipe(
            id = "lu_08",
            name = "Peanut Chicken Skillet",
            mealType = "lunch",
            calories = 450,
            protein = 35f, carbs = 15f, fat = 25f,
            prepMinutes = 20,
            ingredients = listOf("200g Chicken", "Peanut butter sauce", "Mixed frozen veggies", "Rice (optional)"),
            steps = listOf("Cook chicken.", "Add veggies and sauce.", "Stir until creamy."),
            supermarket = "Lidl",
            tags = listOf("budgetbytes", "high-protein")
        ),

        // ── DINNERS ─────────────────────────────────────────────────────
        Recipe(
            id = "di_01",
            name = "Fit Homemade Pizza",
            mealType = "dinner",
            calories = 580,
            protein = 30f, carbs = 75f, fat = 18f,
            prepMinutes = 20,
            ingredients = listOf("Whole wheat base", "Tomato sauce", "Light Mozzarella", "Veggies"),
            steps = listOf("Assemble pizza.", "Bake at 200°C for 12 mins."),
            supermarket = "Lidl",
            tags = listOf("tasty", "weekend")
        ),
        Recipe(
            id = "di_02",
            name = "Grilled Chicken & Sweet Potato",
            mealType = "dinner",
            calories = 420,
            protein = 40f, carbs = 45f, fat = 8f,
            prepMinutes = 25,
            ingredients = listOf("150g Chicken breast", "200g Sweet potato", "Broccoli"),
            steps = listOf("Grill chicken.", "Boil or roast sweet potato."),
            supermarket = "Any",
            tags = listOf("clean", "muscle-build")
        ),
        Recipe(
            id = "di_03",
            name = "Salmon with Asparagus",
            mealType = "dinner",
            calories = 490,
            protein = 38f, carbs = 10f, fat = 35f,
            prepMinutes = 15,
            ingredients = listOf("200g Salmon fillet", "1 bunch Asparagus", "Lemon", "Garlic butter"),
            steps = listOf("Season salmon.", "Pan-sear salmon and asparagus together."),
            supermarket = "Pingo Doce",
            tags = listOf("low-carb", "omega-3")
        ),
        Recipe(
            id = "di_04",
            name = "Beef & Broccoli Stir-Fry",
            mealType = "dinner",
            calories = 460,
            protein = 35f, carbs = 15f, fat = 28f,
            prepMinutes = 20,
            ingredients = listOf("150g Beef strips", "1 cup Broccoli", "Soy sauce", "Ginger", "Sesame oil"),
            steps = listOf("Sauté beef until brown.", "Add broccoli and sauce.", "Stir-fry until tender."),
            supermarket = "Any",
            tags = listOf("low-carb", "high-protein")
        ),
        Recipe(
            id = "di_05",
            name = "Baked Cod with Potatoes",
            mealType = "dinner",
            calories = 380,
            protein = 30f, carbs = 40f, fat = 10f,
            prepMinutes = 35,
            ingredients = listOf("150g Cod fillet", "2 Potatoes", "Olive oil", "Onion"),
            steps = listOf("Slice potatoes and onion.", "Place cod on top.", "Bake at 180°C for 25 mins."),
            supermarket = "Continente",
            tags = listOf("traditional", "healthy")
        ),
        Recipe(
            id = "di_06",
            name = "Sheet Pan Sausage & Veggies",
            mealType = "dinner",
            calories = 420,
            protein = 25f, carbs = 20f, fat = 30f,
            prepMinutes = 10,
            ingredients = listOf("Turkey Sausage", "Bell peppers", "Zucchini", "Onion", "Olive oil"),
            steps = listOf("Chop everything.", "Toss with oil and spices.", "Bake at 200°C for 25 mins."),
            supermarket = "Any",
            tags = listOf("budgetbytes", "one-pan")
        ),

        // ── SNACKS & SHAKES ───────────────────────────────────
        Recipe(
            id = "sn_01",
            name = "Energy Rice Cakes",
            mealType = "snack",
            calories = 190,
            protein = 4f, carbs = 35f, fat = 5f,
            prepMinutes = 2,
            ingredients = listOf("2 Rice cakes", "1 tbsp Peanut butter", "1/2 Banana"),
            steps = listOf("Spread peanut butter.", "Top with banana."),
            supermarket = "Any",
            tags = listOf("pre-workout", "energy")
        ),
        Recipe(
            id = "sn_02",
            name = "Muscle Recovery Shake",
            mealType = "snack",
            calories = 250,
            protein = 30f, carbs = 25f, fat = 3f,
            prepMinutes = 2,
            ingredients = listOf("1 scoop Whey", "250ml milk", "1 tbsp Oats", "Frozen berries"),
            steps = listOf("Blend all ingredients together."),
            supermarket = "Any",
            tags = listOf("post-workout", "protein")
        ),
        Recipe(
            id = "sn_03",
            name = "Green Detox Smoothie",
            mealType = "snack",
            calories = 150,
            protein = 3f, carbs = 30f, fat = 1f,
            prepMinutes = 5,
            ingredients = listOf("1 cup Spinach", "1 Green apple", "1/2 Cucumber", "Lemon juice"),
            steps = listOf("Blend all ingredients with water."),
            supermarket = "Any",
            tags = listOf("detox", "fresh")
        ),
        Recipe(
            id = "sn_04",
            name = "Almond & Dark Chocolate Bar",
            mealType = "snack",
            calories = 210,
            protein = 6f, carbs = 15f, fat = 14f,
            prepMinutes = 2,
            ingredients = listOf("Handful of Almonds", "2 squares Dark chocolate (85%)"),
            steps = listOf("Enjoy together as a quick snack."),
            supermarket = "Any",
            tags = listOf("healthy-fat", "quick")
        ),
        Recipe(
            id = "sn_05",
            name = "Post-Workout Banana Shake",
            mealType = "snack",
            calories = 320,
            protein = 25f, carbs = 50f, fat = 4f,
            prepMinutes = 3,
            ingredients = listOf("1 scoop Protein powder", "1 large Banana", "300ml Water", "Ice cubes"),
            steps = listOf("Blend until smooth."),
            supermarket = "Any",
            tags = listOf("post-workout", "recovery")
        )
    )

    val foodDatabase = listOf(
        FoodItem("Chicken Breast", 165, 31f, 0f, 3.6f),
        FoodItem("Greek Yogurt", 59, 10f, 3.6f, 0.4f),
        FoodItem("Oat Flakes", 389, 17f, 66f, 7f),
        FoodItem("Banana", 89, 1.1f, 23f, 0.3f),
        FoodItem("Turkey Breast", 104, 17f, 1f, 4f),
        FoodItem("Egg (Large)", 70, 6f, 0.6f, 5f),
        FoodItem("Rice", 130, 2.7f, 28f, 0.3f),
        FoodItem("Peanut Butter", 588, 25f, 20f, 50f),
        FoodItem("Sweet Potato", 86, 1.6f, 20f, 0.1f),
        FoodItem("Cabbage", 25, 1.3f, 6f, 0.1f),
        FoodItem("Ground Beef", 250, 26f, 0f, 15f),
        FoodItem("Chickpeas", 164, 9f, 27f, 2.6f)
    )

    // ─── Helpers to keep the plan compact ──────────────────────────────────────
    private fun warmUpTreadmill(min: Int = 5) =
        Exercise("Treadmill Warm-Up", 1, "$min min", 0, "Cardio",
            "Fast walk · Incline 5–8% · comfortable pace", difficulty = "Beginner")

    private fun mobility() =
        Exercise("Dynamic Mobility", 1, "5 min", 0, "Mobility",
            "Arm circles ×20 · Torso twists ×20 · BW squats ×15 · Hip rotations ×10/side",
            difficulty = "Beginner")

    private fun cardioFinish(min: Int, label: String = "Incline Walk", incline: String = "8–12%", speed: String = "5–6 km/h") =
        Exercise(label, 1, "$min min", 0, "Cardio",
            "Incline $incline · speed $speed", difficulty = "Beginner")

    private fun coolDown() =
        Exercise("Cool-Down & Stretch", 1, "5 min", 0, "Mobility",
            "Hamstring, quad, chest & shoulder stretches · 20–30 sec each",
            difficulty = "Beginner")

    val workoutPlan: List<WorkoutDay> = listOf(

        // ════════════════════════════════════════════════════════════════════
        // MONTH 1 · FOUNDATION  (Weeks 1–4)
        // Strength 3x/wk · Cardio 3–4x/wk · Core 2x/wk
        // Focus: form, base capacity, progressive overload
        // ════════════════════════════════════════════════════════════════════

        // ───── WEEK 1 ─────────────────────────────────────────────────────
        WorkoutDay(
            weekNumber = 1, dayNumber = 1,
            name = "Push Day",
            type = "Strength", focus = "Chest · Shoulders · Triceps",
            estimatedMinutes = 60, caloriesBurned = 380,
            exercises = listOf(
                warmUpTreadmill(5),
                mobility(),
                Exercise("Chest Press (Machine/DB)", 4, "10–12", 60, "Chest", "Control the eccentric · full ROM", difficulty = "Beginner"),
                Exercise("Shoulder Press", 4, "10", 60, "Shoulders", "Elbows ~45° · brace core", difficulty = "Beginner"),
                Exercise("Tricep Pushdown (Cable)", 3, "12", 45, "Triceps", "Lock elbows at sides", difficulty = "Beginner"),
                cardioFinish(20, "Incline Treadmill Walk", "5–8%", "5–6 km/h"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 1, dayNumber = 2,
            name = "Cardio & Core",
            type = "Cardio", focus = "Endurance + Core",
            estimatedMinutes = 45, caloriesBurned = 350,
            exercises = listOf(
                Exercise("Moderate Cardio (Bike or Treadmill)", 1, "25–30 min", 0, "Cardio", "RPE 5–6 · steady breathing", difficulty = "Beginner"),
                Exercise("Plank", 3, "30 sec", 30, "Core", "Straight line head-to-heels", difficulty = "Beginner"),
                Exercise("Russian Twists", 3, "20 reps", 30, "Obliques", "Controlled rotation · feet light", difficulty = "Beginner"),
                Exercise("Leg Raises", 3, "12", 30, "Lower Abs", "Lower slowly · no swinging", difficulty = "Beginner"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 1, dayNumber = 3,
            name = "Pull Day",
            type = "Strength", focus = "Back · Biceps",
            estimatedMinutes = 55, caloriesBurned = 360,
            exercises = listOf(
                warmUpTreadmill(5),
                mobility(),
                Exercise("Lat Pulldown", 4, "10–12", 60, "Back", "Pull to upper chest · squeeze lats", difficulty = "Beginner"),
                Exercise("Seated Row", 4, "12", 60, "Back", "Chest up · drive elbows back", difficulty = "Beginner"),
                Exercise("Dumbbell Bicep Curls", 3, "12", 45, "Biceps", "No swing · full squeeze", difficulty = "Beginner"),
                Exercise("Hammer Curls", 3, "12", 45, "Biceps/Forearms", "Neutral grip · slow eccentric", difficulty = "Beginner"),
                cardioFinish(12, "Light Cardio", "0–3%", "5 km/h"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 1, dayNumber = 4,
            name = "Cardio & Core",
            type = "Cardio", focus = "Endurance + Obliques",
            estimatedMinutes = 45, caloriesBurned = 340,
            exercises = listOf(
                Exercise("Treadmill Incline Walk", 1, "30 min", 0, "Cardio", "Incline 6–10% · steady", difficulty = "Beginner"),
                Exercise("Side Plank", 3, "20 sec/side", 30, "Obliques", "Stack hips · push floor away", difficulty = "Beginner"),
                Exercise("Bicycle Crunches", 3, "20", 30, "Core", "Elbow to opposite knee · slow", difficulty = "Beginner"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 1, dayNumber = 5,
            name = "Leg Day",
            type = "Strength", focus = "Quads · Hamstrings · Calves",
            estimatedMinutes = 60, caloriesBurned = 420,
            exercises = listOf(
                warmUpTreadmill(5),
                mobility(),
                Exercise("Leg Press", 4, "12", 75, "Quads", "Knees track toes · don't lock", difficulty = "Beginner"),
                Exercise("Hamstring Curl Machine", 4, "12", 60, "Hamstrings", "Pause at peak contraction", difficulty = "Beginner"),
                Exercise("Calf Raises", 4, "15", 45, "Calves", "Full stretch + full squeeze", difficulty = "Beginner"),
                Exercise("Plank", 2, "30 sec", 30, "Core", "Mini core circuit", difficulty = "Beginner"),
                Exercise("Russian Twists", 2, "20", 30, "Obliques", "Mini core circuit", difficulty = "Beginner"),
                cardioFinish(12, "Cardio Finisher", "3–5%", "5 km/h"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 1, dayNumber = 6,
            name = "Active Recovery",
            type = "Recovery", focus = "Mobility · Light Movement",
            estimatedMinutes = 25, caloriesBurned = 120,
            exercises = listOf(
                Exercise("Easy Walk", 1, "20–30 min", 0, "Cardio", "Conversational pace · outdoors if possible", difficulty = "Beginner"),
                Exercise("Full-Body Stretch", 1, "10 min", 0, "Mobility", "Hold each stretch 30–45 sec", difficulty = "Beginner")
            )
        ),

        // ───── WEEK 2  (progression: +1–2 reps · +5 min cardio · +1 core round) ─────
        WorkoutDay(
            weekNumber = 2, dayNumber = 1,
            name = "Push Day",
            type = "Strength", focus = "Chest · Shoulders · Triceps",
            estimatedMinutes = 65, caloriesBurned = 410,
            exercises = listOf(
                warmUpTreadmill(5),
                mobility(),
                Exercise("Chest Press (Machine/DB)", 4, "12–14", 60, "Chest", "Slight load bump from W1", difficulty = "Beginner"),
                Exercise("Shoulder Press", 4, "11–12", 60, "Shoulders", "Slow 2-sec eccentric", difficulty = "Beginner"),
                Exercise("Tricep Pushdown (Cable)", 3, "13–14", 45, "Triceps", "Pause 1 sec at bottom", difficulty = "Beginner"),
                cardioFinish(25, "Incline Treadmill Walk", "5–8%", "5–6 km/h"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 2, dayNumber = 2,
            name = "Cardio & Core",
            type = "Cardio", focus = "Endurance + Core",
            estimatedMinutes = 50, caloriesBurned = 390,
            exercises = listOf(
                Exercise("Moderate Cardio", 1, "30–35 min", 0, "Cardio", "Add 5 min from W1", difficulty = "Beginner"),
                Exercise("Plank", 4, "30–35 sec", 30, "Core", "Extra round", difficulty = "Beginner"),
                Exercise("Russian Twists", 4, "22", 30, "Obliques", "Extra round", difficulty = "Beginner"),
                Exercise("Leg Raises", 4, "13", 30, "Lower Abs", "Extra round", difficulty = "Beginner"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 2, dayNumber = 3,
            name = "Pull Day",
            type = "Strength", focus = "Back · Biceps",
            estimatedMinutes = 60, caloriesBurned = 390,
            exercises = listOf(
                warmUpTreadmill(5),
                mobility(),
                Exercise("Lat Pulldown", 4, "12–13", 60, "Back", "+1–2 reps vs W1", difficulty = "Beginner"),
                Exercise("Seated Row", 4, "13", 60, "Back", "+1 rep vs W1", difficulty = "Beginner"),
                Exercise("Dumbbell Bicep Curls", 3, "13", 45, "Biceps", "+1 rep vs W1", difficulty = "Beginner"),
                Exercise("Hammer Curls", 3, "13", 45, "Biceps/Forearms", "+1 rep vs W1", difficulty = "Beginner"),
                cardioFinish(15, "Light Cardio", "0–3%", "5 km/h"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 2, dayNumber = 4,
            name = "Cardio & Core",
            type = "Cardio", focus = "Endurance + Obliques",
            estimatedMinutes = 50, caloriesBurned = 370,
            exercises = listOf(
                Exercise("Treadmill Incline Walk", 1, "35 min", 0, "Cardio", "+5 min vs W1", difficulty = "Beginner"),
                Exercise("Side Plank", 4, "22 sec/side", 30, "Obliques", "Extra round", difficulty = "Beginner"),
                Exercise("Bicycle Crunches", 4, "22", 30, "Core", "Extra round", difficulty = "Beginner"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 2, dayNumber = 5,
            name = "Leg Day",
            type = "Strength", focus = "Quads · Hamstrings · Calves",
            estimatedMinutes = 65, caloriesBurned = 450,
            exercises = listOf(
                warmUpTreadmill(5),
                mobility(),
                Exercise("Leg Press", 4, "13–14", 75, "Quads", "+1–2 reps vs W1", difficulty = "Beginner"),
                Exercise("Hamstring Curl Machine", 4, "13", 60, "Hamstrings", "+1 rep vs W1", difficulty = "Beginner"),
                Exercise("Calf Raises", 4, "17", 45, "Calves", "+2 reps vs W1", difficulty = "Beginner"),
                Exercise("Plank", 3, "35 sec", 30, "Core", "Extra round", difficulty = "Beginner"),
                Exercise("Russian Twists", 3, "22", 30, "Obliques", "Extra round", difficulty = "Beginner"),
                cardioFinish(15, "Cardio Finisher", "3–5%", "5 km/h"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 2, dayNumber = 6,
            name = "Active Recovery",
            type = "Recovery", focus = "Mobility · Light Movement",
            estimatedMinutes = 30, caloriesBurned = 130,
            exercises = listOf(
                Exercise("Easy Walk", 1, "25–35 min", 0, "Cardio", "Conversational pace", difficulty = "Beginner"),
                Exercise("Full-Body Stretch", 1, "10 min", 0, "Mobility", "Hold 30–45 sec each", difficulty = "Beginner")
            )
        ),

        // ───── WEEK 3  (+5–10% weight · +5 min cardio · advanced core variations) ─────
        WorkoutDay(
            weekNumber = 3, dayNumber = 1,
            name = "Push Day",
            type = "Strength", focus = "Chest · Shoulders · Triceps",
            estimatedMinutes = 70, caloriesBurned = 440,
            exercises = listOf(
                warmUpTreadmill(5),
                mobility(),
                Exercise("Chest Press (Machine/DB)", 4, "10–12", 60, "Chest", "+5–10% load · same reps", difficulty = "Medium"),
                Exercise("Shoulder Press", 4, "10", 60, "Shoulders", "+5–10% load", difficulty = "Medium"),
                Exercise("Tricep Pushdown (Cable)", 3, "12", 45, "Triceps", "+5–10% load", difficulty = "Medium"),
                cardioFinish(30, "Incline Treadmill Walk", "6–10%", "5–6 km/h"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 3, dayNumber = 2,
            name = "Cardio & Core",
            type = "Cardio", focus = "Endurance + Advanced Core",
            estimatedMinutes = 55, caloriesBurned = 420,
            exercises = listOf(
                Exercise("Moderate Cardio", 1, "35–40 min", 0, "Cardio", "+5 min vs W2", difficulty = "Medium"),
                Exercise("Plank with Shoulder Tap", 4, "40 sec", 30, "Core", "Tap opposite shoulder · keep hips still", difficulty = "Medium"),
                Exercise("Weighted Russian Twists", 4, "22", 30, "Obliques", "Add light DB / plate", difficulty = "Medium"),
                Exercise("Hanging Knee Raises", 4, "12", 45, "Lower Abs", "Slow & controlled", difficulty = "Medium"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 3, dayNumber = 3,
            name = "Pull Day",
            type = "Strength", focus = "Back · Biceps",
            estimatedMinutes = 65, caloriesBurned = 420,
            exercises = listOf(
                warmUpTreadmill(5),
                mobility(),
                Exercise("Lat Pulldown", 4, "10–12", 60, "Back", "+5–10% load", difficulty = "Medium"),
                Exercise("Seated Row", 4, "12", 60, "Back", "+5–10% load", difficulty = "Medium"),
                Exercise("Dumbbell Bicep Curls", 3, "12", 45, "Biceps", "+5–10% load", difficulty = "Medium"),
                Exercise("Hammer Curls", 3, "12", 45, "Biceps/Forearms", "+5–10% load", difficulty = "Medium"),
                cardioFinish(18, "Light Cardio", "0–3%", "5 km/h"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 3, dayNumber = 4,
            name = "Cardio & Core",
            type = "Cardio", focus = "Endurance + Rotational Core",
            estimatedMinutes = 55, caloriesBurned = 400,
            exercises = listOf(
                Exercise("Treadmill Incline Walk", 1, "40 min", 0, "Cardio", "+5 min vs W2", difficulty = "Medium"),
                Exercise("Side Plank with Rotation", 4, "10/side", 30, "Obliques", "Thread arm under torso · slow", difficulty = "Medium"),
                Exercise("Bicycle Crunches", 4, "24", 30, "Core", "Pause when elbow meets knee", difficulty = "Medium"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 3, dayNumber = 5,
            name = "Leg Day",
            type = "Strength", focus = "Quads · Hamstrings · Calves",
            estimatedMinutes = 70, caloriesBurned = 480,
            exercises = listOf(
                warmUpTreadmill(5),
                mobility(),
                Exercise("Leg Press", 4, "12", 75, "Quads", "+5–10% load", difficulty = "Medium"),
                Exercise("Hamstring Curl Machine", 4, "12", 60, "Hamstrings", "+5–10% load", difficulty = "Medium"),
                Exercise("Calf Raises", 4, "15", 45, "Calves", "+5–10% load · pause at top", difficulty = "Medium"),
                Exercise("Plank with Shoulder Tap", 3, "40 sec", 30, "Core", "Advanced variation", difficulty = "Medium"),
                Exercise("Weighted Russian Twists", 3, "22", 30, "Obliques", "Light DB / plate", difficulty = "Medium"),
                cardioFinish(18, "Cardio Finisher", "3–5%", "5 km/h"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 3, dayNumber = 6,
            name = "Active Recovery",
            type = "Recovery", focus = "Mobility · Light Movement",
            estimatedMinutes = 35, caloriesBurned = 150,
            exercises = listOf(
                Exercise("Easy Walk", 1, "30 min", 0, "Cardio", "Conversational pace", difficulty = "Beginner"),
                Exercise("Yoga / Mobility Flow", 1, "10–15 min", 0, "Mobility", "Hip openers · thoracic rotation", difficulty = "Beginner")
            )
        ),

        // ───── WEEK 4  (PEAK · +5–10% weight · −10–15s rest · intervals · 4 core sets) ─
        WorkoutDay(
            weekNumber = 4, dayNumber = 1,
            name = "Push Day · Peak",
            type = "Strength", focus = "Chest · Shoulders · Triceps",
            estimatedMinutes = 70, caloriesBurned = 470,
            exercises = listOf(
                warmUpTreadmill(5),
                mobility(),
                Exercise("Chest Press (Machine/DB)", 4, "10–12", 45, "Chest", "+5–10% · shorter rest", difficulty = "Medium"),
                Exercise("Shoulder Press", 4, "10", 45, "Shoulders", "+5–10% · shorter rest", difficulty = "Medium"),
                Exercise("Tricep Pushdown (Cable)", 4, "12", 30, "Triceps", "+5–10% · shorter rest", difficulty = "Medium"),
                Exercise("Treadmill Intervals", 1, "15 min", 0, "Cardio", "1 min fast · 2 min easy · repeat", difficulty = "Medium"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 4, dayNumber = 2,
            name = "Cardio & Core · Peak",
            type = "Cardio", focus = "Intervals + Advanced Core",
            estimatedMinutes = 60, caloriesBurned = 470,
            exercises = listOf(
                Exercise("Bike or Treadmill Intervals", 1, "30 min", 0, "Cardio", "1 min hard · 2 min easy · 10 rounds", difficulty = "Medium"),
                Exercise("Hanging Leg Raises", 4, "10–12", 45, "Lower Abs", "Slow · no swing", difficulty = "Advanced"),
                Exercise("Side Plank with Reach-Through", 4, "10/side", 30, "Obliques", "Thread arm under torso", difficulty = "Advanced"),
                Exercise("Weighted Russian Twists", 4, "24", 30, "Obliques", "Heaviest plate so far", difficulty = "Medium"),
                Exercise("Plank", 4, "45–60 sec", 30, "Core", "Hold tight · no sagging", difficulty = "Medium"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 4, dayNumber = 3,
            name = "Pull Day · Peak",
            type = "Strength", focus = "Back · Biceps",
            estimatedMinutes = 65, caloriesBurned = 450,
            exercises = listOf(
                warmUpTreadmill(5),
                mobility(),
                Exercise("Lat Pulldown", 4, "10–12", 45, "Back", "+5–10% · shorter rest", difficulty = "Medium"),
                Exercise("Seated Row", 4, "12", 45, "Back", "+5–10% · shorter rest", difficulty = "Medium"),
                Exercise("Dumbbell Bicep Curls", 4, "12", 30, "Biceps", "+5–10% · 4 sets", difficulty = "Medium"),
                Exercise("Hammer Curls", 4, "12", 30, "Biceps/Forearms", "+5–10% · 4 sets", difficulty = "Medium"),
                Exercise("Treadmill Intervals", 1, "12 min", 0, "Cardio", "1 min fast · 2 min easy", difficulty = "Medium"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 4, dayNumber = 4,
            name = "Cardio & Core · Peak",
            type = "Cardio", focus = "Intervals + Obliques",
            estimatedMinutes = 55, caloriesBurned = 430,
            exercises = listOf(
                Exercise("Treadmill Hill Intervals", 1, "30 min", 0, "Cardio", "2 min flat · 2 min steep (8–10%) · repeat", difficulty = "Medium"),
                Exercise("Hanging Leg Raises", 4, "10", 45, "Lower Abs", "Slow lower · pause at bottom", difficulty = "Advanced"),
                Exercise("Side Plank with Reach-Through", 4, "12/side", 30, "Obliques", "Thread under · rotate up", difficulty = "Advanced"),
                Exercise("Bicycle Crunches", 4, "26", 30, "Core", "Slow & deliberate", difficulty = "Medium"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 4, dayNumber = 5,
            name = "Leg Day · Peak",
            type = "Strength", focus = "Quads · Hamstrings · Calves",
            estimatedMinutes = 75, caloriesBurned = 520,
            exercises = listOf(
                warmUpTreadmill(5),
                mobility(),
                Exercise("Leg Press", 4, "12", 60, "Quads", "+5–10% · shorter rest", difficulty = "Medium"),
                Exercise("Hamstring Curl Machine", 4, "12", 45, "Hamstrings", "+5–10% · shorter rest", difficulty = "Medium"),
                Exercise("Calf Raises", 4, "15", 30, "Calves", "+5–10% · pause + squeeze", difficulty = "Medium"),
                Exercise("Hanging Leg Raises", 4, "10", 45, "Lower Abs", "Advanced core finisher", difficulty = "Advanced"),
                Exercise("Side Plank with Reach-Through", 4, "10/side", 30, "Obliques", "Advanced core finisher", difficulty = "Advanced"),
                Exercise("Treadmill Intervals", 1, "12 min", 0, "Cardio", "1 min fast · 2 min easy", difficulty = "Medium"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 4, dayNumber = 6,
            name = "Active Recovery",
            type = "Recovery", focus = "Mobility · Reset",
            estimatedMinutes = 35, caloriesBurned = 150,
            exercises = listOf(
                Exercise("Easy Walk", 1, "30 min", 0, "Cardio", "Conversational · prep for Phase 2", difficulty = "Beginner"),
                Exercise("Full-Body Mobility Flow", 1, "15 min", 0, "Mobility", "Reset before Month 2", difficulty = "Beginner")
            )
        ),

        // ════════════════════════════════════════════════════════════════════
        // MONTH 2 · PROGRESSION  (Weeks 5–8)
        // Split routine 4x/wk: Push · Pull · Legs · Core
        // Cardio: add interval work · Core: more sets / difficulty
        // ════════════════════════════════════════════════════════════════════

        // ───── WEEK 5  (intro the split) ─────────────────────────────────
        WorkoutDay(
            weekNumber = 5, dayNumber = 1,
            name = "Push (Chest · Shoulders · Triceps)",
            type = "Strength", focus = "Push Split",
            estimatedMinutes = 65, caloriesBurned = 440,
            exercises = listOf(
                warmUpTreadmill(5), mobility(),
                Exercise("DB Bench Press", 4, "10", 60, "Chest", "Lower under control · drive up", difficulty = "Medium"),
                Exercise("Incline DB Press", 3, "10–12", 60, "Upper Chest", "30–45° bench", difficulty = "Medium"),
                Exercise("Seated Shoulder Press", 4, "10", 60, "Shoulders", "Press straight overhead", difficulty = "Medium"),
                Exercise("Lateral Raises", 3, "12", 45, "Side Delts", "Lead with elbows", difficulty = "Medium"),
                Exercise("Tricep Pushdown", 3, "12", 45, "Triceps", "Lock elbows", difficulty = "Medium"),
                Exercise("Overhead Tricep Extension", 3, "12", 45, "Triceps", "Full stretch", difficulty = "Medium"),
                cardioFinish(10, "Treadmill Intervals", "0–3%", "1 min fast / 1 min easy"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 5, dayNumber = 2,
            name = "Pull (Back · Biceps)",
            type = "Strength", focus = "Pull Split",
            estimatedMinutes = 65, caloriesBurned = 430,
            exercises = listOf(
                warmUpTreadmill(5), mobility(),
                Exercise("Lat Pulldown", 4, "10", 60, "Back", "Squeeze lats · slow eccentric", difficulty = "Medium"),
                Exercise("Bent-Over DB Row", 4, "10", 60, "Back", "Flat back · pull to hip", difficulty = "Medium"),
                Exercise("Seated Cable Row", 3, "12", 60, "Back", "Chest up · drive elbows", difficulty = "Medium"),
                Exercise("Face Pulls", 3, "15", 45, "Rear Delts", "External rotation at end", difficulty = "Medium"),
                Exercise("DB Bicep Curls", 3, "12", 45, "Biceps", "No swing", difficulty = "Medium"),
                Exercise("Hammer Curls", 3, "12", 45, "Biceps/Forearms", "Neutral grip", difficulty = "Medium"),
                cardioFinish(10, "Bike Intervals", "—", "30 sec hard / 60 sec easy"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 5, dayNumber = 3,
            name = "Legs (Quads · Hamstrings · Calves)",
            type = "Strength", focus = "Legs Split",
            estimatedMinutes = 70, caloriesBurned = 500,
            exercises = listOf(
                warmUpTreadmill(5), mobility(),
                Exercise("Goblet Squat", 4, "12", 75, "Quads", "Chest tall · hips back & down", difficulty = "Medium"),
                Exercise("Romanian Deadlift (DB)", 4, "10", 75, "Hamstrings/Glutes", "Soft knees · push hips back", difficulty = "Medium"),
                Exercise("Walking Lunges", 3, "10/leg", 60, "Quads/Glutes", "Knee tracks toe · vertical torso", difficulty = "Medium"),
                Exercise("Leg Press", 3, "12", 60, "Quads", "Full ROM · don't lock", difficulty = "Medium"),
                Exercise("Hamstring Curl Machine", 3, "12", 45, "Hamstrings", "Pause at peak", difficulty = "Medium"),
                Exercise("Calf Raises", 4, "15", 45, "Calves", "Pause + squeeze", difficulty = "Medium"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 5, dayNumber = 4,
            name = "Core & Conditioning",
            type = "Cardio", focus = "Core Split + Intervals",
            estimatedMinutes = 50, caloriesBurned = 410,
            exercises = listOf(
                Exercise("Treadmill Intervals", 1, "20 min", 0, "Cardio", "1 min fast · 1 min easy · 10 rounds", difficulty = "Medium"),
                Exercise("Hanging Leg Raises", 4, "12", 45, "Lower Abs", "Slow lower", difficulty = "Advanced"),
                Exercise("Cable Woodchopper", 4, "12/side", 45, "Obliques", "Rotate through hips", difficulty = "Medium"),
                Exercise("Weighted Russian Twists", 4, "24", 30, "Obliques", "Heavier plate", difficulty = "Medium"),
                Exercise("Plank", 4, "60 sec", 30, "Core", "Brace HARD", difficulty = "Medium"),
                coolDown()
            )
        ),

        // ───── WEEK 6  (volume bump on split) ───────────────────────────────
        WorkoutDay(
            weekNumber = 6, dayNumber = 1,
            name = "Push (Chest · Shoulders · Triceps)",
            type = "Strength", focus = "Push Split · +volume",
            estimatedMinutes = 70, caloriesBurned = 470,
            exercises = listOf(
                warmUpTreadmill(5), mobility(),
                Exercise("DB Bench Press", 4, "10–12", 60, "Chest", "+1–2 reps vs W5", difficulty = "Medium"),
                Exercise("Incline DB Press", 4, "10–12", 60, "Upper Chest", "Extra set vs W5", difficulty = "Medium"),
                Exercise("Seated Shoulder Press", 4, "10–12", 60, "Shoulders", "+1–2 reps", difficulty = "Medium"),
                Exercise("Lateral Raises", 4, "12", 45, "Side Delts", "Extra set", difficulty = "Medium"),
                Exercise("Tricep Pushdown", 4, "12", 45, "Triceps", "Extra set", difficulty = "Medium"),
                Exercise("Overhead Tricep Extension", 3, "13", 45, "Triceps", "+1 rep", difficulty = "Medium"),
                cardioFinish(12, "Treadmill Intervals", "0–3%", "1 min fast / 1 min easy"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 6, dayNumber = 2,
            name = "Pull (Back · Biceps)",
            type = "Strength", focus = "Pull Split · +volume",
            estimatedMinutes = 70, caloriesBurned = 460,
            exercises = listOf(
                warmUpTreadmill(5), mobility(),
                Exercise("Lat Pulldown", 4, "10–12", 60, "Back", "+1–2 reps", difficulty = "Medium"),
                Exercise("Bent-Over DB Row", 4, "10–12", 60, "Back", "+1–2 reps", difficulty = "Medium"),
                Exercise("Seated Cable Row", 4, "12", 60, "Back", "Extra set", difficulty = "Medium"),
                Exercise("Face Pulls", 4, "15", 45, "Rear Delts", "Extra set", difficulty = "Medium"),
                Exercise("DB Bicep Curls", 4, "12", 45, "Biceps", "Extra set", difficulty = "Medium"),
                Exercise("Hammer Curls", 4, "12", 45, "Biceps/Forearms", "Extra set", difficulty = "Medium"),
                cardioFinish(12, "Bike Intervals", "—", "30 sec hard / 60 sec easy"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 6, dayNumber = 3,
            name = "Legs (Quads · Hamstrings · Calves)",
            type = "Strength", focus = "Legs Split · +volume",
            estimatedMinutes = 75, caloriesBurned = 530,
            exercises = listOf(
                warmUpTreadmill(5), mobility(),
                Exercise("Goblet Squat", 4, "12–14", 75, "Quads", "+1–2 reps", difficulty = "Medium"),
                Exercise("Romanian Deadlift (DB)", 4, "10–12", 75, "Hamstrings/Glutes", "+1–2 reps", difficulty = "Medium"),
                Exercise("Walking Lunges", 4, "10/leg", 60, "Quads/Glutes", "Extra set", difficulty = "Medium"),
                Exercise("Leg Press", 4, "12", 60, "Quads", "Extra set", difficulty = "Medium"),
                Exercise("Hamstring Curl Machine", 4, "12", 45, "Hamstrings", "Extra set", difficulty = "Medium"),
                Exercise("Calf Raises", 4, "17", 45, "Calves", "+2 reps", difficulty = "Medium"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 6, dayNumber = 4,
            name = "Core & Conditioning",
            type = "Cardio", focus = "Core Split + Intervals",
            estimatedMinutes = 55, caloriesBurned = 440,
            exercises = listOf(
                Exercise("Treadmill Intervals", 1, "25 min", 0, "Cardio", "+5 min vs W5", difficulty = "Medium"),
                Exercise("Hanging Leg Raises", 4, "13", 45, "Lower Abs", "+1 rep", difficulty = "Advanced"),
                Exercise("Cable Woodchopper", 4, "13/side", 45, "Obliques", "+1 rep", difficulty = "Medium"),
                Exercise("Weighted Russian Twists", 4, "26", 30, "Obliques", "+2 reps", difficulty = "Medium"),
                Exercise("Plank", 4, "70 sec", 30, "Core", "+10 sec vs W5", difficulty = "Medium"),
                coolDown()
            )
        ),

        // ───── WEEK 7  (intensity bump · +5–10% load) ────────────────────────
        WorkoutDay(
            weekNumber = 7, dayNumber = 1,
            name = "Push · Heavier",
            type = "Strength", focus = "Push Split · +load",
            estimatedMinutes = 70, caloriesBurned = 490,
            exercises = listOf(
                warmUpTreadmill(5), mobility(),
                Exercise("DB Bench Press", 4, "8–10", 75, "Chest", "+5–10% load · longer rest", difficulty = "Advanced"),
                Exercise("Incline DB Press", 4, "10", 60, "Upper Chest", "+5–10% load", difficulty = "Advanced"),
                Exercise("Seated Shoulder Press", 4, "8–10", 75, "Shoulders", "+5–10% load", difficulty = "Advanced"),
                Exercise("Lateral Raises", 4, "12", 45, "Side Delts", "Strict form", difficulty = "Medium"),
                Exercise("Tricep Pushdown", 4, "10", 45, "Triceps", "+5–10% load", difficulty = "Medium"),
                Exercise("Overhead Tricep Extension", 3, "12", 45, "Triceps", "Maintain ROM", difficulty = "Medium"),
                cardioFinish(12, "Treadmill Intervals", "0–3%", "30 sec sprint / 60 sec easy"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 7, dayNumber = 2,
            name = "Pull · Heavier",
            type = "Strength", focus = "Pull Split · +load",
            estimatedMinutes = 70, caloriesBurned = 480,
            exercises = listOf(
                warmUpTreadmill(5), mobility(),
                Exercise("Lat Pulldown", 4, "8–10", 75, "Back", "+5–10% load", difficulty = "Advanced"),
                Exercise("Bent-Over DB Row", 4, "8–10", 75, "Back", "+5–10% load", difficulty = "Advanced"),
                Exercise("Seated Cable Row", 4, "10", 60, "Back", "+5–10% load", difficulty = "Medium"),
                Exercise("Face Pulls", 4, "15", 45, "Rear Delts", "Form first", difficulty = "Medium"),
                Exercise("DB Bicep Curls", 4, "10", 45, "Biceps", "+5–10% load", difficulty = "Medium"),
                Exercise("Hammer Curls", 4, "10", 45, "Biceps/Forearms", "+5–10% load", difficulty = "Medium"),
                cardioFinish(12, "Bike Intervals", "—", "30 sec hard / 60 sec easy"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 7, dayNumber = 3,
            name = "Legs · Heavier",
            type = "Strength", focus = "Legs Split · +load",
            estimatedMinutes = 75, caloriesBurned = 560,
            exercises = listOf(
                warmUpTreadmill(5), mobility(),
                Exercise("Goblet Squat", 4, "10", 90, "Quads", "+5–10% load · longer rest", difficulty = "Advanced"),
                Exercise("Romanian Deadlift (DB)", 4, "8–10", 90, "Hamstrings/Glutes", "+5–10% load", difficulty = "Advanced"),
                Exercise("Walking Lunges", 4, "10/leg", 60, "Quads/Glutes", "Hold DBs heavier", difficulty = "Advanced"),
                Exercise("Leg Press", 4, "10", 75, "Quads", "+5–10% load", difficulty = "Advanced"),
                Exercise("Hamstring Curl Machine", 4, "10", 60, "Hamstrings", "+5–10% load", difficulty = "Medium"),
                Exercise("Calf Raises", 4, "15", 45, "Calves", "+5–10% load", difficulty = "Medium"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 7, dayNumber = 4,
            name = "Core & Conditioning · Harder",
            type = "Cardio", focus = "Tougher Intervals + Core",
            estimatedMinutes = 55, caloriesBurned = 460,
            exercises = listOf(
                Exercise("Treadmill Hill Intervals", 1, "25 min", 0, "Cardio", "2 min flat · 2 min steep (10–12%)", difficulty = "Medium"),
                Exercise("Hanging Leg Raises", 4, "12–15", 45, "Lower Abs", "Add ankle weight if easy", difficulty = "Advanced"),
                Exercise("Cable Woodchopper", 4, "12/side", 45, "Obliques", "+5–10% load", difficulty = "Medium"),
                Exercise("Weighted Plank", 4, "45 sec", 30, "Core", "Plate on back", difficulty = "Advanced"),
                Exercise("V-Ups", 4, "12", 30, "Core", "Reach hands to feet", difficulty = "Advanced"),
                coolDown()
            )
        ),

        // ───── WEEK 8  (Month 2 peak · less rest · finisher day) ─────────────
        WorkoutDay(
            weekNumber = 8, dayNumber = 1,
            name = "Push · Peak",
            type = "Strength", focus = "Push Split · density",
            estimatedMinutes = 70, caloriesBurned = 510,
            exercises = listOf(
                warmUpTreadmill(5), mobility(),
                Exercise("DB Bench Press", 4, "8–10", 45, "Chest", "Same load · shorter rest", difficulty = "Advanced"),
                Exercise("Incline DB Press", 4, "10", 45, "Upper Chest", "Shorter rest", difficulty = "Advanced"),
                Exercise("Seated Shoulder Press", 4, "10", 45, "Shoulders", "Shorter rest", difficulty = "Advanced"),
                Exercise("Lateral Raises", 4, "12–15", 30, "Side Delts", "Burnout last set", difficulty = "Medium"),
                Exercise("Tricep Superset (Pushdown + Overhead Ext)", 4, "10+10", 30, "Triceps", "Back-to-back · no rest between exercises", difficulty = "Advanced"),
                cardioFinish(15, "Treadmill Intervals", "0–3%", "30 sec sprint / 60 sec easy"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 8, dayNumber = 2,
            name = "Pull · Peak",
            type = "Strength", focus = "Pull Split · density",
            estimatedMinutes = 70, caloriesBurned = 500,
            exercises = listOf(
                warmUpTreadmill(5), mobility(),
                Exercise("Lat Pulldown", 4, "8–10", 45, "Back", "Shorter rest", difficulty = "Advanced"),
                Exercise("Bent-Over DB Row", 4, "10", 45, "Back", "Shorter rest", difficulty = "Advanced"),
                Exercise("Seated Cable Row", 4, "10–12", 45, "Back", "Shorter rest", difficulty = "Medium"),
                Exercise("Face Pulls", 4, "15", 30, "Rear Delts", "Shorter rest", difficulty = "Medium"),
                Exercise("Biceps Superset (DB Curl + Hammer)", 4, "10+10", 30, "Biceps", "Back-to-back", difficulty = "Advanced"),
                cardioFinish(15, "Bike Intervals", "—", "30 sec hard / 60 sec easy"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 8, dayNumber = 3,
            name = "Legs · Peak",
            type = "Strength", focus = "Legs Split · density",
            estimatedMinutes = 80, caloriesBurned = 590,
            exercises = listOf(
                warmUpTreadmill(5), mobility(),
                Exercise("Goblet Squat", 4, "10", 75, "Quads", "Same load · drop rest 15s", difficulty = "Advanced"),
                Exercise("Romanian Deadlift (DB)", 4, "10", 75, "Hamstrings/Glutes", "Shorter rest", difficulty = "Advanced"),
                Exercise("Walking Lunges", 4, "12/leg", 60, "Quads/Glutes", "+2 reps/leg", difficulty = "Advanced"),
                Exercise("Leg Press", 4, "12", 60, "Quads", "Pump finisher · don't lock", difficulty = "Medium"),
                Exercise("Hamstring Curl Machine", 4, "12", 45, "Hamstrings", "Squeeze hard", difficulty = "Medium"),
                Exercise("Calf Raise Burnout", 4, "20", 30, "Calves", "High reps to finish", difficulty = "Medium"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 8, dayNumber = 4,
            name = "Core & Conditioning · Peak",
            type = "HIIT", focus = "HIIT + Advanced Core",
            estimatedMinutes = 55, caloriesBurned = 500,
            exercises = listOf(
                Exercise("Treadmill HIIT", 1, "20 min", 0, "Cardio", "30 sec sprint / 60 sec easy · 10 rounds", difficulty = "Advanced"),
                Exercise("Hanging Leg Raises", 4, "15", 45, "Lower Abs", "Strict · no swing", difficulty = "Advanced"),
                Exercise("Cable Woodchopper", 4, "12/side", 30, "Obliques", "Shorter rest", difficulty = "Medium"),
                Exercise("V-Ups", 4, "15", 30, "Core", "+3 reps vs W7", difficulty = "Advanced"),
                Exercise("Plank with Shoulder Tap", 4, "60 sec", 30, "Core", "Hips dead-still", difficulty = "Advanced"),
                Exercise("Mountain Climbers Finisher", 3, "45 sec", 30, "Core/Cardio", "Fast cadence", difficulty = "Medium"),
                coolDown()
            )
        ),

        // ════════════════════════════════════════════════════════════════════
        // MONTH 3 · DEFINITION  (Weeks 9–12)
        // 4x strength · steady-state + HIIT cardio · advanced core
        // ════════════════════════════════════════════════════════════════════

        // ───── WEEK 9 ────────────────────────────────────────────────────────
        WorkoutDay(
            weekNumber = 9, dayNumber = 1,
            name = "Upper Push · Definition",
            type = "Strength", focus = "Chest · Shoulders · Triceps",
            estimatedMinutes = 70, caloriesBurned = 510,
            exercises = listOf(
                warmUpTreadmill(5), mobility(),
                Exercise("DB Bench Press", 4, "8", 75, "Chest", "Top-set heaviest", difficulty = "Advanced"),
                Exercise("Incline DB Press", 4, "10", 60, "Upper Chest", "Tempo 3-1-1", difficulty = "Advanced"),
                Exercise("Arnold Press", 3, "10–12", 60, "Shoulders", "Rotate palms", difficulty = "Advanced"),
                Exercise("Lateral Raise Drop Set", 3, "10 → 10", 60, "Side Delts", "Heavy 10 → drop ~30% → 10", difficulty = "Advanced"),
                Exercise("Tricep Pushdown", 4, "12", 30, "Triceps", "Short rest · pump", difficulty = "Medium"),
                Exercise("Overhead Tricep Extension", 3, "12", 30, "Triceps", "Full stretch", difficulty = "Medium"),
                Exercise("Steady-State Incline Walk", 1, "15 min", 0, "Cardio", "Incline 8–12% · RPE 6", difficulty = "Medium"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 9, dayNumber = 2,
            name = "HIIT + Core",
            type = "HIIT", focus = "Conditioning · Definition",
            estimatedMinutes = 50, caloriesBurned = 490,
            exercises = listOf(
                Exercise("Bike or Treadmill HIIT", 1, "20 min", 0, "Cardio", "20 sec all-out / 40 sec easy · 20 rounds", difficulty = "Advanced"),
                Exercise("Hanging Leg Raises", 4, "12–15", 45, "Lower Abs", "Slow tempo", difficulty = "Advanced"),
                Exercise("Cable Woodchopper", 4, "12/side", 45, "Obliques", "Heavier · controlled", difficulty = "Advanced"),
                Exercise("Dragon Flag (or Negatives)", 3, "5–8", 60, "Core", "Lower under control", difficulty = "Advanced"),
                Exercise("Side Plank with Reach-Through", 4, "12/side", 30, "Obliques", "Slow & strict", difficulty = "Advanced"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 9, dayNumber = 3,
            name = "Upper Pull · Definition",
            type = "Strength", focus = "Back · Biceps",
            estimatedMinutes = 70, caloriesBurned = 490,
            exercises = listOf(
                warmUpTreadmill(5), mobility(),
                Exercise("Pull-Ups (or Assisted)", 4, "Max", 75, "Back", "Strict · full hang", difficulty = "Advanced"),
                Exercise("Lat Pulldown", 4, "10", 60, "Back", "Tempo 3-1-1", difficulty = "Advanced"),
                Exercise("Single-Arm DB Row", 4, "10/arm", 45, "Back", "Squeeze · slow eccentric", difficulty = "Advanced"),
                Exercise("Face Pulls", 4, "15", 30, "Rear Delts", "External rotation", difficulty = "Medium"),
                Exercise("Biceps Superset (DB Curl + Hammer)", 4, "10+10", 30, "Biceps", "Back-to-back", difficulty = "Advanced"),
                Exercise("Steady-State Cycling", 1, "12 min", 0, "Cardio", "Moderate · RPE 5", difficulty = "Medium"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 9, dayNumber = 4,
            name = "Lower + HIIT Finisher",
            type = "Strength", focus = "Legs · Conditioning",
            estimatedMinutes = 80, caloriesBurned = 600,
            exercises = listOf(
                warmUpTreadmill(5), mobility(),
                Exercise("Goblet Squat", 4, "10", 75, "Quads", "Tempo 3-1-1", difficulty = "Advanced"),
                Exercise("Romanian Deadlift (DB)", 4, "10", 75, "Hamstrings/Glutes", "Push hips back", difficulty = "Advanced"),
                Exercise("Bulgarian Split Squat", 3, "10/leg", 60, "Quads/Glutes", "Rear foot elevated", difficulty = "Advanced"),
                Exercise("Leg Press", 3, "12", 60, "Quads", "Pump finisher", difficulty = "Medium"),
                Exercise("Hamstring Curl Machine", 3, "12", 45, "Hamstrings", "Squeeze", difficulty = "Medium"),
                Exercise("Calf Raises", 4, "15", 45, "Calves", "Pause + squeeze", difficulty = "Medium"),
                Exercise("Treadmill HIIT Finisher", 1, "10 min", 0, "Cardio", "30 sec sprint / 60 sec easy", difficulty = "Advanced"),
                coolDown()
            )
        ),

        // ───── WEEK 10  (slight load bump · higher intensity) ────────────────
        WorkoutDay(
            weekNumber = 10, dayNumber = 1,
            name = "Upper Push · +Intensity",
            type = "Strength", focus = "Chest · Shoulders · Triceps",
            estimatedMinutes = 75, caloriesBurned = 540,
            exercises = listOf(
                warmUpTreadmill(5), mobility(),
                Exercise("DB Bench Press", 5, "6–8", 90, "Chest", "+5% load · 5 sets", difficulty = "Advanced"),
                Exercise("Incline DB Press", 4, "8–10", 60, "Upper Chest", "+5% load", difficulty = "Advanced"),
                Exercise("Arnold Press", 4, "10", 60, "Shoulders", "+5% load", difficulty = "Advanced"),
                Exercise("Lateral Raise Drop Set", 3, "12 → 12", 60, "Side Delts", "Heavier top weight", difficulty = "Advanced"),
                Exercise("Tricep Pushdown Burnout", 4, "12–15", 30, "Triceps", "Last set to failure", difficulty = "Advanced"),
                Exercise("Overhead Tricep Extension", 3, "12", 30, "Triceps", "Full ROM", difficulty = "Medium"),
                Exercise("Incline Walk Finisher", 1, "15 min", 0, "Cardio", "Incline 10–12%", difficulty = "Medium"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 10, dayNumber = 2,
            name = "HIIT + Core · +Intensity",
            type = "HIIT", focus = "Conditioning · Definition",
            estimatedMinutes = 55, caloriesBurned = 520,
            exercises = listOf(
                Exercise("Bike HIIT", 1, "22 min", 0, "Cardio", "20 sec all-out / 40 sec easy · 22 rounds", difficulty = "Advanced"),
                Exercise("Hanging Leg Raises", 5, "12", 45, "Lower Abs", "Extra set", difficulty = "Advanced"),
                Exercise("Cable Woodchopper", 4, "12/side", 45, "Obliques", "+5–10% load", difficulty = "Advanced"),
                Exercise("Dragon Flag", 3, "6–8", 60, "Core", "+1 rep vs W9", difficulty = "Advanced"),
                Exercise("Side Plank with Reach-Through", 4, "14/side", 30, "Obliques", "+2 reps", difficulty = "Advanced"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 10, dayNumber = 3,
            name = "Upper Pull · +Intensity",
            type = "Strength", focus = "Back · Biceps",
            estimatedMinutes = 75, caloriesBurned = 520,
            exercises = listOf(
                warmUpTreadmill(5), mobility(),
                Exercise("Pull-Ups (or Weighted)", 5, "6–8", 90, "Back", "Add weight if BW easy", difficulty = "Advanced"),
                Exercise("Lat Pulldown", 4, "8–10", 60, "Back", "+5% load", difficulty = "Advanced"),
                Exercise("Single-Arm DB Row", 4, "10/arm", 45, "Back", "+5% load", difficulty = "Advanced"),
                Exercise("Face Pulls", 4, "15", 30, "Rear Delts", "Strict", difficulty = "Medium"),
                Exercise("Biceps Superset", 4, "10+10", 30, "Biceps", "Curl + Hammer · no rest between", difficulty = "Advanced"),
                Exercise("Steady-State Cycling", 1, "15 min", 0, "Cardio", "Moderate", difficulty = "Medium"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 10, dayNumber = 4,
            name = "Lower + HIIT Finisher · +Intensity",
            type = "Strength", focus = "Legs · Conditioning",
            estimatedMinutes = 85, caloriesBurned = 640,
            exercises = listOf(
                warmUpTreadmill(5), mobility(),
                Exercise("Goblet Squat", 5, "8–10", 90, "Quads", "+5% load · 5 sets", difficulty = "Advanced"),
                Exercise("Romanian Deadlift (DB)", 4, "10", 75, "Hamstrings/Glutes", "+5% load", difficulty = "Advanced"),
                Exercise("Bulgarian Split Squat", 4, "10/leg", 60, "Quads/Glutes", "Extra set", difficulty = "Advanced"),
                Exercise("Leg Press", 4, "12", 60, "Quads", "Extra set", difficulty = "Medium"),
                Exercise("Hamstring Curl Machine", 4, "12", 45, "Hamstrings", "Extra set", difficulty = "Medium"),
                Exercise("Calf Raises", 4, "17", 45, "Calves", "+2 reps", difficulty = "Medium"),
                Exercise("Treadmill HIIT Finisher", 1, "12 min", 0, "Cardio", "30 sec sprint / 60 sec easy", difficulty = "Advanced"),
                coolDown()
            )
        ),

        // ───── WEEK 11  (peak intensity · denser sessions) ───────────────────
        WorkoutDay(
            weekNumber = 11, dayNumber = 1,
            name = "Upper Push · Peak",
            type = "Strength", focus = "Chest · Shoulders · Triceps",
            estimatedMinutes = 75, caloriesBurned = 570,
            exercises = listOf(
                warmUpTreadmill(5), mobility(),
                Exercise("DB Bench Press", 5, "6–8", 75, "Chest", "Same load · shorter rest", difficulty = "Advanced"),
                Exercise("Incline DB Press", 4, "8", 60, "Upper Chest", "Strict tempo", difficulty = "Advanced"),
                Exercise("Arnold Press", 4, "8–10", 60, "Shoulders", "+5% load", difficulty = "Advanced"),
                Exercise("Lateral Raise Triple Drop", 3, "10·10·10", 60, "Side Delts", "Drop weight twice each set", difficulty = "Advanced"),
                Exercise("Tricep Superset", 4, "10+10", 30, "Triceps", "Pushdown + Overhead Ext · back-to-back", difficulty = "Advanced"),
                Exercise("HIIT Treadmill Finisher", 1, "12 min", 0, "Cardio", "30 sec sprint / 30 sec easy", difficulty = "Advanced"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 11, dayNumber = 2,
            name = "HIIT + Core · Peak",
            type = "HIIT", focus = "Conditioning · Definition",
            estimatedMinutes = 60, caloriesBurned = 560,
            exercises = listOf(
                Exercise("Mixed HIIT", 1, "25 min", 0, "Cardio", "5 min bike + 5 min row + 5 min tread · repeat", difficulty = "Advanced"),
                Exercise("Hanging Leg Raise (Toes-to-Bar)", 5, "10", 45, "Lower Abs", "Strict · slow lower", difficulty = "Advanced"),
                Exercise("Cable Woodchopper", 4, "12/side", 30, "Obliques", "Shorter rest", difficulty = "Advanced"),
                Exercise("Dragon Flag", 4, "6–8", 60, "Core", "Extra set", difficulty = "Advanced"),
                Exercise("Weighted Plank", 4, "60 sec", 30, "Core", "Plate on back", difficulty = "Advanced"),
                Exercise("Mountain Climber Sprint", 3, "45 sec", 30, "Core/Cardio", "Fast tempo", difficulty = "Medium"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 11, dayNumber = 3,
            name = "Upper Pull · Peak",
            type = "Strength", focus = "Back · Biceps",
            estimatedMinutes = 75, caloriesBurned = 550,
            exercises = listOf(
                warmUpTreadmill(5), mobility(),
                Exercise("Pull-Ups (Weighted if possible)", 5, "6–8", 90, "Back", "Top-set heaviest", difficulty = "Advanced"),
                Exercise("Lat Pulldown", 4, "8", 60, "Back", "+5% load · strict", difficulty = "Advanced"),
                Exercise("Single-Arm DB Row", 4, "8/arm", 45, "Back", "+5% load", difficulty = "Advanced"),
                Exercise("Face Pulls", 4, "15", 30, "Rear Delts", "Form first · no rest creep", difficulty = "Medium"),
                Exercise("Biceps 21s", 3, "21", 45, "Biceps", "7 bottom · 7 top · 7 full", difficulty = "Advanced"),
                Exercise("Steady-State Cycling", 1, "15 min", 0, "Cardio", "Moderate", difficulty = "Medium"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 11, dayNumber = 4,
            name = "Lower + HIIT · Peak",
            type = "Strength", focus = "Legs · Conditioning",
            estimatedMinutes = 90, caloriesBurned = 680,
            exercises = listOf(
                warmUpTreadmill(5), mobility(),
                Exercise("Goblet Squat", 5, "8", 75, "Quads", "Tempo 3-1-1 · shorter rest", difficulty = "Advanced"),
                Exercise("Romanian Deadlift (DB)", 5, "8–10", 75, "Hamstrings/Glutes", "+5% load · 5 sets", difficulty = "Advanced"),
                Exercise("Bulgarian Split Squat", 4, "12/leg", 60, "Quads/Glutes", "+2 reps", difficulty = "Advanced"),
                Exercise("Leg Press", 4, "12", 45, "Quads", "Shorter rest", difficulty = "Medium"),
                Exercise("Hamstring Curl Machine", 4, "12–15", 45, "Hamstrings", "Burnout last set", difficulty = "Medium"),
                Exercise("Calf Raise Burnout", 4, "20", 30, "Calves", "High rep finisher", difficulty = "Medium"),
                Exercise("Treadmill HIIT Finisher", 1, "12 min", 0, "Cardio", "30 sec sprint / 45 sec easy", difficulty = "Advanced"),
                coolDown()
            )
        ),

        // ───── WEEK 12  (taper + test · slightly less volume · same intensity) ─
        WorkoutDay(
            weekNumber = 12, dayNumber = 1,
            name = "Upper Push · Test Week",
            type = "Strength", focus = "Performance Test",
            estimatedMinutes = 65, caloriesBurned = 480,
            exercises = listOf(
                warmUpTreadmill(5), mobility(),
                Exercise("DB Bench Press · Heavy Top Set", 3, "5", 90, "Chest", "Find heaviest 5 you can hit clean", difficulty = "Advanced"),
                Exercise("Incline DB Press", 3, "8", 75, "Upper Chest", "Quality reps", difficulty = "Advanced"),
                Exercise("Arnold Press", 3, "10", 60, "Shoulders", "Form check", difficulty = "Medium"),
                Exercise("Lateral Raises", 3, "12", 45, "Side Delts", "Strict", difficulty = "Medium"),
                Exercise("Tricep Pushdown", 3, "12", 45, "Triceps", "Controlled", difficulty = "Medium"),
                Exercise("Easy Walk", 1, "10 min", 0, "Cardio", "Cool down", difficulty = "Beginner"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 12, dayNumber = 2,
            name = "HIIT + Core · Test Week",
            type = "HIIT", focus = "Conditioning Test",
            estimatedMinutes = 45, caloriesBurned = 440,
            exercises = listOf(
                Exercise("HIIT Test", 1, "20 min", 0, "Cardio", "30 sec sprint / 60 sec easy · log RPE per round", difficulty = "Advanced"),
                Exercise("Hanging Leg Raises · Test", 4, "Max", 60, "Lower Abs", "Log best set", difficulty = "Advanced"),
                Exercise("Plank Hold · Test", 1, "Max", 0, "Core", "Hold as long as possible · log time", difficulty = "Advanced"),
                Exercise("Side Plank · Test", 1, "Max/side", 0, "Obliques", "Log per side", difficulty = "Advanced"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 12, dayNumber = 3,
            name = "Upper Pull · Test Week",
            type = "Strength", focus = "Performance Test",
            estimatedMinutes = 65, caloriesBurned = 470,
            exercises = listOf(
                warmUpTreadmill(5), mobility(),
                Exercise("Pull-Ups · Max Set", 3, "Max", 90, "Back", "Log best set · strict reps only", difficulty = "Advanced"),
                Exercise("Lat Pulldown · Heavy", 3, "6–8", 75, "Back", "Find heaviest 6–8", difficulty = "Advanced"),
                Exercise("Single-Arm DB Row", 3, "10/arm", 60, "Back", "Quality reps", difficulty = "Medium"),
                Exercise("Face Pulls", 3, "15", 30, "Rear Delts", "Form check", difficulty = "Medium"),
                Exercise("DB Bicep Curls", 3, "10", 45, "Biceps", "Clean reps", difficulty = "Medium"),
                Exercise("Easy Cycling", 1, "10 min", 0, "Cardio", "Cool down", difficulty = "Beginner"),
                coolDown()
            )
        ),
        WorkoutDay(
            weekNumber = 12, dayNumber = 4,
            name = "Lower · Test Week",
            type = "Strength", focus = "Performance Test",
            estimatedMinutes = 70, caloriesBurned = 540,
            exercises = listOf(
                warmUpTreadmill(5), mobility(),
                Exercise("Goblet Squat · Heavy Top Set", 3, "5", 90, "Quads", "Heaviest clean 5 · log it", difficulty = "Advanced"),
                Exercise("Romanian Deadlift (DB)", 3, "8", 75, "Hamstrings/Glutes", "Find heaviest 8", difficulty = "Advanced"),
                Exercise("Bulgarian Split Squat", 3, "10/leg", 60, "Quads/Glutes", "Form check · log load", difficulty = "Medium"),
                Exercise("Leg Press", 3, "10", 60, "Quads", "Quality reps", difficulty = "Medium"),
                Exercise("Calf Raises", 3, "15", 45, "Calves", "Pause + squeeze", difficulty = "Medium"),
                Exercise("HIIT Finisher", 1, "8 min", 0, "Cardio", "30 sec sprint / 60 sec easy", difficulty = "Advanced"),
                coolDown()
            )
        )
    )

    val motivationalTips = listOf(
        "💪 Don't stop until you're proud.",
        "🍕 Balance is key: Train hard, eat smart.",
        "💧 70% of your muscles are water. Drink up!",
        "🔥 Tomorrow's results start with today's sweat.",
        "⚡ You're one workout away from a good mood.",
        "💡 Focused on Quads? Keep the weight on your mid-foot/heels.",
        "💡 Obliques are built with rotation. Control every twist."
    )
}
