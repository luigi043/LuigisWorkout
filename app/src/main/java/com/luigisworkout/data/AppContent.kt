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
    val supermarket: String,    // Continente, Pingo Doce, Lidl, Qualquer
    val tags: List<String> = emptyList()
)

data class Exercise(
    val name: String,
    val sets: Int,
    val reps: String,           // "10", "10-15", "30 seg", "AMRAP"
    val restSeconds: Int,
    val muscleGroup: String,
    val description: String,
    val modification: String = ""
)

data class WorkoutDay(
    val weekNumber: Int,
    val dayNumber: Int,
    val name: String,
    val type: String,           // "Força", "Cardio", "HIIT", "Recuperação"
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
    val supermarket: String = "Qualquer"
)

// ─── RECIPES ───────────────────────────────────────────────────────────────────

object AppContent {

    val recipes = listOf(

        // ── PEQUENOS-ALMOÇOS ──────────────────────────────────────────────────

        Recipe(
            id = "pb_01",
            name = "Papas de Aveia com Banana e Mel",
            mealType = "breakfast",
            calories = 340,
            protein = 12f, carbs = 58f, fat = 6f,
            prepMinutes = 5,
            ingredients = listOf(
                "60g de flocos de aveia (Continente/Pingo Doce)",
                "250ml de leite meio-gordo ou vegetal",
                "1 banana média",
                "1 colher de chá de mel",
                "1 pitada de canela"
            ),
            steps = listOf(
                "Coloca os flocos de aveia e o leite num tacho.",
                "Aquece em lume médio durante 4-5 minutos, mexendo sempre.",
                "Corta a banana às rodelas e coloca por cima.",
                "Rega com mel e polvilha com canela.",
                "Serve imediatamente."
            ),
            supermarket = "Continente",
            tags = listOf("rápido", "económico", "proteína")
        ),

        Recipe(
            id = "pb_02",
            name = "Torradas com Ovos Mexidos e Tomate",
            mealType = "breakfast",
            calories = 380,
            protein = 22f, carbs = 35f, fat = 14f,
            prepMinutes = 10,
            ingredients = listOf(
                "2 fatias de pão de forma integral (Pingo Doce)",
                "3 ovos médios",
                "1 tomate pequeno",
                "Sal, pimenta e orégãos a gosto",
                "1 fio de azeite"
            ),
            steps = listOf(
                "Tosta o pão.",
                "Bate os ovos com sal e pimenta.",
                "Aquece o azeite numa frigideira e junta os ovos.",
                "Mexe suavemente em lume brando até ficarem cremosos.",
                "Corta o tomate às rodelas.",
                "Coloca os ovos por cima das torradas e o tomate ao lado."
            ),
            supermarket = "Pingo Doce",
            tags = listOf("proteína", "saciante")
        ),

        Recipe(
            id = "pb_03",
            name = "Iogurte Grego com Frutos Vermelhos e Granola",
            mealType = "breakfast",
            calories = 290,
            protein = 18f, carbs = 38f, fat = 6f,
            prepMinutes = 3,
            ingredients = listOf(
                "200g de iogurte grego natural (Lidl - Milbona)",
                "100g de frutos vermelhos congelados (descongelados)",
                "30g de granola simples",
                "1 colher de chá de mel (opcional)"
            ),
            steps = listOf(
                "Coloca o iogurte numa taça.",
                "Distribui os frutos vermelhos por cima.",
                "Polvilha com granola.",
                "Adiciona mel se desejado."
            ),
            supermarket = "Lidl",
            tags = listOf("sem cozinha", "rápido", "probióticos")
        ),

        // ── ALMOÇOS ──────────────────────────────────────────────────────────

        Recipe(
            id = "al_01",
            name = "Frango Grelhado com Arroz e Brócolos",
            mealType = "lunch",
            calories = 520,
            protein = 45f, carbs = 52f, fat = 12f,
            prepMinutes = 25,
            ingredients = listOf(
                "150g de peito de frango (Continente)",
                "80g de arroz agulha cru",
                "200g de brócolos frescos ou congelados",
                "Sumo de 1/2 limão",
                "Alho, sal, pimenta, azeite",
                "Ervas aromáticas a gosto"
            ),
            steps = listOf(
                "Tempera o frango com alho, limão, sal e pimenta. Deixa marinar 10 min.",
                "Coze o arroz conforme indicado na embalagem.",
                "Coze os brócolos no vapor ou em água durante 8 minutos.",
                "Grelha o frango 6-7 minutos de cada lado.",
                "Serve tudo junto com um fio de azeite."
            ),
            supermarket = "Continente",
            tags = listOf("alta proteína", "completo", "low-fat")
        ),

        Recipe(
            id = "al_02",
            name = "Sopa de Legumes com Ovo Escalfado",
            mealType = "lunch",
            calories = 280,
            protein = 16f, carbs = 30f, fat = 8f,
            prepMinutes = 20,
            ingredients = listOf(
                "2 cenouras",
                "1 courgette",
                "2 batatas médias",
                "1 cebola",
                "2 ovos",
                "Sal, azeite, coentros ou salsa",
                "1 cubo de caldo de legumes (Pingo Doce)"
            ),
            steps = listOf(
                "Descasca e corta todos os legumes em cubos.",
                "Refoga a cebola em azeite durante 3 minutos.",
                "Junta os restantes legumes, cobre com água e adiciona o cubo de caldo.",
                "Coze 15 minutos até ficarem tenros.",
                "Tritura metade da sopa para ficar cremosa.",
                "Deixa ferver suavemente e parte os ovos diretamente para a sopa.",
                "Escalfa 4 minutos. Serve com ervas frescas."
            ),
            supermarket = "Pingo Doce",
            tags = listOf("reconfortante", "low calorie", "económico")
        ),

        Recipe(
            id = "al_03",
            name = "Massa Integral com Atum e Tomate",
            mealType = "lunch",
            calories = 490,
            protein = 35f, carbs = 62f, fat = 9f,
            prepMinutes = 15,
            ingredients = listOf(
                "80g de massa integral (Lidl)",
                "1 lata de atum ao natural (120g)",
                "200g de tomates frescos ou 1/2 lata de tomate pelado",
                "1 dente de alho",
                "Azeite, sal, pimenta, orégãos",
                "Salsa fresca opcional"
            ),
            steps = listOf(
                "Coze a massa em água salgada al dente.",
                "Refoga o alho picado em azeite 1 minuto.",
                "Junta o tomate e deixa apurar 8 minutos.",
                "Adiciona o atum escorrido e mistura bem.",
                "Incorpora a massa cozida e envolve.",
                "Serve com orégãos e salsa."
            ),
            supermarket = "Lidl",
            tags = listOf("ómega-3", "proteína", "rápido")
        ),

        Recipe(
            id = "al_04",
            name = "Salada de Grão com Atum e Legumes",
            mealType = "lunch",
            calories = 420,
            protein = 32f, carbs = 45f, fat = 10f,
            prepMinutes = 10,
            ingredients = listOf(
                "1 lata de grão cozido (400g, Continente)",
                "1 lata de atum ao natural",
                "1 pepino",
                "1 tomate grande",
                "1/4 de cebola roxa",
                "Azeite, vinagre, sal, pimenta",
                "Salsa ou coentros"
            ),
            steps = listOf(
                "Escorre e lava bem o grão.",
                "Corta todos os legumes em cubos pequenos.",
                "Mistura grão, atum escorrido e legumes.",
                "Tempera com azeite, vinagre, sal e pimenta.",
                "Adiciona as ervas e serve frio."
            ),
            supermarket = "Continente",
            tags = listOf("sem cozinha", "fibra", "saciante")
        ),

        // ── JANTARES ──────────────────────────────────────────────────────────

        Recipe(
            id = "jt_01",
            name = "Salmão no Forno com Batata-Doce e Espinafres",
            mealType = "dinner",
            calories = 550,
            protein = 42f, carbs = 40f, fat = 18f,
            prepMinutes = 35,
            ingredients = listOf(
                "150g de filete de salmão (Pingo Doce)",
                "200g de batata-doce",
                "100g de espinafres frescos ou congelados",
                "Limão, alho, azeite",
                "Sal, pimenta, paprika fumada",
                "Endro (dill) a gosto"
            ),
            steps = listOf(
                "Pré-aquece o forno a 200°C.",
                "Corta a batata-doce em cubos, tempera com azeite, paprika e sal.",
                "Assa a batata-doce 20 minutos.",
                "Tempera o salmão com limão, alho, sal e pimenta.",
                "Junta o salmão à assadeira e assa mais 15 minutos.",
                "Refoga os espinafres em azeite com alho 3 minutos.",
                "Serve tudo junto com endro."
            ),
            supermarket = "Pingo Doce",
            tags = listOf("ómega-3", "anti-inflamatório", "premium")
        ),

        Recipe(
            id = "jt_02",
            name = "Frango com Legumes ao Forno",
            mealType = "dinner",
            calories = 430,
            protein = 40f, carbs = 28f, fat = 16f,
            prepMinutes = 45,
            ingredients = listOf(
                "2 coxas de frango sem pele (Lidl)",
                "2 cenouras",
                "1 courgette",
                "1 pimento vermelho",
                "1 cebola",
                "Alho, azeite, ervas de Provence",
                "Sal, pimenta, sumo de limão"
            ),
            steps = listOf(
                "Pré-aquece o forno a 190°C.",
                "Corta todos os legumes em tiras ou cubos.",
                "Tempera o frango com alho, limão, sal, pimenta e ervas.",
                "Coloca tudo numa assadeira, rega com azeite.",
                "Assa 40-45 minutos, virando o frango a meio.",
                "Serve diretamente da assadeira."
            ),
            supermarket = "Lidl",
            tags = listOf("one pan", "económico", "família")
        ),

        Recipe(
            id = "jt_03",
            name = "Sopa Creme de Abóbora e Gengibre",
            mealType = "dinner",
            calories = 200,
            protein = 4f, carbs = 32f, fat = 6f,
            prepMinutes = 25,
            ingredients = listOf(
                "500g de abóbora manteiga (Continente)",
                "1 cebola",
                "1 cenoura",
                "1 cm de gengibre fresco",
                "400ml de leite de coco light",
                "Caldo de legumes, azeite, sal, pimenta"
            ),
            steps = listOf(
                "Refoga cebola e gengibre em azeite 3 minutos.",
                "Junta abóbora e cenoura cortadas, cobre com caldo.",
                "Coze 20 minutos até tudo ficar muito macio.",
                "Tritura até obter um creme liso.",
                "Adiciona o leite de coco e aquece 2 minutos.",
                "Rectifica sal e serve com pimenta preta."
            ),
            supermarket = "Continente",
            tags = listOf("detox", "low calorie", "inverno", "vegan")
        ),

        // ── LANCHES ──────────────────────────────────────────────────────────

        Recipe(
            id = "sn_01",
            name = "Maçã com Manteiga de Amendoim",
            mealType = "snack",
            calories = 200,
            protein = 5f, carbs = 25f, fat = 9f,
            prepMinutes = 2,
            ingredients = listOf(
                "1 maçã média",
                "1 colher de sopa de manteiga de amendoim natural (Lidl)"
            ),
            steps = listOf(
                "Corta a maçã às fatias.",
                "Serve com a manteiga de amendoim para mergulhar."
            ),
            supermarket = "Lidl",
            tags = listOf("sem cozinha", "fibra", "gordura saudável")
        ),

        Recipe(
            id = "sn_02",
            name = "Ovos Cozidos com Sal e Pimenta",
            mealType = "snack",
            calories = 155,
            protein = 13f, carbs = 1f, fat = 11f,
            prepMinutes = 12,
            ingredients = listOf(
                "2 ovos grandes (Pingo Doce)",
                "Sal grosso e pimenta preta"
            ),
            steps = listOf(
                "Coze os ovos em água a ferver durante 10 minutos.",
                "Arrefece em água fria, descasca.",
                "Tempera com sal e pimenta e serve."
            ),
            supermarket = "Pingo Doce",
            tags = listOf("proteína", "fácil", "meal prep")
        ),

        Recipe(
            id = "sn_03",
            name = "Batido de Proteína Caseiro",
            mealType = "snack",
            calories = 250,
            protein = 20f, carbs = 28f, fat = 4f,
            prepMinutes = 3,
            ingredients = listOf(
                "200ml de leite meio-gordo",
                "1 banana congelada",
                "2 colheres de sopa de aveia",
                "1 colher de sopa de cacau em pó sem açúcar (Lidl)",
                "Mel ou stevia a gosto"
            ),
            steps = listOf(
                "Coloca todos os ingredientes no liquidificador.",
                "Tritura até ficar homogéneo.",
                "Serve imediatamente."
            ),
            supermarket = "Lidl",
            tags = listOf("pós-treino", "sem pó de proteína", "económico")
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
                name = "Corpo Completo A",
                type = "Força",
                estimatedMinutes = 35,
                caloriesBurned = 200 + (week - 1) * 15,
                exercises = listOf(
                    Exercise("Agachamento", 3, "${8 + week * 2}", 60, "Pernas", "Pés à largura dos ombros, desce até 90°"),
                    Exercise("Flexões (joelhos se necessário)", 3, "${6 + week * 2}", 60, "Peito", "Corpo em linha reta, desce até peito quase tocar o chão", "Joelhos no chão se difícil"),
                    Exercise("Afundos alternados", 3, "${8 + week * 2} cada", 60, "Pernas", "Passo largo à frente, joelho traseiro quase no chão"),
                    Exercise("Prancha", 3, "${20 + week * 5} seg", 45, "Core", "Corpo recto, não deixar a anca cair"),
                    Exercise("Superman", 3, "12", 45, "Costas", "Deita de barriga para baixo, levanta braços e pernas simultaneamente"),
                    Exercise("Cadeira imaginária", 3, "${30 + week * 5} seg", 45, "Pernas", "Costas na parede, coxas paralelas ao chão")
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
                    Exercise("Jumping Jacks", 3, "30 seg", 30, "Cardio", "Saltos abrindo pernas e braços em simultâneo", "Marcha no lugar se articulações sensíveis"),
                    Exercise("Mountain Climbers", 3, "30 seg", 45, "Core/Cardio", "Posição de prancha, alterna pernas rapidamente"),
                    Exercise("Crunches", 3, "${12 + week * 2}", 45, "Abdominais", "Mãos atrás da cabeça, eleva apenas os ombros"),
                    Exercise("Bicicleta abdominal", 3, "20 cada lado", 45, "Abdominais", "Cotovelo ao joelho oposto, alternado"),
                    Exercise("Burpees (modificado)", 3, "${5 + week}", 60, "Cardio Total", "Agacha, apoia mãos, recua pernas, avança, levanta"),
                    Exercise("Elevação de pernas deitado", 3, "${10 + week * 2}", 45, "Abdominais baixos", "Deita, mãos debaixo dos glúteos, eleva as pernas retas")
                )
            ))

