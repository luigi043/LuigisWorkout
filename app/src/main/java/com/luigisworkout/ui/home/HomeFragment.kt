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
import com.luigisworkout.data.AppContent
import com.luigisworkout.databinding.FragmentHomeBinding
import com.luigisworkout.luigiApp
import com.luigisworkout.ui.logmeal.LogMealActivity
import com.luigisworkout.ui.logweight.LogWeightActivity
import com.luigisworkout.util.AppClock
import com.luigisworkout.viewmodel.HomeViewModel
import com.luigisworkout.viewmodel.WorkoutViewModel
import com.luigisworkout.viewmodel.appViewModelFactory
import java.util.Calendar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by activityViewModels {
        appViewModelFactory(requireActivity().luigiApp)
    }

    // Shared with WorkoutFragment so the Home "Current Plan" card and the workout
    // chips reflect the same week-progress state without duplicating the math.
    private val workoutViewModel: WorkoutViewModel by activityViewModels {
        appViewModelFactory(requireActivity().luigiApp)
    }

    /** Cached so we can skip rebuilding the 28-day calendar grid on identical emissions. */
    private var lastCalendarSignature: Set<String>? = null

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

        // Only animate on cold start — re-entering from another tab shouldn't replay
        // the entrance, which previously flashed every time the user tapped Home.
        if (savedInstanceState == null) animateEntrance()

        viewModel.refreshMacros()
    }

    private fun setupCalendar() {
        viewModel.recentWeekWorkouts.observe(viewLifecycleOwner) { workouts ->
            val completedDates = workouts.asSequence()
                .filter { it.completed }
                .map { it.date }
                .toSet()

            // Skip the rebuild if nothing changed. Saves 28 view inflations / layout passes
            // on every meal/weight/workout-list emission upstream.
            if (completedDates == lastCalendarSignature && binding.gridCalendar.childCount == 28) return@observe
            lastCalendarSignature = completedDates

            renderCalendar(completedDates)
        }
    }

    private fun renderCalendar(completedDates: Set<String>) {
        binding.gridCalendar.removeAllViews()

        val size = resources.getDimensionPixelSize(R.dimen.spacing_md)
        val completedTint = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.primary))
        val emptyTint = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.surface_variant))
        val bg = ContextCompat.getDrawable(requireContext(), R.drawable.bg_calendar_day)

        val calendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -27) }

        repeat(28) {
            val isoDate = AppClock.todayString().let {
                // Cheap one-shot format using the calendar we already advance.
                val y = calendar.get(Calendar.YEAR)
                val m = calendar.get(Calendar.MONTH) + 1
                val d = calendar.get(Calendar.DAY_OF_MONTH)
                "%04d-%02d-%02d".format(y, m, d)
            }
            val dayView = View(requireContext()).apply {
                layoutParams = GridLayout.LayoutParams().apply {
                    width = size
                    height = size
                    setMargins(4, 4, 4, 4)
                }
                background = bg
                backgroundTintList = if (isoDate in completedDates) completedTint else emptyTint
            }
            binding.gridCalendar.addView(dayView)
            calendar.add(Calendar.DAY_OF_YEAR, 1)
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
        binding.tvDate.text = AppClock.longDateHeader()
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
            val goal = 2000 // TODO: pull from DailyGoal once goal UI lands
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
            binding.tvCurrentWeight.text = weight?.weightKg?.let { String.format("%.1f", it) } ?: "--.-"
        }

        // Current Plan card — pulls from WorkoutViewModel so Home and Workout always
        // agree on how far through the week the user is.
        workoutViewModel.userProfile.observe(viewLifecycleOwner) { profile ->
            val week = (profile?.currentWeekNumber ?: 1).coerceIn(1, 12)
            binding.tvHomeWeekLabel.text = getString(R.string.week_format, week)
        }
        workoutViewModel.currentWeekProgress.observe(viewLifecycleOwner) { (done, total) ->
            binding.tvHomeWeekProgress.text = if (total > 0) {
                "$done of $total days done this week"
            } else {
                "Plan loading…"
            }
            binding.progressHomeWeek.max = total.coerceAtLeast(1)
            binding.progressHomeWeek.setProgressCompat(done.coerceIn(0, total), true)
        }

        // Pull the daily tip from AppContent so we have one source of truth, not a
        // local 5-entry copy that drifts out of sync with the real 30-tip rotation.
        val tips = AppContent.motivationalTips
        if (tips.isNotEmpty()) {
            val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
            binding.tvDailyTip.text = tips[dayOfYear % tips.size]
        }
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
