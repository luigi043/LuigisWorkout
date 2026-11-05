package com.luigisworkout.ui.mealplan

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.luigisworkout.R
import com.luigisworkout.data.Recipe
import com.luigisworkout.data.database.entities.MealLog
import com.luigisworkout.databinding.FragmentMealPlanBinding
import com.luigisworkout.luigiApp
import com.luigisworkout.ui.logmeal.LogMealActivity
import com.luigisworkout.viewmodel.MealPlanViewModel
import com.luigisworkout.viewmodel.appViewModelFactory

class MealPlanFragment : Fragment() {

    private var _binding: FragmentMealPlanBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MealPlanViewModel by activityViewModels {
        appViewModelFactory(requireActivity().luigiApp)
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
        
        // Entrance Animation
        binding.rvRecipes.layoutAnimation = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_fall_down)
    }

    private fun setupTabs() {
        val tabs = listOf(
            getString(R.string.tab_breakfast),
            getString(R.string.tab_lunch),
            getString(R.string.tab_dinner),
            getString(R.string.tab_snack),
            getString(R.string.tab_drinks),
            getString(R.string.tab_history_label)
        )
        tabs.forEach { title ->
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText(title))
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                currentTab = tab?.position ?: 0
                updateContent()
                binding.rvRecipes.scheduleLayoutAnimation()
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
        }

        viewModel.todayCalories.observe(viewLifecycleOwner) { calories ->
            binding.tvTodayKcal.text = "${calories ?: 0} kcal"
            // Simple animation for the number
            binding.tvTodayKcal.animate().scaleX(1.1f).scaleY(1.1f).setDuration(200).withEndAction {
                binding.tvTodayKcal.animate().scaleX(1f).scaleY(1f).setDuration(200).start()
            }.start()
        }

        updateContent()
    }

    private fun updateContent() {
        // Tabs 0..4 are recipe categories, tab 5 is history.
        val showRecipes = currentTab < 5
        binding.rvRecipes.visibility = if (showRecipes) View.VISIBLE else View.GONE
        binding.historyContainer.visibility = if (!showRecipes) View.VISIBLE else View.GONE

        if (showRecipes) {
            val recipes = when (currentTab) {
                0 -> viewModel.breakfastRecipes
                1 -> viewModel.lunchRecipes
                2 -> viewModel.dinnerRecipes
                3 -> viewModel.snackRecipes
                4 -> viewModel.drinkRecipes
                else -> emptyList()
            }
            recipeAdapter.submitList(recipes)
        }
    }

    private fun showRecipeDetail(recipe: Recipe) {
        val message = buildString {
            appendLine("🔥 ${recipe.calories} kcal  |  💪 ${recipe.protein.toInt()}g Protein")
            if (recipe.suggestedTime.isNotEmpty()) appendLine("🕐 Best around ${recipe.suggestedTime}")
            appendLine("⏱ ${recipe.prepMinutes} min prep time")
            appendLine()
            val costSuffix = if (recipe.approxCostEuro > 0f) " · ≈ €%.2f / serving".format(recipe.approxCostEuro) else ""
            appendLine("🛒 Buy at: ${recipe.supermarket}$costSuffix")
            if (recipe.supplementNote.isNotEmpty()) {
                appendLine()
                appendLine("💊 ${recipe.supplementNote}")
            }
            appendLine()
            appendLine("📦 Ingredients:")
            recipe.ingredients.forEach { appendLine("  • $it") }
            appendLine()
            appendLine("👨‍🍳 Preparation:")
            recipe.steps.forEachIndexed { i, step -> appendLine("  ${i + 1}. $step") }
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(recipe.name)
            .setMessage(message)
            .setPositiveButton(R.string.add_to_diary) { _, _ ->
                // Add ingredients to shopping list
                recipe.ingredients.forEach { ingredient ->
                    viewModel.addShoppingItem(ingredient, supermarket = recipe.supermarket)
                }

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
            .setNegativeButton(R.string.close, null)
            .show()
    }

    private fun confirmDeleteMeal(meal: MealLog) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_entry_title)
            .setMessage(getString(R.string.confirm_delete_meal_msg, meal.foodName))
            .setPositiveButton(R.string.delete) { _, _ -> viewModel.deleteMeal(meal) }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun setupClickListeners() {
        binding.fabLogMeal.setOnClickListener {
            it.animate().rotationBy(90f).setDuration(300).start()
            startActivity(Intent(requireContext(), LogMealActivity::class.java))
        }
        
        binding.btnQuickLog.setOnClickListener {
            startActivity(Intent(requireContext(), LogMealActivity::class.java))
        }

        binding.btnGoToShopping.setOnClickListener {
            findNavController().navigate(R.id.shoppingListFragment)
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
) : ListAdapter<Recipe, RecipeAdapter.ViewHolder>(DIFF) {

    object DIFF : DiffUtil.ItemCallback<Recipe>() {
        // Recipes don't have a Room id — use name as the stable key (unique in AppContent).
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe) = oldItem.name == newItem.name
        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe) = oldItem == newItem
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvRecipeName)
        val tvCalories: TextView = view.findViewById(R.id.tvRecipeCalories)
        val tvTime: TextView = view.findViewById(R.id.tvRecipeTime)
        val tvSupermarket: TextView = view.findViewById(R.id.tvSupermarket)
        val chipProtein: Chip = view.findViewById(R.id.chipProtein)
        val chipCarbs: Chip = view.findViewById(R.id.chipCarbs)
        val chipFat: Chip = view.findViewById(R.id.chipFat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = getItem(position)
        holder.tvName.text = recipe.name
        holder.tvCalories.text = "${recipe.calories} kcal"
        // Prepend the suggested meal time (e.g. "🕐 13:00 · ⏱ 20 min") so the user
        // can see at a glance which slot of the day a recipe fits into.
        holder.tvTime.text = if (recipe.suggestedTime.isNotEmpty())
            "🕐 ${recipe.suggestedTime} · ⏱ ${recipe.prepMinutes} min"
        else
            "⏱ ${recipe.prepMinutes} min"
        // If we know a per-serving cost, surface it next to the supermarket.
        holder.tvSupermarket.text = if (recipe.approxCostEuro > 0f)
            "🛒 ${recipe.supermarket} · €%.2f".format(recipe.approxCostEuro)
        else
            "🛒 ${recipe.supermarket}"
        holder.chipProtein.text = "${recipe.protein.toInt()}g P"
        holder.chipCarbs.text = "${recipe.carbs.toInt()}g C"
        holder.chipFat.text = "${recipe.fat.toInt()}g F"

        holder.itemView.setOnClickListener {
            it.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).withEndAction {
                it.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
                onRecipeClick(recipe)
            }.start()
        }
    }
}

