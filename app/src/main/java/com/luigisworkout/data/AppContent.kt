package com.luigisworkout.data

// ─── DATA MODELS ───────────────────────────────────────────────────────────────

data class Recipe(
    val id: String,
    val name: String,
    val mealType: String,       // breakfast, lunch, dinner, snack, drink
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
    val cheapestAt: String = "Any",
    val suggestedTime: String = "",         // "HH:mm" — when in the day to eat this
    val approxCostEuro: Float = 0f,          // per serving, € · 0 = unknown
    val supplementNote: String = ""          // e.g. "Take 5g creatine with this meal"
)

/** A point in the user's daily meal schedule. */
data class MealSlot(
    val label: String,          // human-readable, e.g. "Late Snack + Creatine"
    val time: String,           // "HH:mm"
    val mealType: String        // matches Recipe.mealType so we can filter
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

        // ════════════════════════════════════════════════════════════════════
        // Budget-Bytes-style fat-loss menu for a 115 kg user on a 5-meal day:
        //   07:30  Breakfast    — protein + slow carbs
        //   09:00  Morning snack — protein top-up, easy to grab
        //   13:00  Lunch         — the big meal (salad / pasta / chicken / pizza)
        //   19:00  Dinner        — lean protein + veg, lower carbs
        //   20:00  Late snack    — slow-digesting protein + creatine window
        //
        // Costs are rough per-serving in € based on Lidl / Pingo Doce / Aldi
        // basics. Recipes scale linearly — meal-prep 3-4 servings at once.
        // ════════════════════════════════════════════════════════════════════

        // ── 07:30 · BREAKFAST ─────────────────────────────────────────────
        Recipe(
            id = "br_protein_oats",
            name = "Protein Oats Bowl",
            mealType = "breakfast",
            calories = 460, protein = 35f, carbs = 58f, fat = 10f,
            prepMinutes = 5,
            suggestedTime = "07:30",
            approxCostEuro = 0.95f,
            ingredients = listOf(
                "60g rolled oats",
                "300ml semi-skim milk",
                "1 scoop whey protein (~25g)",
                "1 banana",
                "1 tbsp peanut butter"
            ),
            steps = listOf(
                "Bring milk to a low simmer.",
                "Stir in oats, cook 3 min on low heat.",
                "Take off heat, whisk in whey (or it goes lumpy).",
                "Top with sliced banana and peanut butter."
            ),
            supermarket = "Lidl",
            tags = listOf("high-protein", "fast", "budget", "meal-prep")
        ),
        Recipe(
            id = "br_egg_scramble",
            name = "Quick Eggs + Toast",
            mealType = "breakfast",
            calories = 410, protein = 28f, carbs = 32f, fat = 18f,
            prepMinutes = 7,
            suggestedTime = "07:30",
            approxCostEuro = 0.70f,
            ingredients = listOf(
                "3 whole eggs",
                "2 slices wholegrain bread",
                "1 tsp olive oil",
                "Salt, pepper, paprika",
                "1 tomato (sliced)"
            ),
            steps = listOf(
                "Whisk the eggs with salt, pepper and paprika.",
                "Heat olive oil in a non-stick pan on medium-low.",
                "Pour eggs in, stir gently with a spatula until just set (~2 min).",
                "Toast the bread, top with eggs and sliced tomato."
            ),
            supermarket = "Pingo Doce",
            tags = listOf("high-protein", "fast", "budget")
        ),
        Recipe(
            id = "br_tuna_crackers",
            name = "Portuguese Tuna & Crackers Bowl",
            mealType = "breakfast",
            calories = 380, protein = 32f, carbs = 36f, fat = 12f,
            prepMinutes = 4,
            suggestedTime = "07:30",
            approxCostEuro = 1.10f,
            ingredients = listOf(
                "1 can tuna in water (120g, drained)",
                "6 wholegrain crackers (Maria / Cream Cracker integral)",
                "1 tbsp light mayo",
                "Lemon juice + black pepper",
                "Handful of cherry tomatoes"
            ),
            steps = listOf(
                "Drain tuna well, flake into a bowl.",
                "Mix with mayo, lemon juice, plenty of black pepper.",
                "Serve with crackers and tomatoes on the side."
            ),
            supermarket = "Lidl",
            tags = listOf("no-cook", "high-protein", "budget")
        ),

        // ── 09:00 · MORNING SNACK ─────────────────────────────────────────
        Recipe(
            id = "sn_yogurt_almond",
            name = "Greek Yogurt, Honey & Almonds",
            mealType = "snack",
            calories = 280, protein = 22f, carbs = 24f, fat = 10f,
            prepMinutes = 2,
            suggestedTime = "09:00",
            approxCostEuro = 0.85f,
            ingredients = listOf(
                "200g 0% Greek yogurt",
                "1 tsp honey",
                "15g almonds (about 12 nuts)"
            ),
            steps = listOf(
                "Spoon yogurt into a small bowl.",
                "Drizzle honey on top, sprinkle the almonds.",
                "Done — 2 minutes flat."
            ),
            supermarket = "Aldi",
            tags = listOf("no-cook", "high-protein", "fast", "budget")
        ),
        Recipe(
            id = "sn_boiled_eggs",
            name = "2 Boiled Eggs + Coffee",
            mealType = "snack",
            calories = 180, protein = 14f, carbs = 1f, fat = 13f,
            prepMinutes = 10,
            suggestedTime = "09:00",
            approxCostEuro = 0.40f,
            ingredients = listOf(
                "2 large eggs",
                "Salt, black pepper",
                "Black coffee, unsweetened"
            ),
            steps = listOf(
                "Boil eggs 8 min from cold water (firm yolks) — batch 6 on Sunday.",
                "Peel, season with salt and pepper.",
                "Pair with coffee."
            ),
            supermarket = "Any",
            tags = listOf("meal-prep", "high-protein", "very-cheap")
        ),
        Recipe(
            id = "sn_pb_toast",
            name = "Peanut Butter Banana Toast",
            mealType = "snack",
            calories = 320, protein = 12f, carbs = 38f, fat = 14f,
            prepMinutes = 3,
            suggestedTime = "09:00",
            approxCostEuro = 0.55f,
            ingredients = listOf(
                "1 slice wholegrain bread",
                "1 tbsp natural peanut butter",
                "1 banana"
            ),
            steps = listOf(
                "Toast the bread until crisp.",
                "Spread peanut butter, top with banana slices."
            ),
            supermarket = "Lidl",
            tags = listOf("no-cook", "fast", "budget")
        ),

        // ── 13:00 · LUNCH (variety: salad / pasta / chicken / pizza) ──────
        Recipe(
            id = "ln_chicken_rice_bowl",
            name = "Chicken & Rice Power Bowl",
            mealType = "lunch",
            calories = 620, protein = 48f, carbs = 70f, fat = 12f,
            prepMinutes = 25,
            servings = 1,
            suggestedTime = "13:00",
            approxCostEuro = 2.10f,
            ingredients = listOf(
                "150g chicken breast",
                "80g long-grain rice (dry weight)",
                "1 tsp olive oil",
                "Paprika, garlic powder, salt, pepper",
                "100g frozen mixed vegetables",
                "1 tbsp soy sauce"
            ),
            steps = listOf(
                "Cook rice per pack (15 min).",
                "Season chicken with paprika, garlic, salt, pepper.",
                "Pan-fry chicken in olive oil over medium heat, 4-5 min per side.",
                "Microwave frozen veg, 3 min.",
                "Slice chicken, stack on rice + veg, finish with soy sauce."
            ),
            supermarket = "Pingo Doce",
            tags = listOf("meal-prep", "high-protein", "budget", "chicken")
        ),
        Recipe(
            id = "ln_tuna_pasta_salad",
            name = "Tuna Pasta Salad",
            mealType = "lunch",
            calories = 540, protein = 38f, carbs = 62f, fat = 14f,
            prepMinutes = 15,
            suggestedTime = "13:00",
            approxCostEuro = 1.40f,
            ingredients = listOf(
                "100g pasta (penne or fusilli, dry)",
                "1 can tuna in water (120g, drained)",
                "100g cherry tomatoes (halved)",
                "1/2 cucumber diced",
                "1 tbsp olive oil",
                "1 tbsp lemon juice",
                "Salt, pepper, oregano"
            ),
            steps = listOf(
                "Boil pasta in salted water until al dente; drain and rinse cold.",
                "Toss pasta with tuna, tomatoes and cucumber.",
                "Dress with olive oil, lemon juice, salt, pepper, oregano.",
                "Chill 10 min if you can — better cold."
            ),
            supermarket = "Lidl",
            tags = listOf("meal-prep", "salad", "pasta", "budget")
        ),
        Recipe(
            id = "ln_pesto_chicken_pasta",
            name = "Pesto Chicken Pasta",
            mealType = "lunch",
            calories = 660, protein = 45f, carbs = 70f, fat = 18f,
            prepMinutes = 20,
            suggestedTime = "13:00",
            approxCostEuro = 2.30f,
            ingredients = listOf(
                "100g pasta (penne, dry)",
                "120g chicken breast, cubed",
                "2 tbsp pesto (jarred is fine)",
                "1 tsp olive oil",
                "Handful baby spinach",
                "Parmesan to taste"
            ),
            steps = listOf(
                "Boil pasta until al dente; reserve 2 tbsp cooking water.",
                "Pan-fry chicken cubes in olive oil over medium-high, 5-6 min.",
                "Off heat, stir pesto + reserved pasta water into the pan.",
                "Toss with pasta and spinach (it wilts from residual heat).",
                "Top with parmesan."
            ),
            supermarket = "Continente",
            tags = listOf("pasta", "chicken", "fast")
        ),
        Recipe(
            id = "ln_caesar_salad_cheap",
            name = "Cheap Chicken Caesar Salad",
            mealType = "lunch",
            calories = 480, protein = 42f, carbs = 18f, fat = 24f,
            prepMinutes = 15,
            suggestedTime = "13:00",
            approxCostEuro = 1.80f,
            ingredients = listOf(
                "150g chicken breast",
                "1 small romaine lettuce",
                "2 tbsp light mayo + 1 tsp lemon juice + 1 garlic clove (DIY dressing)",
                "20g parmesan, grated",
                "Croutons (or 1 slice toasted bread, cubed)",
                "Salt, pepper"
            ),
            steps = listOf(
                "Season and pan-fry chicken in 1 tsp olive oil, 4-5 min/side.",
                "Mix mayo + lemon + crushed garlic for the dressing.",
                "Tear lettuce into a big bowl, slice chicken on top.",
                "Toss with dressing, parmesan, croutons."
            ),
            supermarket = "Lidl",
            tags = listOf("salad", "chicken", "high-protein")
        ),
        Recipe(
            id = "ln_tortilla_pizza",
            name = "Tortilla Protein Pizza",
            mealType = "lunch",
            calories = 520, protein = 38f, carbs = 48f, fat = 18f,
            prepMinutes = 12,
            suggestedTime = "13:00",
            approxCostEuro = 1.60f,
            ingredients = listOf(
                "1 large wholewheat tortilla wrap",
                "3 tbsp tomato sauce (passata + salt + oregano)",
                "80g shredded mozzarella (light)",
                "100g cooked chicken or 60g lean ham, chopped",
                "Sliced bell pepper, red onion",
                "Black pepper, chili flakes"
            ),
            steps = listOf(
                "Preheat oven to 220°C.",
                "Place tortilla on a baking tray, spread tomato sauce thinly.",
                "Top with mozzarella, chicken/ham and veg.",
                "Bake 7-8 min until cheese bubbles and edges crisp.",
                "Season with pepper and chili flakes."
            ),
            supermarket = "Aldi",
            tags = listOf("pizza", "fast", "high-protein", "budget")
        ),

        // ── 19:00 · DINNER ────────────────────────────────────────────────
        Recipe(
            id = "dn_baked_chicken_veg",
            name = "Baked Chicken + Roasted Veg",
            mealType = "dinner",
            calories = 480, protein = 48f, carbs = 28f, fat = 16f,
            prepMinutes = 35,
            suggestedTime = "19:00",
            approxCostEuro = 2.20f,
            ingredients = listOf(
                "180g chicken breast",
                "1 zucchini, sliced",
                "1 bell pepper, sliced",
                "1 small onion, wedges",
                "2 tsp olive oil",
                "Garlic, paprika, oregano, salt, pepper"
            ),
            steps = listOf(
                "Preheat oven to 200°C.",
                "Toss veg with 1 tsp olive oil and seasonings on a sheet pan.",
                "Season chicken, drizzle with the other tsp of oil; nestle on the pan.",
                "Bake 25 min — chicken at 75°C internal."
            ),
            supermarket = "Pingo Doce",
            tags = listOf("meal-prep", "high-protein", "chicken", "low-carb")
        ),
        Recipe(
            id = "dn_lentil_stew",
            name = "Lentil & Tomato Stew",
            mealType = "dinner",
            calories = 430, protein = 24f, carbs = 62f, fat = 8f,
            prepMinutes = 30,
            servings = 4,
            suggestedTime = "19:00",
            approxCostEuro = 0.65f,
            ingredients = listOf(
                "250g dried lentils (red or brown)",
                "1 can chopped tomatoes (400g)",
                "1 onion, diced",
                "2 cloves garlic, minced",
                "1 tsp olive oil",
                "1 tsp cumin, 1 tsp smoked paprika, salt",
                "750ml veg or chicken stock"
            ),
            steps = listOf(
                "Sauté onion + garlic in olive oil 3 min.",
                "Stir in cumin and paprika 30 sec.",
                "Add lentils, tomatoes, stock; bring to boil.",
                "Simmer 20-25 min until lentils are soft.",
                "Salt to taste. Freezes well in 4 portions."
            ),
            supermarket = "Lidl",
            tags = listOf("vegetarian", "meal-prep", "very-cheap", "high-fiber")
        ),
        Recipe(
            id = "dn_chicken_chili",
            name = "Chicken Chili + Rice",
            mealType = "dinner",
            calories = 560, protein = 44f, carbs = 60f, fat = 12f,
            prepMinutes = 30,
            servings = 3,
            suggestedTime = "19:00",
            approxCostEuro = 1.80f,
            ingredients = listOf(
                "400g chicken breast, cubed",
                "1 can red kidney beans (400g, drained)",
                "1 can chopped tomatoes (400g)",
                "1 onion, diced",
                "1 bell pepper, diced",
                "2 tsp chili powder, 1 tsp cumin, salt",
                "240g rice (dry, for 3 servings)"
            ),
            steps = listOf(
                "Brown chicken cubes in a pot, 5 min.",
                "Add onion + pepper, cook 4 min.",
                "Add tomatoes, beans, spices; simmer 15 min.",
                "Meanwhile cook rice per pack.",
                "Serve chili over rice. Stores 4 days in fridge."
            ),
            supermarket = "Continente",
            tags = listOf("meal-prep", "high-protein", "chicken", "budget")
        ),

        // ── 20:00 · LATE SNACK · CREATINE WINDOW ──────────────────────────
        Recipe(
            id = "ls_casein_creatine",
            name = "Casein Shake + Creatine",
            mealType = "snack",
            calories = 220, protein = 30f, carbs = 12f, fat = 5f,
            prepMinutes = 2,
            suggestedTime = "20:00",
            approxCostEuro = 0.80f,
            ingredients = listOf(
                "1 scoop casein protein (~30g)",
                "250ml semi-skim milk",
                "5g creatine monohydrate",
                "1 tsp cocoa powder (optional)"
            ),
            steps = listOf(
                "Add milk to a shaker, then casein, then creatine.",
                "Shake 30 seconds until smooth.",
                "Drink right after — casein digests slowly, perfect before bed."
            ),
            supermarket = "Prozis / Decathlon",
            tags = listOf("supplement", "high-protein", "bedtime"),
            supplementNote = "Take your daily 5 g creatine here — timing doesn't matter as long as you're consistent every day."
        ),
        Recipe(
            id = "ls_cottage_cheese",
            name = "Cottage Cheese + Banana + Creatine",
            mealType = "snack",
            calories = 260, protein = 28f, carbs = 28f, fat = 4f,
            prepMinutes = 3,
            suggestedTime = "20:00",
            approxCostEuro = 1.00f,
            ingredients = listOf(
                "200g cottage cheese (low-fat)",
                "1 banana, sliced",
                "1 tsp honey",
                "5g creatine monohydrate (stirred into water)"
            ),
            steps = listOf(
                "Spoon cottage cheese into a bowl, top with banana and honey.",
                "Take creatine separately with a glass of water."
            ),
            supermarket = "Lidl",
            tags = listOf("no-cook", "supplement", "high-protein"),
            supplementNote = "5 g creatine monohydrate daily, every day, no loading needed."
        ),

        // ── DRINKS ────────────────────────────────────────────────────────
        Recipe(
            id = "dr_post_workout_shake",
            name = "Post-Workout Protein Shake",
            mealType = "drink",
            calories = 320, protein = 35f, carbs = 38f, fat = 4f,
            prepMinutes = 3,
            suggestedTime = "post-workout",
            approxCostEuro = 0.95f,
            ingredients = listOf(
                "1 scoop whey protein (~25g)",
                "300ml semi-skim milk",
                "1 banana",
                "5g creatine (optional, if you didn't take it earlier)"
            ),
            steps = listOf(
                "Blend everything 30 sec until smooth.",
                "Drink within 30 min of the last set for the simplest recovery window."
            ),
            supermarket = "Prozis / Decathlon",
            tags = listOf("drink", "supplement", "post-workout")
        ),
        Recipe(
            id = "dr_iced_coffee_creatine",
            name = "Iced Coffee + Creatine",
            mealType = "drink",
            calories = 35, protein = 1f, carbs = 4f, fat = 1f,
            prepMinutes = 5,
            suggestedTime = "pre-workout",
            approxCostEuro = 0.25f,
            ingredients = listOf(
                "1 double espresso (chilled)",
                "150ml cold semi-skim milk",
                "Ice cubes",
                "5g creatine monohydrate"
            ),
            steps = listOf(
                "Brew espresso, let it cool 5 min.",
                "Stir creatine into a splash of warm water until dissolved.",
                "Pour everything over ice."
            ),
            supermarket = "Any",
            tags = listOf("drink", "supplement", "pre-workout", "very-cheap"),
            supplementNote = "Caffeine + creatine together is fine — both improve training output."
        ),
        Recipe(
            id = "dr_lemon_mint_water",
            name = "Lemon & Mint Hydration Jug",
            mealType = "drink",
            calories = 10, protein = 0f, carbs = 2f, fat = 0f,
            prepMinutes = 3,
            servings = 4,
            suggestedTime = "all-day",
            approxCostEuro = 0.30f,
            ingredients = listOf(
                "1.5L cold water",
                "1 lemon, sliced",
                "Handful fresh mint",
                "Ice"
            ),
            steps = listOf(
                "Slice lemon, lightly bruise mint leaves.",
                "Combine in a jug with water + ice.",
                "Drink throughout the day — aim 3L total water at 115 kg."
            ),
            supermarket = "Any",
            tags = listOf("drink", "zero-cal", "hydration", "very-cheap")
        )
    )

    /**
     * Suggested daily meal schedule. Drives the "what should I be eating now?"
     * hint on the Meal Plan screen and could feed a future notification too.
     */
    val mealSchedule: List<MealSlot> = listOf(
        MealSlot("Breakfast", "07:30", "breakfast"),
        MealSlot("Morning Snack", "09:00", "snack"),
        MealSlot("Lunch", "13:00", "lunch"),
        MealSlot("Dinner", "19:00", "dinner"),
        MealSlot("Late Snack + Creatine", "20:00", "snack")
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
    // 12-week, 5-day-per-week split for a 28 y/o · 1.89 m · 115 kg user chasing
    // predictable FAT LOSS (115 → ~105 kg by September, 90-95 kg long term) while
    // holding onto muscle. Pairs with 2 400 kcal / 180 g protein and a 30-45 min
    // walk on the 2 OFF days (see the "off-day walk" tip).
    //
    // Day rotation (Mon-Fri, fixed across all 12 weeks) — every major muscle hit
    // ~2×/week, 6 working movements per day for high training volume:
    //   Day 1 — Push   · Chest / Shoulders / Triceps
    //   Day 2 — Pull   · Back / Biceps
    //   Day 3 — Legs   · Quads / Glutes / Hamstrings / Calves
    //   Day 4 — Shoulders & Arms · Delts / Biceps / Triceps / Core
    //   Day 5 — Full Body Conditioning · big lifts + core, longest cardio
    //
    // Exercise names are kept standard so the in-app "View Example" lands on the
    // matching musclewiki.com demo for each one.
    //
    // What changes is the *intensity*, captured by [PhaseSpec]:
    //   Weeks 1–4   Foundation       3 sets, 10–12 reps, 60–90 s rest
    //   Weeks 5–8   Intensification  4 sets, 8–10 reps, 50–75 s rest, drop set last
    //   Weeks 9–12  Metabolic Peak   4 sets + supersets, 40–60 s rest, longer cardio
    //
    // Knee-safety rules baked in (we keep squats/lunges/deadlift, joint-friendly):
    //   • Goblet / box squat over barbell back squat · sit back to a box.
    //   • Reverse lunges (not forward / jumping) to keep shear off the knee.
    //   • Hinge work (RDL) is hip-dominant; depth always controlled.
    //
    // Cardio is a LISS finisher (incline walk) on every training day; weeks 9–12
    // swap to HIIT bike intervals for a stronger metabolic kick. On the 2 non-
    // training days, the plan asks for a 30-45 min walk (8 000-10 000 steps).

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

    /** Plank hold time scales gently across the three phases. */
    private fun plankSeconds(p: PhaseSpec): String = when (p.name) {
        "Foundation" -> "30–40 s"
        "Intensification" -> "40–50 s"
        else -> "50–60 s"
    }

    // ─── Day templates · 5-day split ─────────────────────────────────────────────
    //
    // Day 1 Push · Day 2 Pull · Day 3 Legs · Day 4 Shoulders & Arms · Day 5 Full Body.
    // 6 working movements each (plus warm-up + cardio finisher) for high fat-loss
    // volume. Exercise names match musclewiki.com so "View Example" works.

    private fun pushDay(week: Int): WorkoutDay {
        val p = phaseFor(week)
        return WorkoutDay(
            weekNumber = week, dayNumber = 1,
            name = "Push · Chest, Shoulders & Triceps",
            type = "Strength", focus = "Chest · Shoulders · Triceps",
            estimatedMinutes = 65,
            caloriesBurned = 500 + p.cardioKcal,
            exercises = listOf(
                warmUpTreadmill(5),
                mobility(),
                Exercise("Barbell Bench Press", p.compoundSets, p.compoundReps, p.restCompoundSec,
                    "Chest", "Plant feet · pinch shoulder blades · bar to lower chest · controlled descent",
                    difficulty = "Advanced"),
                Exercise("Incline Dumbbell Press", p.compoundSets, p.compoundReps, p.restCompoundSec,
                    "Upper Chest", "Bench at 30° · press up and slightly in · full ROM at the bottom · don't bounce",
                    difficulty = "Medium"),
                Exercise("Dumbbell Shoulder Press", p.compoundSets, p.compoundReps, p.restCompoundSec,
                    "Shoulders", "Seated · neutral grip · elbows 30° forward · stop just shy of lockout",
                    difficulty = "Medium"),
                Exercise("Cable Chest Fly", p.isoSets, p.isoReps, p.restIsoSec,
                    "Chest", "Slight elbow bend · meet in front of hips · 1 s squeeze",
                    difficulty = "Medium"),
                Exercise("Dumbbell Lateral Raise", p.isoSets, p.isoReps, p.restIsoSec,
                    "Side Delts", "Lead with the elbows · DBs slightly forward · pause at shoulder height",
                    modification = mod(p.dropSetNote), difficulty = "Beginner"),
                Exercise("Tricep Pushdown", p.isoSets, p.isoReps, p.restIsoSec,
                    "Triceps", "Elbows pinned to ribs · full extension · slow 2 s eccentric",
                    modification = mod(p.supersetNote), difficulty = "Beginner"),
                phaseCardio(p),
                coolDown()
            )
        )
    }

    private fun pullDay(week: Int): WorkoutDay {
        val p = phaseFor(week)
        return WorkoutDay(
            weekNumber = week, dayNumber = 2,
            name = "Pull · Back & Biceps",
            type = "Strength", focus = "Back · Biceps · Rear Delts",
            estimatedMinutes = 65,
            caloriesBurned = 500 + p.cardioKcal,
            exercises = listOf(
                warmUpTreadmill(5),
                mobility(),
                Exercise("Lat Pulldown", p.compoundSets, p.compoundReps, p.restCompoundSec,
                    "Lats", "Wide grip · pull bar to upper chest · drive elbows down · stretch at top",
                    difficulty = "Medium"),
                Exercise("Seated Cable Row", p.compoundSets, p.compoundReps, p.restCompoundSec,
                    "Mid Back", "V-handle · chest up · drive elbows back · pause + squeeze at the ribs",
                    difficulty = "Medium"),
                Exercise("Dumbbell Row", p.compoundSets, p.compoundReps, p.restCompoundSec,
                    "Lats/Mid Back", "Single arm · brace on bench · pull to the hip · don't twist the torso",
                    difficulty = "Medium"),
                Exercise("Face Pull", p.isoSets, p.isoReps, p.restIsoSec,
                    "Rear Delts/Upper Back", "Rope to forehead · external rotation at the end · feel the rear delts",
                    difficulty = "Beginner"),
                Exercise("Dumbbell Bicep Curl", p.isoSets, p.isoReps, p.restIsoSec,
                    "Biceps", "Elbows still · full stretch at the bottom · 1 s squeeze · no swinging",
                    modification = mod(p.dropSetNote), difficulty = "Beginner"),
                Exercise("Hammer Curl", p.isoSets, p.isoReps, p.restIsoSec,
                    "Biceps/Forearms", "Neutral grip · thumbs up · control the lower",
                    modification = mod(p.supersetNote), difficulty = "Beginner"),
                phaseCardio(p),
                coolDown()
            )
        )
    }

    private fun legsDay(week: Int): WorkoutDay {
        val p = phaseFor(week)
        return WorkoutDay(
            weekNumber = week, dayNumber = 3,
            name = "Legs · Quads, Glutes & Hamstrings",
            type = "Strength", focus = "Quads · Glutes · Hamstrings · Calves",
            estimatedMinutes = 70,
            caloriesBurned = 600 + p.cardioKcal,
            exercises = listOf(
                warmUpTreadmill(6),
                Exercise("Knee Prep Mobility", 1, "5 min", 0, "Mobility",
                    "Banded knee circles · controlled BW squats to box · hip flexor stretch · ankle pumps",
                    difficulty = "Beginner"),
                Exercise("Goblet Squat", p.compoundSets, p.compoundReps, p.restCompoundSec,
                    "Quads/Glutes", "Hold a DB at the chest · sit back to a box at parallel · drive through heels · chest tall",
                    difficulty = "Medium"),
                Exercise("Leg Press", p.compoundSets, p.compoundReps, p.restCompoundSec,
                    "Quads/Glutes", "Feet high & wide · stop at ~90° knee flexion · DON'T round the lower back · drive through heels",
                    difficulty = "Medium"),
                Exercise("Romanian Deadlift", p.compoundSets, p.compoundReps, p.restCompoundSec,
                    "Hamstrings/Glutes", "Soft knees · push hips back · bar slides down thighs · flat back · stop when hamstrings shout",
                    difficulty = "Advanced"),
                Exercise("Leg Extension", p.isoSets, p.isoReps, p.restIsoSec,
                    "Quads", "Lighter than ego says · 2 s pause at the top · stop if it bites the knee cap",
                    difficulty = "Beginner"),
                Exercise("Seated Leg Curl", p.isoSets, p.isoReps, p.restIsoSec,
                    "Hamstrings", "Toes neutral · 2 s squeeze at peak · slow eccentric",
                    modification = mod(p.dropSetNote), difficulty = "Beginner"),
                Exercise("Standing Calf Raise", p.isoSets, "15", p.restIsoSec,
                    "Calves", "Full stretch at the bottom · 1 s squeeze on top · don't bounce",
                    modification = mod(p.supersetNote), difficulty = "Beginner"),
                phaseCardio(p),
                coolDown()
            )
        )
    }

    private fun shouldersArmsDay(week: Int): WorkoutDay {
        val p = phaseFor(week)
        return WorkoutDay(
            weekNumber = week, dayNumber = 4,
            name = "Shoulders & Arms",
            type = "Strength", focus = "Shoulders · Biceps · Triceps · Core",
            estimatedMinutes = 65,
            caloriesBurned = 480 + p.cardioKcal,
            exercises = listOf(
                warmUpTreadmill(5),
                mobility(),
                Exercise("Seated Barbell Shoulder Press", p.compoundSets, p.compoundReps, p.restCompoundSec,
                    "Shoulders", "Back flat on the pad · bar to upper chest · press straight up · keep the ribs down",
                    difficulty = "Advanced"),
                Exercise("Dumbbell Lateral Raise", p.isoSets, p.isoReps, p.restIsoSec,
                    "Side Delts", "Lead with the elbows · DBs slightly forward · pause at shoulder height",
                    difficulty = "Beginner"),
                Exercise("Reverse Pec Deck", p.isoSets, p.isoReps, p.restIsoSec,
                    "Rear Delts", "Arms slightly bent · open wide · pause at end range",
                    difficulty = "Beginner"),
                Exercise("EZ Bar Curl", p.isoSets, p.isoReps, p.restIsoSec,
                    "Biceps", "Elbows tucked · full stretch at the bottom · don't swing · 1 s squeeze",
                    difficulty = "Medium"),
                Exercise("Overhead Tricep Extension", p.isoSets, p.isoReps, p.restIsoSec,
                    "Triceps", "Cable or DB · elbows tight to the head · full stretch at the bottom · slow extension",
                    modification = mod(p.dropSetNote), difficulty = "Beginner"),
                Exercise("Cable Crunch", p.isoSets, p.isoReps, p.restIsoSec,
                    "Abs", "Round the spine down towards the knees · exhale hard · slow return",
                    modification = mod(p.supersetNote), difficulty = "Beginner"),
                phaseCardio(p),
                coolDown()
            )
        )
    }

    private fun fullBodyDay(week: Int): WorkoutDay {
        val p = phaseFor(week)
        // Longest cardio of the week — Friday gets the strongest metabolic finish.
        val longCardio = phaseCardio(p)
        return WorkoutDay(
            weekNumber = week, dayNumber = 5,
            name = "Full Body Conditioning",
            type = "Strength", focus = "Full Body · Core · Conditioning",
            estimatedMinutes = 70,
            caloriesBurned = 560 + p.cardioKcal,
            exercises = listOf(
                warmUpTreadmill(6),
                mobility(),
                Exercise("Barbell Hip Thrust", p.compoundSets, p.compoundReps, p.restCompoundSec,
                    "Glutes", "Bar over the hips · chin tucked · drive hips to the ceiling · 1 s squeeze at the top",
                    difficulty = "Medium"),
                Exercise("Incline Dumbbell Press", p.compoundSets, p.compoundReps, p.restCompoundSec,
                    "Upper Chest", "Bench at 30° · press up and slightly in · full ROM · don't bounce",
                    difficulty = "Medium"),
                Exercise("Lat Pulldown", p.compoundSets, p.compoundReps, p.restCompoundSec,
                    "Lats", "Wide grip · pull bar to upper chest · drive elbows down · stretch at top",
                    difficulty = "Medium"),
                Exercise("Reverse Lunge", p.compoundSets, "10/leg", p.restCompoundSec,
                    "Quads/Glutes", "DBs in hand · step BACK (kinder on the knee) · front shin vertical · push through the front heel",
                    modification = mod(p.dropSetNote), difficulty = "Medium"),
                Exercise("Front Plank", p.isoSets, plankSeconds(p), p.restIsoSec,
                    "Core", "Forearms down · squeeze glutes · ribs down · don't let the hips sag",
                    difficulty = "Beginner"),
                Exercise("Hanging Knee Raise", p.isoSets, p.isoReps, p.restIsoSec,
                    "Abs", "Curl the pelvis up · control the lower · no swinging · lie on the floor if the bar is too much",
                    modification = mod(p.supersetNote), difficulty = "Beginner"),
                longCardio,
                coolDown()
            )
        )
    }

    val workoutPlan: List<WorkoutDay> = (1..12).flatMap { week ->
        listOf(
            pushDay(week), pullDay(week), legsDay(week),
            shouldersArmsDay(week), fullBodyDay(week)
        )
    }

    val motivationalTips = listOf(
        "💪 Don't stop until you're proud.",
        "🍕 Balance is key: Train hard, eat smart.",
        "💧 70% of your muscles are water. Drink up!",
        "⚡ You're one workout away from a good mood.",
        "💡 Focused on Quads? Keep the weight on your mid-foot/heels.",
        "💡 Obliques are built with rotation. Control every twist.",
        "🦵 Knee feeling cranky? Drop the depth, not the workout.",
        "🏋️ A heavier lift today is a lighter body tomorrow.",
        "⏱ Short rests + heavy weights = the fastest fat loss.",
        "🥩 Eat protein every 3–4 hours · muscle stays, fat goes.",
        "📉 You don't out-train a bad diet — track the calories.",
        "💊 Creatine: 5 g daily, every day, with any meal. Consistency > timing.",
        "🥗 Cheap, simple, repeated. That's how you lose weight.",
        "🚶 Off-day walk: 30–45 min · aim 8 000–10 000 steps. The walks are where the fat goes.",
        "📅 5 lifts a week + 2 walk days. Hit the sessions, protect the 2 rest days.",
        "🎯 The target isn't abs in 3 months. It's 115 → ~105 kg by September, muscle intact.",
        "🔁 Every muscle trained twice a week — that's the volume that drives fat loss + shape."
    )
}
