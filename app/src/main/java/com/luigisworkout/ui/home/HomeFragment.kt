package com.luigisworkout.ui.home

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.GridLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.luigisworkout.R
import com.luigisworkout.databinding.FragmentHomeBinding
import com.luigisworkout.ui.logmeal.LogMealActivity
import com.luigisworkout.ui.logweight.LogWeightActivity
import com.luigisworkout.viewmodel.AppViewModelFactory
import com.luigisworkout.viewmodel.HomeViewModel
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by activityViewModels {
        AppViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupGreeting()
        setupCalendar()
        setupObservers()
        setupClickListeners()
        animateEntrance()
        
        viewModel.refreshMacros()
    }

    private fun setupCalendar() {
        binding.gridCalendar.removeAllViews()

        viewModel.recentWeekWorkouts.observe(viewLifecycleOwner) { workouts ->
            binding.gridCalendar.removeAllViews()
            
            val calendar = Calendar.getInstance()
            // We want to show the last 28 days
            calendar.add(Calendar.DAY_OF_YEAR, -27)
            
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val completedDates = workouts.filter { it.completed }.map { it.date }.toSet()

            for (i in 0 until 28) {
                val currentDate = dateFormat.format(calendar.time)
                val isCompleted = completedDates.contains(currentDate)
                
                val dayView = View(requireContext())
                val size = resources.getDimensionPixelSize(R.dimen.spacing_md)
                val params = GridLayout.LayoutParams().apply {
                    width = size
                    height = size
                    setMargins(4, 4, 4, 4)
                }
                dayView.layoutParams = params
                
                dayView.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_calendar_day)
                if (isCompleted) {
                    dayView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.primary))
                } else {
                    dayView.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.surface_variant))
                }
                
                binding.gridCalendar.addView(dayView)
                calendar.add(Calendar.DAY_OF_YEAR, 1)
            }
        }
    }

    private fun setupGreeting() {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val greetingRes = when {
            hour < 12 -> R.string.greeting_morning
            hour < 18 -> R.string.greeting_afternoon
            else -> R.string.greeting_evening
        }
        binding.tvGreeting.text = getString(greetingRes)

        val sdf = SimpleDateFormat("EEEE, MMMM d", Locale.ENGLISH)
        binding.tvDate.text = sdf.format(Date())
    }

    private fun animateEntrance() {
        binding.headerLayout.alpha = 0f
        binding.headerLayout.translationY = -50f
        binding.caloriesCard.alpha = 0f
        binding.caloriesCard.translationY = 50f
        
        binding.headerLayout.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(600)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()

        binding.caloriesCard.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(800)
            .setStartDelay(200)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
    }

    private fun setupObservers() {
        viewModel.todayCalories.observe(viewLifecycleOwner) { calories ->
            val kcal = calories ?: 0
            val goal = 2000 // Sample goal
            binding.tvCaloriesConsumed.text = kcal.toString()
            binding.progressCalories.max = goal
            binding.progressCalories.setProgress(kcal, true)
            
            val remaining = (goal - kcal).coerceAtLeast(0)
            binding.tvCaloriesRemaining.text = getString(R.string.calories_remaining_format, remaining)
        }

        viewModel.macros.observe(viewLifecycleOwner) { (protein, carbs, fat) ->
            binding.tvProtein.text = "${protein.toInt()}g"
            binding.tvCarbs.text = "${carbs.toInt()}g"
            binding.tvFat.text = "${fat.toInt()}g"
        }

        viewModel.todayGoal.observe(viewLifecycleOwner) { goal ->
            val glasses = goal?.waterGlasses ?: 0
            binding.tvWaterCount.text = "$glasses/8"
        }

        viewModel.todayWorkout.observe(viewLifecycleOwner) { workout ->
            if (workout != null && workout.completed) {
                binding.tvWorkoutName.text = getString(R.string.workout_done)
                binding.tvWorkoutDetail.text = getString(R.string.burned_today_format, workout.caloriesBurned)
            } else {
                binding.tvWorkoutName.text = getString(R.string.ready_to_train)
                binding.tvWorkoutDetail.text = getString(R.string.check_daily_plan)
            }
        }

        viewModel.latestWeight.observe(viewLifecycleOwner) { weight ->
            if (weight != null) {
                binding.tvCurrentWeight.text = String.format("%.1f", weight.weightKg)
            } else {
                binding.tvCurrentWeight.text = "--.-"
            }
        }

        // Motivational tip from AppContent
        val tips = listOf(
            "💪 Don't stop until you're proud.",
            "💧 Hydration is the key to recovery.",
            "🥗 Fuel your body with the right macros.",
            "🔥 One more rep makes the difference!",
            "⚡ Discipline beats motivation every time."
        )
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        binding.tvDailyTip.text = tips[dayOfYear % tips.size]
    }

    private fun setupClickListeners() {
        binding.btnLogMeal.setOnClickListener {
            it.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100).withEndAction {
                it.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
                startActivity(Intent(requireContext(), LogMealActivity::class.java))
            }.start()
        }

        binding.btnGoToWorkout.setOnClickListener {
            findNavController().navigate(R.id.workoutFragment)
        }

        binding.btnAddWater.setOnClickListener {
            it.animate().rotationBy(45f).setDuration(200).start()
            viewModel.addWater()
        }

        binding.btnRemoveWater.setOnClickListener {
            viewModel.removeWater()
        }

        binding.btnLogWeight.setOnClickListener {
            startActivity(Intent(requireContext(), LogWeightActivity::class.java))
        }

        binding.cardTodayWorkout.setOnClickListener {
            findNavController().navigate(R.id.workoutFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
