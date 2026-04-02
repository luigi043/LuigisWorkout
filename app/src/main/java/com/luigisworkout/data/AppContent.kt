package com.luigisworkout.data

// ─── DATA MODELS ───────────────────────────────────────────────────────────────

data class Recipe(
    val id: String,
    val name: String,
    val mealType: String,       // breakfast, lunch, dinner, snack
    val calories: Int,
    val protein: Float,
    val carbs: Float,
    val fat: Float,
    val prepMinutes: Int,
    val servings: Int = 1,
    val ingredients: List<String>,
    val steps: List<String>,
    val supermarket: String,    // Continente, Pingo Doce, Lidl, Any
    val tags: List<String> = emptyList()
)

data class Exercise(
    val name: String,
    val sets: Int,
    val reps: String,           // "10", "10-15", "30 sec", "AMRAP"
    val restSeconds: Int,
    val durationSeconds: Int? = null, // Time for each exercise if applicable
    val muscleGroup: String,
    val description: String,
    val modification: String = ""
)

data class WorkoutDay(
    val weekNumber: Int,
    val dayNumber: Int,
    val name: String,
    val type: String,           // "Strength", "Cardio", "HIIT", "Recovery"
    val estimatedMinutes: Int,
    val caloriesBurned: Int,
    val exercises: List<Exercise>
)

data class FoodItem(
    val name: String,
    val calories: Int,
    val protein: Float,
    val carbs: Float,
    val fat: Float,
    val unit: String = "100g",
    val supermarket: String = "Any"
)

// ─── RECIPES ───────────────────────────────────────────────────────────────────

object AppContent {