            // Day 3 - Full Body B
            plan.add(WorkoutDay(
                weekNumber = week, dayNumber = 3,
                name = "Corpo Completo B",
                type = "Força",
                estimatedMinutes = 35,
                caloriesBurned = 200 + (week - 1) * 15,
                exercises = listOf(
                    Exercise("Ponte de glúteos", 3, "${12 + week * 2}", 45, "Glúteos", "Deita, joelhos dobrados, eleva a anca ao máximo"),
                    Exercise("Flexões inclinadas (mãos na cadeira)", 3, "${8 + week * 2}", 60, "Peito superior", "Mãos em superfície elevada"),
                    Exercise("Agachamento sumô", 3, "${10 + week * 2}", 45, "Pernas/Glúteos", "Pernas abertas em V, pés virados para fora"),
                    Exercise("Triceps dips (cadeira)", 3, "${8 + week * 2}", 60, "Triceps", "Mãos na cadeira atrás, dobra e estica os cotovelos"),
                    Exercise("Prancha lateral", 3, "${15 + week * 5} seg cada", 45, "Core", "Suporta o peso num antebraço e pé lateral"),
                    Exercise("Good Morning", 3, "12", 45, "Costas baixas", "Em pé, mãos na nuca, inclina o tronco para a frente")
                )
            ))
        }

        // WEEK 5-8: Progression (4 days/week - add HIIT)
        for (week in 5..8) {
            val wk = week - 4 // 1..4

            plan.add(WorkoutDay(
                weekNumber = week, dayNumber = 1,
                name = "Força Superior",
                type = "Força",
                estimatedMinutes = 40,
                caloriesBurned = 220 + wk * 15,
                exercises = listOf(
                    Exercise("Flexões padrão", 4, "${10 + wk * 2}", 60, "Peito", "Forma perfeita"),
                    Exercise("Pike Push-Up", 3, "${8 + wk * 2}", 60, "Ombros", "Anca no ar, flexão a baixar a cabeça ao chão"),
                    Exercise("Triceps dips", 3, "${10 + wk * 2}", 60, "Triceps", "Cadeira ou sofá"),
                    Exercise("Flexões diamante", 3, "${8 + wk}", 60, "Triceps/Peito", "Mãos juntas formando diamante"),
                    Exercise("Prancha com toque de ombros", 3, "20 cada", 45, "Core", "Toca no ombro oposto em prancha sem rodar a anca"),
                    Exercise("Rotações de tronco sentado", 3, "15 cada lado", 30, "Core", "Sentado, pés no ar, roda o tronco com braços à frente")
                )
            ))

            plan.add(WorkoutDay(
                weekNumber = week, dayNumber = 2,
                name = "HIIT Queima Gordura",
                type = "HIIT",
                estimatedMinutes = 25,
                caloriesBurned = 320 + wk * 20,
                exercises = listOf(
                    Exercise("Aquecimento - Marcha elevada", 1, "3 min", 0, "Aquecimento", "Marcha no lugar, elevando joelhos"),
                    Exercise("Burpees", 4, "40 seg / 20 seg descanso", 20, "HIIT", "Máxima intensidade"),
                    Exercise("Jump Squats", 4, "40 seg / 20 seg descanso", 20, "HIIT", "Agacha e salta explosivamente"),
                    Exercise("Mountain Climbers rápidos", 4, "40 seg / 20 seg descanso", 20, "HIIT", "O mais rápido possível"),
                    Exercise("High Knees", 4, "40 seg / 20 seg descanso", 20, "HIIT", "Joelhos acima da anca em corrida no lugar"),
                    Exercise("Arrefecimento - Alongamentos", 1, "3 min", 0, "Recuperação", "Quadríceps, isquiotibiais, peito, costas")
                )
            ))

            plan.add(WorkoutDay(
                weekNumber = week, dayNumber = 3,
                name = "Força Inferior",
                type = "Força",
                estimatedMinutes = 40,
                caloriesBurned = 230 + wk * 15,
                exercises = listOf(
                    Exercise("Agachamento profundo", 4, "${12 + wk * 2}", 60, "Pernas", "Abaixo dos 90°"),
                    Exercise("Afundos bulgáros (pé na cadeira)", 3, "${10 + wk} cada", 60, "Glúteos/Pernas", "Pé traseiro na cadeira, desce em linha reta"),
                    Exercise("Ponte de glúteos perna única", 3, "${10 + wk} cada", 45, "Glúteos", "Eleva uma perna enquanto a outra empurra o chão"),
                    Exercise("Step up (cadeira/degrau)", 3, "${12 + wk} cada", 45, "Pernas", "Sobe e desce alternando pernas"),
                    Exercise("Agachamento lateral", 3, "10 cada", 45, "Adutores/Glúteos", "Afasta uma perna lateralmente dobrado"),
                    Exercise("Elevação de gémeos", 3, "${15 + wk * 3}", 30, "Gémeos", "Em pé na ponta dos pés, sobe e desce")
                )
            ))

            plan.add(WorkoutDay(
                weekNumber = week, dayNumber = 4,
                name = "Core + Mobilidade",
                type = "Cardio",
                estimatedMinutes = 30,
                caloriesBurned = 180 + wk * 10,
                exercises = listOf(
                    Exercise("Prancha", 3, "${45 + wk * 10} seg", 45, "Core", "Máximo tempo em posição perfeita"),
                    Exercise("Prancha lateral alternada", 3, "${25 + wk * 5} seg cada", 30, "Core lateral", "Alterna lado sem tocar o chão"),
                    Exercise("Dead Bug", 3, "8 cada lado", 45, "Core profundo", "Deita, braço e perna oposta descem juntos"),
                    Exercise("Rolo de coluna", 3, "10", 30, "Mobilidade", "Deitado, rola a coluna suavemente"),
                    Exercise("Yoga Flow (Cão/Cobra)", 2, "5 cada", 30, "Flexibilidade", "Flui entre posição de cão virado para baixo e cobra"),
                    Exercise("Respiração de caixa", 1, "5 min", 0, "Recuperação", "Inspira 4 seg, segura 4, expira 4, segura 4")
                )
            ))
        }

        // WEEK 9-12: Advanced (5 days/week)
        for (week in 9..12) {
            val wk = week - 8 // 1..4

            plan.add(WorkoutDay(
                weekNumber = week, dayNumber = 1,
                name = "Push (Peito, Ombros, Triceps)",
                type = "Força",
                estimatedMinutes = 45,
                caloriesBurned = 260 + wk * 15,
                exercises = listOf(
                    Exercise("Flexões padrão", 4, "${15 + wk}", 60, "Peito", "Explosão subindo, controlado descendo"),
                    Exercise("Flexões inclinadas", 3, "${12 + wk}", 60, "Peito superior", "Pés na cadeira ou sofá"),
                    Exercise("Pike Push-Up", 4, "${10 + wk}", 60, "Ombros", "Anca alta, cabeça desce ao chão"),
                    Exercise("Flexões diamante", 3, "${10 + wk}", 60, "Triceps", "Mãos em diamante"),
                    Exercise("Triceps dips + pause", 3, "${10 + wk}", 60, "Triceps", "2 segundos de pausa no fundo"),
                    Exercise("Prancha com extensão de braço", 3, "8 cada", 45, "Core/Ombros", "Em prancha, estende um braço à frente")
                )
            ))

            plan.add(WorkoutDay(
                weekNumber = week, dayNumber = 2,
                name = "Pull (Costas, Biceps)",
                type = "Força",
                estimatedMinutes = 40,
                caloriesBurned = 230 + wk * 15,
                exercises = listOf(
                    Exercise("Superman isométrico", 4, "${30 + wk * 5} seg", 45, "Costas baixas", "Mantém a posição elevada"),
                    Exercise("Remada inclinada (mochila com peso)", 4, "${10 + wk}", 60, "Costas", "Usa mochila com livros como peso"),
                    Exercise("Flexões reverse (mãos viradas)", 3, "${8 + wk}", 60, "Biceps/Costas", "Mãos apontam para ti, trabalha biceps"),
                    Exercise("Prancha com remo alternado", 3, "10 cada", 60, "Core/Costas", "Em prancha, puxa cotovelo para o lado"),
                    Exercise("Good Morning profundo", 3, "${12 + wk}", 45, "Costas/Isquiotibiais", "Maior amplitude de movimento"),
                    Exercise("Hold isométrico inclinado", 3, "${20 + wk * 5} seg", 45, "Costas", "Segura a posição de inclinação máxima")
                )
            ))

            plan.add(WorkoutDay(
                weekNumber = week, dayNumber = 3,
                name = "HIIT Metabólico",
                type = "HIIT",
                estimatedMinutes = 30,
                caloriesBurned = 380 + wk * 20,
                exercises = listOf(
                    Exercise("Aquecimento dinâmico", 1, "3 min", 0, "Aquecimento", "Rotações de articulações + marcha elevada"),
                    Exercise("Burpee com salto", 5, "40 seg / 15 seg descanso", 15, "HIIT total", "Com salto no fim e explosão"),
                    Exercise("Agachamento com salto", 5, "40 seg / 15 seg descanso", 15, "HIIT pernas", "Aterra suave com joelhos dobrados"),
                    Exercise("Flexão + Mountain Climber", 4, "40 seg / 20 seg descanso", 20, "HIIT superior/core", "1 flexão + 2 mountain climbers, repete"),
                    Exercise("Sprint no lugar", 5, "30 seg / 15 seg descanso", 15, "Cardio", "Máxima velocidade possível"),
                    Exercise("Arrefecimento", 1, "4 min", 0, "Recuperação", "Alongamentos estáticos completos")
                )
            ))

            plan.add(WorkoutDay(
                weekNumber = week, dayNumber = 4,
                name = "Pernas + Glúteos",
                type = "Força",
                estimatedMinutes = 45,
                caloriesBurned = 270 + wk * 15,
                exercises = listOf(
                    Exercise("Agachamento profundo + pausa 2 seg", 4, "${12 + wk}", 60, "Pernas", "Pausa no fundo"),
                    Exercise("Afundo búlgaro", 4, "${10 + wk} cada", 60, "Glúteos/Pernas", "Pé traseiro na cadeira"),
                    Exercise("Ponte glúteos + elevação perna única", 4, "${12 + wk} cada", 45, "Glúteos", "Máxima contração no topo"),
                    Exercise("Leg Curl natural (par de sofá)", 3, "${6 + wk}", 60, "Isquiotibiais", "Joelhos presos no sofá, desce controlado"),
                    Exercise("Elevação de pernas lateral", 3, "${15 + wk} cada", 30, "Abdutores", "Deitado de lado, eleva a perna superior"),
                    Exercise("Panturrilha unilateral", 3, "${15 + wk} cada", 30, "Gémeos", "Apoia num pé, sobe e desce")
                )
            ))

            plan.add(WorkoutDay(
                weekNumber = week, dayNumber = 5,
                name = "Core + Recuperação Ativa",
                type = "Recuperação",
                estimatedMinutes = 30,
                caloriesBurned = 130 + wk * 5,
                exercises = listOf(
                    Exercise("Prancha progressiva", 3, "${60 + wk * 10} seg", 45, "Core", "Sem ceder a posição"),
                    Exercise("Dead Bug avançado", 3, "10 cada", 45, "Core profundo", "Movimento lento e controlado"),
                    Exercise("Hollow Body Hold", 3, "${20 + wk * 5} seg", 45, "Core", "Lombar colada ao chão, pernas e braços a 45°"),
                    Exercise("Yoga Flow Completo", 2, "10 min", 0, "Mobilidade", "Flui entre posturas, respira fundo"),
                    Exercise("Rolo de espuma / auto-massagem", 1, "5 min", 0, "Recuperação", "Rola os grupos musculares trabalhados"),
                    Exercise("Meditação / Respiração", 1, "5 min", 0, "Mental", "Respiração profunda, relaxamento total")
                )
            ))
        }

        return plan
    }

    // ─── FOOD DATABASE ─────────────────────────────────────────────────────────

    val foodDatabase = listOf(
        // Proteínas
        FoodItem("Peito de Frango cozido", 165, 31f, 0f, 3.6f, "100g", "Continente"),
        FoodItem("Ovo inteiro", 155, 13f, 1.1f, 11f, "100g", "Qualquer"),
        FoodItem("Atum ao natural (lata)", 116, 25f, 0f, 1f, "100g", "Pingo Doce"),
        FoodItem("Salmão grelhado", 208, 29f, 0f, 10f, "100g", "Pingo Doce"),
        FoodItem("Iogurte Grego Natural", 97, 9f, 3.6f, 5f, "100g", "Lidl"),
        FoodItem("Queijo Flamengo magro", 200, 20f, 1f, 13f, "100g", "Continente"),
        FoodItem("Frango costas cozido", 239, 26f, 0f, 15f, "100g", "Lidl"),

        // Hidratos
        FoodItem("Arroz branco cozido", 130, 2.7f, 28f, 0.3f, "100g", "Continente"),
        FoodItem("Arroz integral cozido", 123, 2.7f, 25f, 1f, "100g", "Pingo Doce"),
        FoodItem("Massa cozida", 158, 6f, 31f, 0.9f, "100g", "Lidl"),
        FoodItem("Batata cozida", 87, 1.9f, 20f, 0.1f, "100g", "Qualquer"),
        FoodItem("Batata-doce cozida", 90, 2f, 21f, 0.1f, "100g", "Continente"),
        FoodItem("Pão integral", 247, 8.5f, 41f, 3.4f, "100g", "Pingo Doce"),
        FoodItem("Flocos de Aveia", 389, 17f, 66f, 7f, "100g", "Lidl"),
        FoodItem("Grão cozido (lata)", 164, 8.9f, 27f, 2.6f, "100g", "Continente"),
        FoodItem("Feijão vermelho cozido", 127, 8.7f, 22f, 0.5f, "100g", "Pingo Doce"),
        FoodItem("Lentilhas cozidas", 116, 9f, 20f, 0.4f, "100g", "Lidl"),

        // Legumes (baixas calorias)
        FoodItem("Brócolos cozidos", 35, 2.4f, 7f, 0.4f, "100g", "Continente"),
        FoodItem("Espinafres crus", 23, 2.9f, 3.6f, 0.4f, "100g", "Pingo Doce"),
        FoodItem("Cenoura crua", 41, 0.9f, 10f, 0.2f, "100g", "Qualquer"),
        FoodItem("Tomate cru", 18, 0.9f, 3.9f, 0.2f, "100g", "Qualquer"),
        FoodItem("Courgette cozida", 17, 1.2f, 3.5f, 0.1f, "100g", "Qualquer"),
        FoodItem("Alface", 15, 1.4f, 2.9f, 0.2f, "100g", "Qualquer"),
        FoodItem("Pepino", 16, 0.7f, 3.6f, 0.1f, "100g", "Qualquer"),
        FoodItem("Pimento vermelho", 31, 1f, 6f, 0.3f, "100g", "Qualquer"),
        FoodItem("Cebola", 40, 1.1f, 9.3f, 0.1f, "100g", "Qualquer"),
        FoodItem("Abóbora cozida", 20, 0.7f, 4.9f, 0.1f, "100g", "Continente"),

        // Fruta
        FoodItem("Banana", 89, 1.1f, 23f, 0.3f, "100g", "Qualquer"),
        FoodItem("Maçã", 52, 0.3f, 14f, 0.2f, "100g", "Qualquer"),
        FoodItem("Laranja", 47, 0.9f, 12f, 0.1f, "100g", "Qualquer"),
        FoodItem("Morangos", 32, 0.7f, 7.7f, 0.3f, "100g", "Qualquer"),
        FoodItem("Uvas", 69, 0.7f, 18f, 0.2f, "100g", "Qualquer"),

        // Gorduras saudáveis
        FoodItem("Azeite", 884, 0f, 0f, 100f, "100ml", "Qualquer"),
        FoodItem("Manteiga de Amendoim", 588, 25f, 20f, 50f, "100g", "Lidl"),
        FoodItem("Abacate", 160, 2f, 9f, 15f, "100g", "Continente"),

        // Lacticínios
        FoodItem("Leite meio-gordo", 46, 3.2f, 4.8f, 1.5f, "100ml", "Qualquer"),
        FoodItem("Queijo cottage", 98, 11f, 3.4f, 4.3f, "100g", "Pingo Doce")
    )

    // ─── MOTIVATIONAL TIPS ─────────────────────────────────────────────────────

    val motivationalTips = listOf(
        "💪 Cada treino conta! Mesmo 20 minutos fazem diferença.",
        "🥗 Come devagar — o teu cérebro demora 20 min a registar a saciedade.",
        "💧 Bebe água antes de cada refeição — reduz a fome em 13%.",
        "😴 O sono é metade do sucesso! Dorme 7-8 horas por noite.",
        "🚶 10.000 passos/dia queimam até 400 calorias extra!",
        "🍳 Prepara as refeições ao domingo — evitas decisões más durante a semana.",
        "📏 Mede-te mensalmente, não diariamente — o peso varia naturalmente.",
        "🫁 Respira durante o treino! Expira no esforço.",
        "🏠 Treinar em casa poupa tempo e dinheiro — consiste é a chave!",
        "🌿 Proteína em cada refeição = saciedade prolongada + músculo preservado.",
        "🎯 Foca-te no progresso, não na perfeição. Um dia mau não estraga tudo.",
        "🛒 Faz lista de compras — evitas compras por impulso no Continente ou Pingo Doce.",
        "⏰ Come de 3h em 3h — mantém o metabolismo ativo.",
        "🏋️ Consistência bate intensidade. Melhor treinar 4x/semana do que 1x muito intenso.",
        "🍫 Permitir-se um snack ao fim de semana evita a compulsão!"
    )
}