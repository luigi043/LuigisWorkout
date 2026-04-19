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

        // ── PRE/POST WORKOUT & SNACKS ───────────────────────────────────
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
        FoodItem("Sweet Potato", 86, 1.6f, 20f, 0.1f)
    )

    val workoutPlan: List<WorkoutDay> = listOf(
        WorkoutDay(
            weekNumber = 1, dayNumber = 1,
            name = "Leg Day Foundation",
            type = "Strength",
            focus = "Quads & Glutes",
            estimatedMinutes = 45,
            caloriesBurned = 350,
            exercises = listOf(
                Exercise("Goblet Squats", 4, "12", 60, muscleGroup = "Legs", description = "Hold weight at chest level", difficulty = "Beginner"),
                Exercise("Lunges", 3, "10 per leg", 45, muscleGroup = "Legs", description = "Keep back straight", difficulty = "Beginner"),
                Exercise("Calf Raises", 3, "15", 30, muscleGroup = "Calves", description = "Rise on toes", difficulty = "Beginner")
            )
        ),
        WorkoutDay(
            weekNumber = 1, dayNumber = 2,
            name = "Upper Body Push",
            type = "Strength",
            focus = "Chest & Shoulders",
            estimatedMinutes = 40,
            caloriesBurned = 280,
            exercises = listOf(
                Exercise("Push-ups", 3, "Max", 60, muscleGroup = "Chest", description = "Straight line body", difficulty = "Medium"),
                Exercise("Shoulder Press", 3, "12", 60, muscleGroup = "Shoulders", description = "Control the weight", difficulty = "Medium"),
                Exercise("Plank", 3, "45 sec", 45, muscleGroup = "Core", description = "Hold tight", difficulty = "Beginner")
            )
        ),
        WorkoutDay(
            weekNumber = 1, dayNumber = 3,
            name = "Cardio Blast",
            type = "HIIT",
            focus = "Endurance",
            estimatedMinutes = 30,
            caloriesBurned = 400,
            exercises = listOf(
                Exercise("Burpees", 4, "10", 30, muscleGroup = "Full Body", description = "Explosive movement", difficulty = "Advanced"),
                Exercise("Mountain Climbers", 4, "30 sec", 30, muscleGroup = "Core", description = "Fast knees", difficulty = "Medium"),
                Exercise("Jumping Jacks", 4, "1 min", 30, muscleGroup = "Cardio", description = "Stay on toes", difficulty = "Beginner")
            )
        )
    )

    val motivationalTips = listOf(
        "💪 Don't stop until you're proud.",
        "🍕 Balance is key: Train hard, eat smart.",
        "💧 70% of your muscles are water. Drink up!",
        "🔥 Tomorrow's results start with today's sweat.",
        "⚡ You're one workout away from a good mood."
    )
}