    val recipes = listOf(

        // ── BREAKFASTS ──────────────────────────────────────────────────

        Recipe(
            id = "pb_01",
            name = "Banana and Honey Oatmeal",
            mealType = "breakfast",
            calories = 340,
            protein = 12f, carbs = 58f, fat = 6f,
            prepMinutes = 5,
            ingredients = listOf(
                "60g oat flakes",
                "250ml semi-skimmed or plant-based milk",
                "1 medium banana",
                "1 tsp honey",
                "1 pinch of cinnamon"
            ),
            steps = listOf(
                "Put the oat flakes and milk in a pot.",
                "Heat over medium heat for 4-5 minutes, stirring constantly.",
                "Slice the banana and place on top.",
                "Drizzle with honey and sprinkle with cinnamon.",
                "Serve immediately."
            ),
            supermarket = "Any",
            tags = listOf("fast", "budget", "protein")
        ),

        Recipe(
            id = "pb_02",
            name = "Scrambled Eggs on Toast with Tomato",
            mealType = "breakfast",
            calories = 380,
            protein = 22f, carbs = 35f, fat = 14f,
            prepMinutes = 10,
            ingredients = listOf(
                "2 slices of wholemeal bread",
                "3 medium eggs",
                "1 small tomato",
                "Salt, pepper and oregano to taste",
                "1 dash of olive oil"
            ),
            steps = listOf(
                "Toast the bread.",
                "Whisk the eggs with salt and pepper.",
                "Heat olive oil in a pan and add the eggs.",
                "Stir gently over low heat until creamy.",
                "Slice the tomato.",
                "Place the eggs on the toast and the tomato on the side."
            ),
            supermarket = "Any",
            tags = listOf("protein", "satiating")
        ),

        Recipe(
            id = "pb_03",
            name = "Greek Yogurt with Berries and Granola",
            mealType = "breakfast",
            calories = 290,
            protein = 18f, carbs = 38f, fat = 6f,
            prepMinutes = 3,
            ingredients = listOf(
                "200g plain Greek yogurt",
                "100g frozen mixed berries (thawed)",
                "30g simple granola",
                "1 tsp honey (optional)"
            ),
            steps = listOf(
                "Place yogurt in a bowl.",
                "Distribute berries on top.",
                "Sprinkle with granola.",
                "Add honey if desired."
            ),
            supermarket = "Lidl",
            tags = listOf("no cook", "fast", "probiotics")
        ),

        // ── LUNCHES ──────────────────────────────────────────────────────────

        Recipe(
            id = "al_01",
            name = "Grilled Chicken with Rice and Broccoli",
            mealType = "lunch",
            calories = 520,
            protein = 45f, carbs = 52f, fat = 12f,
            prepMinutes = 25,
            ingredients = listOf(
                "150g chicken breast",
                "80g raw long-grain rice",
                "200g fresh or frozen broccoli",
                "Juice of 1/2 lemon",
                "Garlic, salt, pepper, olive oil",
                "Herbs to taste"
            ),
            steps = listOf(
                "Season the chicken with garlic, lemon, salt and pepper. Let marinate 10 min.",
                "Cook the rice according to package instructions.",
                "Steam or boil the broccoli for 8 minutes.",
                "Grill the chicken for 6-7 minutes on each side.",
                "Serve everything together with a drizzle of olive oil."
            ),
            supermarket = "Any",
            tags = listOf("high protein", "complete", "low-fat")
        ),

        Recipe(
            id = "al_02",
            name = "Vegetable Soup with Poached Egg",
            mealType = "lunch",
            calories = 280,
            protein = 16f, carbs = 30f, fat = 8f,
            prepMinutes = 20,
            ingredients = listOf(
                "2 carrots",
                "1 zucchini",
                "2 medium potatoes",
                "1 onion",
                "2 eggs",
                "Salt, olive oil, cilantro or parsley",
                "1 vegetable bouillon cube"
            ),
            steps = listOf(
                "Peel and dice all vegetables.",
                "Sauté the onion in olive oil for 3 minutes.",
                "Add the remaining vegetables, cover with water and add the bouillon cube.",
                "Cook for 15 minutes until tender.",
                "Blend half the soup for creaminess.",
                "Bring to a gentle boil and crack eggs directly into the soup.",
                "Poach for 4 minutes. Serve with fresh herbs."
            ),
            supermarket = "Any",
            tags = listOf("comforting", "low calorie", "budget")
        ),

        // ── SNACKS / JUICES ──────────────────────────────────────────────────

        Recipe(
            id = "sn_04",
            name = "Green Detox Juice",
            mealType = "snack",
            calories = 120,
            protein = 3f, carbs = 28f, fat = 0.5f,
            prepMinutes = 5,
            ingredients = listOf("Spinach", "Cucumber", "Green apple", "Lemon juice", "Ginger", "Water or ice"),
            steps = listOf("Blend spinach, cucumber, green apple, lemon juice, and a small piece of ginger.", "Add water or ice to thin it out if needed."),
            supermarket = "Any"
        ),
        Recipe(
            id = "sn_05",
            name = "Carrot Ginger Citrus",
            mealType = "snack",
            calories = 95,
            protein = 1.5f, carbs = 22f, fat = 0.3f,
            prepMinutes = 5,
            ingredients = listOf("Carrots", "Orange", "Lemon", "Ginger"),
            steps = listOf("Blend carrots, orange, lemon, and a small piece of ginger.", "It’s sweet, but low-cal."),
            supermarket = "Any"
        ),
        Recipe(
            id = "sn_06",
            name = "Berry Protein Booster",
            mealType = "snack",
            calories = 210,
            protein = 12f, carbs = 35f, fat = 2f,
            prepMinutes = 5,
            ingredients = listOf("Mixed berries", "Greek yogurt", "Spinach"),
            steps = listOf("Blend mixed berries, a bit of Greek yogurt, and a handful of spinach for fiber and protein."),
            supermarket = "Any"
        ),
        Recipe(
            id = "sn_07",
            name = "Watermelon Mint Cooler",
            mealType = "snack",
            calories = 85,
            protein = 1f, carbs = 20f, fat = 0.2f,
            prepMinutes = 5,
            ingredients = listOf("Watermelon", "Mint leaves", "Lime"),
            steps = listOf("Blend watermelon, a handful of mint leaves, and a squeeze of lime. It’s super refreshing and hydrating."),
            supermarket = "Any"
        ),
        Recipe(
            id = "sn_08",
            name = "Beetroot Citrus Juice",
            mealType = "snack",
            calories = 110,
            protein = 2f, carbs = 25f, fat = 0.2f,
            prepMinutes = 5,
            ingredients = listOf("Cooked beetroot", "Orange", "Carrot"),
            steps = listOf("Blend cooked beetroot, orange, and a bit of carrot. It’s rich in nutrients and naturally sweet."),
            supermarket = "Any"
        ),
        Recipe(
            id = "sn_09",
            name = "Apple Cinnamon Zing",
            mealType = "snack",
            calories = 90,
            protein = 1f, carbs = 22f, fat = 0.3f,
            prepMinutes = 5,
            ingredients = listOf("Apple", "Cinnamon", "Carrot"),
            steps = listOf("Blend apple, a dash of cinnamon, and a small piece of carrot for a subtle sweet kick."),
            supermarket = "Any"
        )
    )

