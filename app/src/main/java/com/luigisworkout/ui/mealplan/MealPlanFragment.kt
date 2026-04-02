package com.luigisworkout.ui.mealplan

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.luigisworkout.R
import com.luigisworkout.data.AppContent
import com.luigisworkout.data.Recipe
import com.luigisworkout.data.database.entities.MealLog
import com.luigisworkout.databinding.FragmentMealPlanBinding
import com.luigisworkout.ui.logmeal.LogMealActivity
import com.luigisworkout.viewmodel.AppViewModelFactory
import com.luigisworkout.viewmodel.MealPlanViewModel
import java.text.SimpleDateFormat
import java.util.*

class MealPlanFragment : Fragment() {

    private var _binding: FragmentMealPlanBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MealPlanViewModel by activityViewModels {
        AppViewModelFactory(requireActivity().application)
    }

    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var mealLogAdapter: MealLogAdapter
    private var currentTab = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMealPlanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTabs()
        setupRecyclerViews()
        setupObservers()
        setupClickListeners()
    }

    private fun setupTabs() {
        val tabs = listOf("🌅 Pequeno-Almoço", "☀️ Almoço", "🌙 Jantar", "🍎 Lanche", "📋 Registo")
        tabs.forEach { title ->
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(title))
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                currentTab = tab?.position ?: 0
                updateContent()
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupRecyclerViews() {
        recipeAdapter = RecipeAdapter { recipe -> showRecipeDetail(recipe) }
        binding.rvRecipes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRecipes.adapter = recipeAdapter

        mealLogAdapter = MealLogAdapter { meal -> confirmDeleteMeal(meal) }
        binding.rvMealLog.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMealLog.adapter = mealLogAdapter
    }

    private fun setupObservers() {
        viewModel.todayMeals.observe(viewLifecycleOwner) { meals ->
            mealLogAdapter.submitList(meals)
            updateCalorieSummary(meals)
        }

        viewModel.todayCalories.observe(viewLifecycleOwner) { calories ->
            binding.tvTodayKcal.text = "${calories ?: 0} kcal hoje"
        }

        updateContent()
    }

    private fun updateContent() {
        val showRecipes = currentTab < 4
        binding.rvRecipes.visibility = if (showRecipes) View.VISIBLE else View.GONE
        binding.rvMealLog.visibility = if (!showRecipes) View.VISIBLE else View.GONE
        binding.cardMealSummary.visibility = if (!showRecipes) View.VISIBLE else View.GONE

        if (showRecipes) {
            val recipes = when (currentTab) {
                0 -> viewModel.breakfastRecipes
                1 -> viewModel.lunchRecipes
                2 -> viewModel.dinnerRecipes
                3 -> viewModel.snackRecipes
                else -> emptyList()
            }
            recipeAdapter.submitList(recipes)
        }
    }

    private fun updateCalorieSummary(meals: List<MealLog>) {
        val byType = meals.groupBy { it.mealType }
        binding.tvBreakfastKcal.text = "Pequeno-Almoço: ${byType["breakfast"]?.sumOf { it.calories } ?: 0} kcal"
        binding.tvLunchKcal.text = "Almoço: ${byType["lunch"]?.sumOf { it.calories } ?: 0} kcal"
        binding.tvDinnerKcal.text = "Jantar: ${byType["dinner"]?.sumOf { it.calories } ?: 0} kcal"
        binding.tvSnackKcal.text = "Lanches: ${byType["snack"]?.sumOf { it.calories } ?: 0} kcal"
    }

    private fun showRecipeDetail(recipe: Recipe) {
        val message = buildString {
            appendLine("🔥 ${recipe.calories} kcal  |  💪 ${recipe.protein.toInt()}g prot")
            appendLine("⏱ ${recipe.prepMinutes} min  |  🛒 ${recipe.supermarket}")
            appendLine()
            appendLine("📦 Ingredientes:")
            recipe.ingredients.forEach { appendLine("• $it") }
            appendLine()
            appendLine("👨‍🍳 Preparação:")
            recipe.steps.forEachIndexed { i, step -> appendLine("${i + 1}. $step") }
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(recipe.name)
            .setMessage(message)
            .setPositiveButton("📝 Registar Refeição") { _, _ ->
                val intent = Intent(requireContext(), LogMealActivity::class.java).apply {
                    putExtra("food_name", recipe.name)
                    putExtra("calories", recipe.calories)
                    putExtra("protein", recipe.protein)
                    putExtra("carbs", recipe.carbs)
                    putExtra("fat", recipe.fat)
                    putExtra("meal_type", recipe.mealType)
                }
                startActivity(intent)
            }
            .setNegativeButton("Fechar", null)
            .show()
    }

    private fun confirmDeleteMeal(meal: MealLog) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Eliminar Refeição")
            .setMessage("Eliminar '${meal.foodName}'?")
            .setPositiveButton("Eliminar") { _, _ -> viewModel.deleteMeal(meal) }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun setupClickListeners() {
        binding.fabLogMeal.setOnClickListener {
            startActivity(Intent(requireContext(), LogMealActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// ─── RECIPE ADAPTER ────────────────────────────────────────────────────────────

class RecipeAdapter(
    private val onRecipeClick: (Recipe) -> Unit
) : RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    private var recipes = listOf<Recipe>()

    fun submitList(list: List<Recipe>) {
        recipes = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvRecipeName)
        val tvCalories: TextView = view.findViewById(R.id.tvRecipeCalories)
        val tvTime: TextView = view.findViewById(R.id.tvRecipeTime)
        val tvSupermarket: TextView = view.findViewById(R.id.tvSupermarket)
        val chipProtein: Chip = view.findViewById(R.id.chipProtein)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.tvName.text = recipe.name
        holder.tvCalories.text = "${recipe.calories} kcal"
        holder.tvTime.text = "⏱ ${recipe.prepMinutes} min"
        holder.tvSupermarket.text = "🛒 ${recipe.supermarket}"
        holder.chipProtein.text = "${recipe.protein.toInt()}g prot"
        holder.itemView.setOnClickListener { onRecipeClick(recipe) }
    }

    override fun getItemCount() = recipes.size
}

// ─── MEAL LOG ADAPTER ─────────────────────────────────────────────────────────

class MealLogAdapter(
    private val onDelete: (MealLog) -> Unit
) : RecyclerView.Adapter<MealLogAdapter.ViewHolder>() {

    private var meals = listOf<MealLog>()

    fun submitList(list: List<MealLog>) {
        meals = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvFood: TextView = view.findViewById(R.id.tvFoodName)
        val tvCalories: TextView = view.findViewById(R.id.tvMealCalories)
        val tvType: TextView = view.findViewById(R.id.tvMealType)
        val tvMacros: TextView = view.findViewById(R.id.tvMacros)
        val btnDelete: View = view.findViewById(R.id.btnDeleteMeal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_meal_log, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val meal = meals[position]
        holder.tvFood.text = meal.foodName
        holder.tvCalories.text = "${meal.calories} kcal"
        holder.tvType.text = when (meal.mealType) {
            "breakfast" -> "🌅 Pequeno-Almoço"
            "lunch" -> "☀️ Almoço"
            "dinner" -> "🌙 Jantar"
            "snack" -> "🍎 Lanche"
            else -> meal.mealType
        }
        holder.tvMacros.text = "P: ${meal.protein.toInt()}g | C: ${meal.carbs.toInt()}g | G: ${meal.fat.toInt()}g"
        holder.btnDelete.setOnClickListener { onDelete(meal) }
    }

    override fun getItemCount() = meals.size
}