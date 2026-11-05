package com.luigisworkout.ui.logmeal

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.luigisworkout.R
import com.luigisworkout.data.AppContent
import com.luigisworkout.data.FoodItem
import com.luigisworkout.data.database.entities.MealLog
import com.luigisworkout.databinding.ActivityLogMealBinding
import com.luigisworkout.luigiApp
import com.luigisworkout.util.AppClock
import com.luigisworkout.viewmodel.LogMealViewModel
import com.luigisworkout.viewmodel.appViewModelFactory

class LogMealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogMealBinding
    private val viewModel: LogMealViewModel by viewModels {
        appViewModelFactory(luigiApp)
    }

    private lateinit var foodSearchAdapter: FoodSearchAdapter
    private val mealTypes = listOf("breakfast", "lunch", "dinner", "snack", "drink")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.log_meal_label)

        setupMealTypeSpinner()
        if (savedInstanceState == null) intent.extras?.let { extras ->
            binding.etFoodName.setText(extras.getString("food_name", ""))
            binding.etCalories.setText(extras.getInt("calories").toString())
            binding.etProtein.setText(extras.getFloat("protein").toString())
            binding.etCarbs.setText(extras.getFloat("carbs").toString())
            binding.etFat.setText(extras.getFloat("fat").toString())

            val mealIndex = mealTypes.indexOf(extras.getString("meal_type"))
            binding.spinnerMealType.setSelection(if (mealIndex >= 0) mealIndex else 1)
        }

        setupFoodSearch()
        setupSaveButton()
        setupObservers()
    }

    private fun setupMealTypeSpinner() {
        val types = listOf(
            "🌅 ${getString(R.string.tab_breakfast)}",
            "☀️ ${getString(R.string.tab_lunch)}",
            "🌙 ${getString(R.string.tab_dinner)}",
            "🍎 ${getString(R.string.tab_snack)}",
            "🥤 ${getString(R.string.tab_drinks)}"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerMealType.adapter = adapter
    }

    private fun setupFoodSearch() {
        foodSearchAdapter = FoodSearchAdapter { food ->
            binding.etFoodName.setText(food.name)
            binding.etCalories.setText(food.calories.toString())
            binding.etProtein.setText(food.protein.toString())
            binding.etCarbs.setText(food.carbs.toString())
            binding.etFat.setText(food.fat.toString())
            binding.rvFoodSearch.visibility = View.GONE
        }

        binding.rvFoodSearch.layoutManager = LinearLayoutManager(this)
        binding.rvFoodSearch.adapter = foodSearchAdapter

        binding.etFoodName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s?.toString()?.trim() ?: ""
                if (query.length >= 2) {
                    val results = AppContent.foodDatabase.filter {
                        it.name.contains(query, ignoreCase = true)
                    }
                    foodSearchAdapter.submitList(results)
                    binding.rvFoodSearch.visibility = if (results.isNotEmpty()) View.VISIBLE else View.GONE
                } else {
                    binding.rvFoodSearch.visibility = View.GONE
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupSaveButton() {
        binding.btnSaveMeal.setOnClickListener {
            val name = binding.etFoodName.text.toString().trim()
            val calories = binding.etCalories.text.toString().trim().toIntOrNull()
            val quantity = binding.etQuantity.text.toString().trim().replace(',', '.').toFloatOrNull()

            if (name.isEmpty()) {
                binding.etFoodName.error = getString(R.string.enter_food_name)
                return@setOnClickListener
            }
            if (calories == null || calories < 0) {
                binding.etCalories.error = getString(R.string.enter_calories)
                return@setOnClickListener
            }
            if (quantity == null || !quantity.isFinite() || quantity <= 0f ||
                calories.toDouble() * quantity > Int.MAX_VALUE
            ) {
                binding.etQuantity.error = getString(R.string.quantity_invalid)
                return@setOnClickListener
            }

            val protein = readMacro(binding.etProtein, quantity) ?: return@setOnClickListener
            val carbs = readMacro(binding.etCarbs, quantity) ?: return@setOnClickListener
            val fat = readMacro(binding.etFat, quantity) ?: return@setOnClickListener
            val log = MealLog(
                date = AppClock.todayString(),
                mealType = mealTypes.getOrElse(binding.spinnerMealType.selectedItemPosition) { "lunch" },
                foodName = name,
                calories = (calories.toDouble() * quantity).toInt(),
                protein = protein,
                carbs = carbs,
                fat = fat,
                quantity = quantity
            )
            viewModel.saveMeal(log)
        }
    }

    private fun readMacro(field: EditText, quantity: Float): Float? {
        val input = field.text.toString().trim()
        if (input.isEmpty()) return 0f
        val total = input.replace(',', '.').toFloatOrNull()?.times(quantity)
        if (total != null && total.isFinite() && total >= 0f) return total
        field.error = getString(R.string.macro_invalid)
        return null
    }

    private fun setupObservers() {
        viewModel.saved.observe(this) { saved ->
            if (saved) {
                Toast.makeText(this, getString(R.string.meal_saved), Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}

// ─── FOOD SEARCH ADAPTER ──────────────────────────────────────────────────────

class FoodSearchAdapter(
    private val onFoodClick: (FoodItem) -> Unit
) : ListAdapter<FoodItem, FoodSearchAdapter.ViewHolder>(DIFF) {

    object DIFF : DiffUtil.ItemCallback<FoodItem>() {
        // FoodItem name is the natural key inside AppContent.foodDatabase.
        override fun areItemsTheSame(oldItem: FoodItem, newItem: FoodItem) = oldItem.name == newItem.name
        override fun areContentsTheSame(oldItem: FoodItem, newItem: FoodItem) = oldItem == newItem
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvFoodSearchName)
        val tvDetails: TextView = view.findViewById(R.id.tvFoodSearchDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food_search, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food = getItem(position)
        holder.tvName.text = food.name
        holder.tvDetails.text = "${food.calories} kcal/${food.unit} | P:${food.protein.toInt()}g C:${food.carbs.toInt()}g F:${food.fat.toInt()}g | ${food.supermarket}"
        holder.itemView.setOnClickListener { onFoodClick(food) }
    }
}