    // ─── WORKOUT PLAN (12 WEEKS) ────────────────────────────────────────────────

    val workoutPlan: List<WorkoutDay> = buildWorkoutPlan()

    private fun buildWorkoutPlan(): List<WorkoutDay> {
        val plan = mutableListOf<WorkoutDay>()

        // WEEK 1-4: Foundation (3 days/week)
        for (week in 1..4) {
            // Day 1 - Full Body A
            plan.add(WorkoutDay(
                weekNumber = week, dayNumber = 1,
                name = "Full Body A",
                type = "Strength",
                estimatedMinutes = 35,
                caloriesBurned = 200 + (week - 1) * 15,
                exercises = listOf(
                    Exercise("Squats", 3, "${8 + week * 2}", 60, 45, "Legs", "Feet shoulder-width apart, lower to 90°"),
                    Exercise("Push-ups", 3, "${6 + week * 2}", 60, 45, "Chest", "Body in a straight line, lower chest near floor", "Knees on floor if difficult"),
                    Exercise("Alternating Lunges", 3, "${8 + week * 2} each", 60, 60, "Legs", "Long step forward, back knee near floor"),
                    Exercise("Plank", 3, "${20 + week * 5} sec", 45, 30 + week * 5, "Core", "Body straight, don't let hips drop"),
                    Exercise("Superman", 3, "12", 45, 45, "Back", "Lie face down, lift arms and legs simultaneously"),
                    Exercise("Wall Sit", 3, "${30 + week * 5} sec", 45, 30 + week * 5, "Legs", "Back against wall, thighs parallel to floor")
                )
            ))

            // Day 2 - Cardio + Core
            plan.add(WorkoutDay(
                weekNumber = week, dayNumber = 2,
                name = "Cardio + Core",
                type = "Cardio",
                estimatedMinutes = 30,
                caloriesBurned = 250 + (week - 1) * 20,
                exercises = listOf(
                    Exercise("Jumping Jacks", 3, "30 sec", 30, 30, "Cardio", "Jump opening legs and arms simultaneously"),
                    Exercise("Mountain Climbers", 3, "30 sec", 45, 30, "Core/Cardio", "Plank position, alternate legs quickly"),
                    Exercise("Crunches", 3, "${12 + week * 2}", 45, 45, "Abdominals", "Hands behind head, lift only shoulders"),
                    Exercise("Bicycle Crunches", 3, "20 each side", 45, 60, "Abdominals", "Elbow to opposite knee, alternating"),
                    Exercise("Modified Burpees", 3, "${5 + week}", 60, 60, "Total Cardio", "Squat, hands down, step back, step in, stand up"),
                    Exercise("Lying Leg Raises", 3, "${10 + week * 2}", 45, 45, "Lower Abdominals", "Lie down, hands under glutes, lift legs straight")
                )
            ))

            // Day 3 - Full Body B
            plan.add(WorkoutDay(
                weekNumber = week, dayNumber = 3,
                name = "Full Body B",
                type = "Strength",
                estimatedMinutes = 35,
                caloriesBurned = 200 + (week - 1) * 15,
                exercises = listOf(
                    Exercise("Glute Bridge", 3, "${12 + week * 2}", 45, 45, "Glutes", "Lie down, knees bent, lift hips to max"),
                    Exercise("Incline Push-ups", 3, "${8 + week * 2}", 60, 45, "Upper Chest", "Hands on elevated surface"),
                    Exercise("Sumo Squats", 3, "${10 + week * 2}", 45, 45, "Legs/Glutes", "Wide stance, feet pointed out"),
                    Exercise("Chair Dips", 3, "${8 + week * 2}", 60, 45, "Triceps", "Hands on chair behind, bend and straighten elbows"),
                    Exercise("Side Plank", 3, "${15 + week * 5} sec each", 45, 30 + week * 5, "Core", "Weight on one forearm and side of foot"),
                    Exercise("Good Morning", 3, "12", 45, 45, "Lower Back", "Stand, hands on neck, lean torso forward")
                )
            ))
        }

        return plan
    }

