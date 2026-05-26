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

    // ─── Plan generator ────────────────────────────────────────────────────────
    //
    // 12-week, 6-day-per-week Push/Pull/Legs split tuned for a 115 kg user with
    // sensitive knees who wants accelerated fat loss without sacrificing muscle.
    //
    // Day rotation (fixed across all 12 weeks):
    //   Day 1 — Push A (chest focus)
    //   Day 2 — Pull A (back width)
    //   Day 3 — Legs A (quad/glute, knee-safe)
    //   Day 4 — Push B (shoulder focus)
    //   Day 5 — Pull B (back thickness)
    //   Day 6 — Legs B (hamstring/glute hinge)
    //
    // What changes is the *intensity*, captured by [PhaseSpec]:
    //   Weeks 1–4   Foundation       3 sets, 10–12 reps, 60–90 s rest
    //   Weeks 5–8   Intensification  4 sets, 8–10 reps, 50–75 s rest, drop set last
    //   Weeks 9–12  Metabolic Peak   4 sets + supersets, 40–60 s rest, longer cardio
    //
    // Knee-safety rules baked in:
    //   • No barbell back squat, no jump squats, no burpees, no plyo lunges.
    //   • Leg press / hack squat capped at ~90° knee flexion (form notes say so).
    //   • Primary leg work is hip-dominant (RDL, hip thrust) and machine-based.
    //
    // Cardio is a LISS finisher (incline walk) on every day; weeks 9–12 swap a
    // couple of finishers for HIIT bike intervals for a stronger metabolic kick.

    private data class PhaseSpec(
        val name: String,
        val compoundSets: Int,
        val isoSets: Int,
        val compoundReps: String,
        val isoReps: String,
        val restCompoundSec: Int,
        val restIsoSec: Int,
        val cardioFinisherMin: Int,
        val cardioKcal: Int,           // approximate kcal from the finisher
        val cardioStyle: CardioStyle,  // LISS incline walk vs HIIT bike
        val dropSetNote: String,       // attached to the last isolation; "" if N/A
        val supersetNote: String       // attached to the final isolation; "" if N/A
    )

    private enum class CardioStyle { INCLINE_WALK, HIIT_BIKE }

    private fun phaseFor(week: Int): PhaseSpec = when (week) {
        in 1..4 -> PhaseSpec(
            name = "Foundation",
            compoundSets = 3, isoSets = 3,
            compoundReps = "10–12", isoReps = "12–15",
            restCompoundSec = 90, restIsoSec = 60,
            cardioFinisherMin = 15, cardioKcal = 150,
            cardioStyle = CardioStyle.INCLINE_WALK,
            dropSetNote = "", supersetNote = ""
        )
        in 5..8 -> PhaseSpec(
            name = "Intensification",
            compoundSets = 4, isoSets = 3,
            compoundReps = "8–10", isoReps = "10–12",
            restCompoundSec = 75, restIsoSec = 50,
            cardioFinisherMin = 18, cardioKcal = 200,
            cardioStyle = CardioStyle.INCLINE_WALK,
            dropSetNote = "Last set: drop weight ~30% and rep out to failure",
            supersetNote = ""
        )
        else -> PhaseSpec(
            name = "Metabolic Peak",
            compoundSets = 4, isoSets = 4,
            compoundReps = "6–10", isoReps = "12–15",
            restCompoundSec = 60, restIsoSec = 40,
            cardioFinisherMin = 22, cardioKcal = 280,
            cardioStyle = CardioStyle.HIIT_BIKE,
            dropSetNote = "Triple drop on last set · 30% off, then 30% off, to failure each",
            supersetNote = "Superset with previous exercise · zero rest between"
        )
    }

    private fun phaseCardio(p: PhaseSpec) = when (p.cardioStyle) {
        CardioStyle.INCLINE_WALK -> cardioFinish(p.cardioFinisherMin, "Incline Treadmill Walk", "10–12%", "5–6 km/h")
        CardioStyle.HIIT_BIKE -> Exercise(
            "HIIT Bike Intervals", 1, "${p.cardioFinisherMin} min", 0, "Cardio",
            "30 s all-out sprint / 60 s easy spin · repeat for the full block · maximal effort",
            difficulty = "Advanced"
        )
    }

    private fun mod(note: String) = note  // tiny alias so the call sites read cleanly

    // ─── Day templates ─────────────────────────────────────────────────────────

    private fun pushA(week: Int): WorkoutDay {
        val p = phaseFor(week)
        return WorkoutDay(
            weekNumber = week, dayNumber = 1,
            name = "Push A · Chest Focus",
            type = "Strength", focus = "Chest · Shoulders · Triceps",
            estimatedMinutes = 65,
            caloriesBurned = 470 + p.cardioKcal,
            exercises = listOf(
                warmUpTreadmill(5),
                mobility(),
                Exercise("Barbell Bench Press", p.compoundSets, p.compoundReps, p.restCompoundSec,
                    "Chest", "Plant feet · pinch shoulder blades · bar to lower chest · controlled descent",
                    difficulty = "Advanced"),
                Exercise("Incline DB Press (30°)", p.compoundSets, p.compoundReps, p.restCompoundSec,
                    "Upper Chest", "DBs press up and slightly in · full ROM at the bottom · don't bounce",
                    difficulty = "Medium"),
                Exercise("Cable Chest Fly (High to Low)", p.isoSets, p.isoReps, p.restIsoSec,
                    "Chest", "Slight elbow bend · meet in front of hips · 1 s squeeze",
                    modification = mod(p.dropSetNote), difficulty = "Medium"),
                Exercise("Seated DB Shoulder Press", p.compoundSets, p.compoundReps, p.restCompoundSec,
                    "Shoulders", "Neutral grip · elbows 30° forward · stop just shy of lockout",
                    difficulty = "Medium"),
                Exercise("Cable Tricep Pushdown", p.isoSets, p.isoReps, p.restIsoSec,
                    "Triceps", "Elbows pinned to ribs · full extension · slow 2 s eccentric",
                    modification = mod(p.supersetNote), difficulty = "Beginner"),
                phaseCardio(p),
                coolDown()
            )
        )
    }

    private fun pullA(week: Int): WorkoutDay {
        val p = phaseFor(week)
        return WorkoutDay(
            weekNumber = week, dayNumber = 2,
            name = "Pull A · Back Width",
            type = "Strength", focus = "Back · Biceps",
            estimatedMinutes = 65,
            caloriesBurned = 460 + p.cardioKcal,
            exercises = listOf(
                warmUpTreadmill(5),
                mobility(),
                Exercise("Lat Pulldown (Wide Grip)", p.compoundSets, p.compoundReps, p.restCompoundSec,
                    "Lats", "Pull bar to upper chest · drive elbows down · stretch at top",
                    difficulty = "Medium"),
                Exercise("Seated Cable Row (V-Handle)", p.compoundSets, p.compoundReps, p.restCompoundSec,
                    "Mid Back", "Chest up · drive elbows back · pause + squeeze at the ribs",
                    difficulty = "Medium"),
                Exercise("Single-Arm DB Row", p.compoundSets, p.compoundReps, p.restCompoundSec,
                    "Lats/Mid Back", "Brace on bench · pull to hip · don't twist torso",
                    difficulty = "Medium"),
                Exercise("Face Pull (Cable Rope)", p.isoSets, p.isoReps, p.restIsoSec,
                    "Rear Delts/Upper Back", "Rope to forehead · external rotation at the end · feel rear delts",
                    modification = mod(p.dropSetNote), difficulty = "Beginner"),
                Exercise("Incline DB Curl", p.isoSets, p.isoReps, p.restIsoSec,
                    "Biceps", "Bench 45° · let arms hang straight down · full stretch, full squeeze",
                    modification = mod(p.supersetNote), difficulty = "Beginner"),
                phaseCardio(p),
                coolDown()
            )
        )
    }

    private fun legsA(week: Int): WorkoutDay {
        val p = phaseFor(week)
        return WorkoutDay(
            weekNumber = week, dayNumber = 3,
            name = "Legs A · Quad & Glute",
            type = "Strength", focus = "Quads · Glutes · Calves",
            estimatedMinutes = 70,
            caloriesBurned = 580 + p.cardioKcal,
            exercises = listOf(
                warmUpTreadmill(6),
                Exercise("Knee Prep Mobility", 1, "5 min", 0, "Mobility",
                    "Banded knee circles · controlled BW squats to box · hip flexor stretch · ankle pumps",
                    difficulty = "Beginner"),
                Exercise("Leg Press (Feet High & Wide)", p.compoundSets, p.compoundReps, p.restCompoundSec,
                    "Quads/Glutes", "Stop at ~90° knee flexion · DO NOT round lower back · drive through heels",
                    difficulty = "Medium"),
                Exercise("Hip Thrust (Barbell)", p.compoundSets, p.compoundReps, p.restCompoundSec,
                    "Glutes", "Bar over hips · chin tucked · drive hips to ceiling · 1 s squeeze at top",
                    difficulty = "Medium"),
                Exercise("Hack Squat Machine", p.compoundSets, p.compoundReps, p.restCompoundSec,
                    "Quads", "Limit depth to thighs parallel · slow 3 s eccentric · push through whole foot",
                    difficulty = "Medium"),
                Exercise("Leg Extension (Light & Controlled)", p.isoSets, p.isoReps, p.restIsoSec,
                    "Quads", "Lighter than ego says · 2 s pause at top · stop if it bites the knee cap",
                    modification = mod(p.dropSetNote), difficulty = "Beginner"),
                Exercise("Standing Calf Raise (Machine)", p.isoSets, "15", p.restIsoSec,
                    "Calves", "Full stretch at the bottom · 1 s squeeze on top · don't bounce",
                    modification = mod(p.supersetNote), difficulty = "Beginner"),
                phaseCardio(p),
                coolDown()
            )
        )
    }

    private fun pushB(week: Int): WorkoutDay {
        val p = phaseFor(week)
        return WorkoutDay(
            weekNumber = week, dayNumber = 4,
            name = "Push B · Shoulder Focus",
            type = "Strength", focus = "Shoulders · Chest · Triceps",
            estimatedMinutes = 65,
            caloriesBurned = 470 + p.cardioKcal,
            exercises = listOf(
                warmUpTreadmill(5),
                mobility(),
                Exercise("Seated Barbell Shoulder Press", p.compoundSets, p.compoundReps, p.restCompoundSec,
                    "Shoulders", "Back flat on pad · bar to upper chest · press straight up · keep ribs down",
                    difficulty = "Advanced"),
                Exercise("Incline Bench Press (45°)", p.compoundSets, p.compoundReps, p.restCompoundSec,
                    "Upper Chest", "Bar to upper chest line · elbows ~45° · full ROM",
                    difficulty = "Medium"),
                Exercise("DB Lateral Raise", p.isoSets, p.isoReps, p.restIsoSec,
                    "Side Delts", "Lead with the elbows · DBs slightly forward · pause at shoulder height",
                    modification = mod(p.dropSetNote), difficulty = "Beginner"),
                Exercise("Cable Chest Fly (Low to High)", p.isoSets, p.isoReps, p.restIsoSec,
                    "Upper Chest", "Sweep up and across · meet at chin level · squeeze inner chest",
                    difficulty = "Medium"),
                Exercise("Overhead Cable Tricep Extension", p.isoSets, p.isoReps, p.restIsoSec,
                    "Triceps", "Elbows tight to head · full stretch at the bottom · slow extension",
                    modification = mod(p.supersetNote), difficulty = "Beginner"),
                phaseCardio(p),
                coolDown()
            )
        )
    }

    private fun pullB(week: Int): WorkoutDay {
        val p = phaseFor(week)
        return WorkoutDay(
            weekNumber = week, dayNumber = 5,
            name = "Pull B · Back Thickness",
            type = "Strength", focus = "Back · Rear Delts · Biceps",
            estimatedMinutes = 65,
            caloriesBurned = 470 + p.cardioKcal,
            exercises = listOf(
                warmUpTreadmill(5),
                mobility(),
                Exercise("Bent-Over Barbell Row (Underhand)", p.compoundSets, p.compoundReps, p.restCompoundSec,
                    "Mid Back/Lats", "Hinge to ~45° · bar to belly · pinch shoulder blades · neutral spine",
                    difficulty = "Advanced"),
                Exercise("Chest-Supported Row Machine", p.compoundSets, p.compoundReps, p.restCompoundSec,
                    "Mid Back", "Chest stays on pad · pull to ribs · don't shrug",
                    difficulty = "Medium"),
                Exercise("Reverse-Grip Lat Pulldown", p.compoundSets, p.compoundReps, p.restCompoundSec,
                    "Lats/Biceps", "Underhand · pull to lower chest · drive elbows down and back",
                    difficulty = "Medium"),
                Exercise("Reverse Pec Deck", p.isoSets, p.isoReps, p.restIsoSec,
                    "Rear Delts", "Arms slightly bent · open wide · pause at end range",
                    modification = mod(p.dropSetNote), difficulty = "Beginner"),
                Exercise("EZ-Bar Preacher Curl", p.isoSets, p.isoReps, p.restIsoSec,
                    "Biceps", "Full stretch at the bottom · don't bounce off the pad · 1 s squeeze",
                    modification = mod(p.supersetNote), difficulty = "Medium"),
                phaseCardio(p),
                coolDown()
            )
        )
    }

    private fun legsB(week: Int): WorkoutDay {
        val p = phaseFor(week)
        return WorkoutDay(
            weekNumber = week, dayNumber = 6,
            name = "Legs B · Hamstring & Glute Hinge",
            type = "Strength", focus = "Hamstrings · Glutes · Calves",
            estimatedMinutes = 70,
            caloriesBurned = 560 + p.cardioKcal,
            exercises = listOf(
                warmUpTreadmill(6),
                Exercise("Hinge Prep Mobility", 1, "5 min", 0, "Mobility",
                    "Cat-cow · hip airplanes · banded good-mornings · hamstring sweep",
                    difficulty = "Beginner"),
                Exercise("Romanian Deadlift (Barbell)", p.compoundSets, p.compoundReps, p.restCompoundSec,
                    "Hamstrings/Glutes", "Soft knees · push hips back · bar slides down thighs · stop when hamstrings shout",
                    difficulty = "Advanced"),
                Exercise("Barbell Hip Thrust (Heavy)", p.compoundSets, p.compoundReps, p.restCompoundSec,
                    "Glutes", "Heavier than Legs A · pause 2 s at the top · ribs down",
                    difficulty = "Medium"),
                Exercise("Seated Leg Curl Machine", p.isoSets, p.isoReps, p.restIsoSec,
                    "Hamstrings", "Toes neutral · 2 s squeeze at peak · slow eccentric",
                    modification = mod(p.dropSetNote), difficulty = "Beginner"),
                Exercise("Cable Glute Kickback", p.isoSets, "12/leg", p.restIsoSec,
                    "Glutes", "Slight forward lean · drive heel up and back · squeeze 1 s",
                    difficulty = "Beginner"),
                Exercise("Seated Calf Raise", p.isoSets, "15", p.restIsoSec,
                    "Calves/Soleus", "Knees over toes · full stretch · controlled tempo",
                    modification = mod(p.supersetNote), difficulty = "Beginner"),
                phaseCardio(p),
                coolDown()
            )
        )
    }

    val workoutPlan: List<WorkoutDay> = (1..12).flatMap { week ->
        listOf(pushA(week), pullA(week), legsA(week), pushB(week), pullB(week), legsB(week))
    }

    val motivationalTips = listOf(
        "💪 Don't stop until you're proud.",
        "🍕 Balance is key: Train hard, eat smart.",
        "💧 70% of your muscles are water. Drink up!",
        "🔥 Tomorrow's results start with today's sweat.",
        "⚡ You're one workout away from a good mood.",
        "💡 Focused on Quads? Keep the weight on your mid-foot/heels.",
        "💡 Obliques are built with rotation. Control every twist.",
        "🦵 Knee feeling cranky? Drop the depth, not the workout.",
        "🏋️ A heavier lift today is a lighter body tomorrow.",
        "⏱ Short rests + heavy weights = the fastest fat loss.",
        "🥩 Eat protein every 3–4 hours · muscle stays, fat goes.",
        "📉 You don't out-train a bad diet — track the calories."
    )
}