// ─── MEAL LOG ADAPTER ─────────────────────────────────────────────────────────

class MealLogAdapter(
    private val onDelete: (MealLog) -> Unit
) : ListAdapter<MealLog, MealLogAdapter.ViewHolder>(DIFF) {

    object DIFF : DiffUtil.ItemCallback<MealLog>() {
        override fun areItemsTheSame(oldItem: MealLog, newItem: MealLog) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: MealLog, newItem: MealLog) = oldItem == newItem
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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
        val meal = getItem(position)
        holder.tvFood.text = meal.foodName
        holder.tvCalories.text = "${meal.calories} kcal"
        holder.tvType.text = when (meal.mealType.lowercase()) {
            "breakfast" -> "🌅 " + holder.itemView.context.getString(R.string.tab_breakfast)
            "lunch" -> "☀️ " + holder.itemView.context.getString(R.string.tab_lunch)
            "dinner" -> "🌙 " + holder.itemView.context.getString(R.string.tab_dinner)
            "snack" -> "🍎 " + holder.itemView.context.getString(R.string.tab_snack)
            "drink" -> "🥤 " + holder.itemView.context.getString(R.string.tab_drinks)
            else -> meal.mealType.uppercase()
        }
        holder.tvMacros.text = "P: ${meal.protein.toInt()}g | C: ${meal.carbs.toInt()}g | F: ${meal.fat.toInt()}g"
        holder.btnDelete.setOnClickListener { onDelete(meal) }
    }
}