    // ─── FOOD DATABASE ─────────────────────────────────────────────────────────

    val foodDatabase = listOf(
        FoodItem("Cooked Chicken Breast", 165, 31f, 0f, 3.6f, "100g", "Any"),
        FoodItem("Whole Egg", 155, 13f, 1.1f, 11f, "100g", "Any"),
        FoodItem("Canned Tuna (natural)", 116, 25f, 0f, 1f, "100g", "Any"),
        FoodItem("Grilled Salmon", 208, 29f, 0f, 10f, "100g", "Any"),
        FoodItem("Plain Greek Yogurt", 97, 9f, 3.6f, 5f, "100g", "Any"),
        FoodItem("White Rice (cooked)", 130, 2.7f, 28f, 0.3f, "100g", "Any"),
        FoodItem("Whole Grain Bread", 247, 8.5f, 41f, 3.4f, "100g", "Any"),
        FoodItem("Oat Flakes", 389, 17f, 66f, 7f, "100g", "Any"),
        FoodItem("Cooked Broccoli", 35, 2.4f, 7f, 0.4f, "100g", "Any"),
        FoodItem("Apple", 52, 0.3f, 14f, 0.2f, "100g", "Any"),
        FoodItem("Banana", 89, 1.1f, 23f, 0.3f, "100g", "Any"),
        FoodItem("Peanut Butter", 588, 25f, 20f, 50f, "100g", "Any")
    )

    // ─── MOTIVATIONAL TIPS ─────────────────────────────────────────────────────

    val motivationalTips = listOf(
        "💪 Every workout counts! Even 20 minutes makes a difference.",
        "🥗 Eat slowly — it takes your brain 20 min to register fullness.",
        "💧 Drink water before every meal — it reduces hunger by 13%.",
        "😴 Sleep is half the success! Get 7-8 hours a night.",
        "🚶 10,000 steps/day burns up to 400 extra calories!",
        "🍳 Meal prep on Sunday — avoid bad decisions during the week.",
        "📏 Measure monthly, not daily — weight varies naturally.",
        "🫁 Breathe during training! Exhale on effort.",
        "🏠 Training at home saves time and money — consistency is key!",
        "🌿 Protein in every meal = longer satiety + muscle preserved.",
        "🎯 Focus on progress, not perfection. A bad day doesn't ruin everything.",
        "🛒 Make a shopping list — avoid impulse buys.",
        "🏋️ Consistency beats intensity. Better to train 4x/week than 1x very intense."
    )
}
