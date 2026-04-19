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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.luigisworkout.R
import com.luigisworkout.data.AppContent
import com.luigisworkout.data.FoodItem
import com.luigisworkout.data.database.entities.MealLog
import com.luigisworkout.databinding.ActivityLogMealBinding
import com.luigisworkout.viewmodel.AppViewModelFactory
import com.luigisworkout.viewmodel.LogMealViewModel
import java.text.SimpleDateFormat
import java.util.*

class LogMealActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogMealBinding
    private val viewModel: LogMealViewModel by viewModels {
        AppViewModelFactory(application)
    }

    private lateinit var foodSearchAdapter: FoodSearchAdapter
    private var selectedCalories = 0
    private var selectedProtein = 0f
    private var selectedCarbs = 0f
    private var selectedFat = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.log_meal_label)

        // Pre-fill from recipe intent extras
        intent.extras?.let { extras ->
            binding.etFoodName.setText(extras.getString("food_name", ""))
            selectedCalories = extras.getInt("calories", 0)
            selectedProtein = extras.getFloat("protein", 0f)
            selectedCarbs = extras.getFloat("carbs", 0f)
            selectedFat = extras.getFloat("fat", 0f)
            binding.etCalories.setText(selectedCalories.toString())
            binding.etProtein.setText(selectedProtein.toString())
            binding.etCarbs.setText(selectedCarbs.toString())
            binding.etFat.setText(selectedFat.toString())

            val mealType = extras.getString("meal_type", "lunch")
            binding.spinnerMealType.setSelection(getMealTypeIndex(mealType))
        }

        setupMealTypeSpinner()
        setupFoodSearch()
        setupSaveButton()
        setupObservers()
    }

    private fun getMealTypeIndex(type: String) = when (type) {
        "breakfast" -> 0
        "lunch" -> 1
        "dinner" -> 2
        "snack" -> 3
        else -> 1
    }

    private fun setupMealTypeSpinner() {
        val types = listOf(
            "🌅 ${getString(R.string.tab_breakfast)}",
            "☀️ ${getString(R.string.tab_lunch)}",
            "🌙 ${getString(R.string.tab_dinner)}",
            "🍎 ${getString(R.string.tab_snack)}"
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
            val calories = binding.etCalories.text.toString().toIntOrNull() ?: 0
            val protein = binding.etProtein.text.toString().toFloatOrNull() ?: 0f
            val carbs = binding.etCarbs.text.toString().toFloatOrNull() ?: 0f
            val fat = binding.etFat.text.toString().toFloatOrNull() ?: 0f
            val quantity = binding.etQuantity.text.toString().toFloatOrNull() ?: 1f

            if (name.isEmpty()) {
                binding.etFoodName.error = getString(R.string.enter_food_name)
                return@setOnClickListener
            }
            if (calories == 0) {
                binding.etCalories.error = getString(R.string.enter_calories)
                return@setOnClickListener
            }

            val mealType = when (binding.spinnerMealType.selectedItemPosition) {
                0 -> "breakfast"
                1 -> "lunch"
                2 -> "dinner"
                3 -> "snack"
                else -> "lunch"
            }

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val log = MealLog(
                date = dateFormat.format(Date()),
                mealType = mealType,
                foodName = name,
                calories = (calories * quantity).toInt(),
                protein = protein * quantity,
                carbs = carbs * quantity,
                fat = fat * quantity,
                quantity = quantity
            )
            viewModel.saveMeal(log)
        }
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
) : RecyclerView.Adapter<FoodSearchAdapter.ViewHolder>() {

    private var items = listOf<FoodItem>()

    fun submitList(list: List<FoodItem>) {
        items = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvFoodSearchName)
        val tvDetails: TextView = view.findViewById(R.id.tvFoodSearchDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food_search, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food = items[position]
        holder.tvName.text = food.name
        holder.tvDetails.text = "${food.calories} kcal/${food.unit} | P:${food.protein.toInt()}g C:${food.carbs.toInt()}g F:${food.fat.toInt()}g | ${food.supermarket}"
        holder.itemView.setOnClickListener { onFoodClick(food) }
    }

    override fun getItemCount() = items.size
}
