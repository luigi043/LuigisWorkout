package com.luigisworkout.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        setupObservers()
        setupClickListeners()
        viewModel.refreshMacros()
    }

    private fun setupGreeting() {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val greeting = when {
            hour < 12 -> "Bom dia"
            hour < 18 -> "Boa tarde"
            else -> "Boa noite"
        }
        binding.tvGreeting.text = "$greeting, Luigi! 💪"

        val sdf = SimpleDateFormat("EEEE, d 'de' MMMM", Locale("pt", "PT"))
        binding.tvDate.text = sdf.format(Date()).replaceFirstChar { it.uppercase() }
    }

    private fun setupObservers() {
        viewModel.todayCalories.observe(viewLifecycleOwner) { calories ->
            val kcal = calories ?: 0
            val goal = 1800
            binding.tvCaloriesConsumed.text = "$kcal kcal"
            binding.tvCaloriesGoal.text = "/ $goal kcal"
            binding.progressCalories.max = goal
            binding.progressCalories.progress = kcal.coerceAtMost(goal)
            val remaining = (goal - kcal).coerceAtLeast(0)
            binding.tvCaloriesRemaining.text = "$remaining kcal restantes"
        }

        viewModel.macros.observe(viewLifecycleOwner) { (protein, carbs, fat) ->
            binding.tvProtein.text = "${protein.toInt()}g\nProteína"
            binding.tvCarbs.text = "${carbs.toInt()}g\nHidratos"
            binding.tvFat.text = "${fat.toInt()}g\nGordura"
        }

        viewModel.todayGoal.observe(viewLifecycleOwner) { goal ->
            val glasses = goal?.waterGlasses ?: 0
            val waterGoal = goal?.waterGoal ?: 8
            binding.tvWaterCount.text = "$glasses / $waterGoal 💧"
            binding.progressWater.max = waterGoal
            binding.progressWater.progress = glasses
        }

        viewModel.todayWorkout.observe(viewLifecycleOwner) { workout ->
            if (workout != null && workout.completed) {
                binding.cardTodayWorkout.setBackgroundResource(R.drawable.bg_card_success)
                binding.tvWorkoutStatus.text = "✅ Treino Concluído!"
                binding.tvWorkoutName.text = workout.workoutName
                binding.tvWorkoutDetail.text = "${workout.totalMinutes} min · ${workout.caloriesBurned} kcal queimadas"
            } else {
                binding.tvWorkoutStatus.text = "Treino de Hoje"
                binding.tvWorkoutName.text = "Pronto para treinar?"
                binding.tvWorkoutDetail.text = "Vai ao plano de treino para começar"
            }
        }

        viewModel.completedWorkouts.observe(viewLifecycleOwner) { count ->
            binding.tvStreakCount.text = "$count"
        }

        viewModel.latestWeight.observe(viewLifecycleOwner) { weight ->
            if (weight != null) {
                binding.tvCurrentWeight.text = String.format("%.1f kg", weight.weightKg)
                binding.tvBmi.text = "IMC: ${String.format("%.1f", weight.bmi)}"
            } else {
                binding.tvCurrentWeight.text = "-- kg"
                binding.tvBmi.text = "Regista o teu peso"
            }
        }

        // Motivational tip
        val tips = listOf(
            "💪 Cada treino conta!",
            "💧 Bebe água antes de cada refeição.",
            "🥗 Proteína em cada refeição = saciedade!",
            "😴 Dorme 7-8 horas para recuperar melhor.",
            "🚶 10.000 passos/dia faz grande diferença!"
        )
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        binding.tvDailyTip.text = tips[dayOfYear % tips.size]
    }

    private fun setupClickListeners() {
        binding.btnLogMeal.setOnClickListener {
            startActivity(Intent(requireContext(), LogMealActivity::class.java))
        }

        binding.btnLogWeight.setOnClickListener {
            startActivity(Intent(requireContext(), LogWeightActivity::class.java))
        }

        binding.btnAddWater.setOnClickListener {
            viewModel.addWater()
        }

        binding.btnRemoveWater.setOnClickListener {
            viewModel.removeWater()
        }

        binding.cardTodayWorkout.setOnClickListener {
            findNavController().navigate(R.id.workoutFragment)
        }

        binding.btnGoToMeals.setOnClickListener {
            findNavController().navigate(R.id.mealPlanFragment)
        }

        binding.btnGoToProgress.setOnClickListener {
            findNavController().navigate(R.id.progressFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}