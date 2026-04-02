package com.luigisworkout.ui.workout

import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.luigisworkout.R
import com.luigisworkout.data.Exercise
import com.luigisworkout.data.WorkoutDay
import com.luigisworkout.databinding.FragmentWorkoutBinding
import com.luigisworkout.viewmodel.AppViewModelFactory
import com.luigisworkout.viewmodel.WorkoutViewModel
import java.util.*

class WorkoutFragment : Fragment() {

    private var _binding: FragmentWorkoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WorkoutViewModel by activityViewModels {
        AppViewModelFactory(requireActivity().application)
    }

    private lateinit var exerciseAdapter: ExerciseAdapter
    private var selectedWorkoutDay: WorkoutDay? = null
    private var workoutStartTime: Long = 0L
    private var workoutTimer: CountDownTimer? = null
    private var completedExercises = mutableSetOf<Int>()
    private var isWorkoutActive = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentWorkoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWeekSelector()
        setupExerciseList()
        setupObservers()
        setupClickListeners()
    }

    private fun setupWeekSelector() {
        val weeks = (1..12).map { "Semana $it" }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, weeks)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerWeek.adapter = adapter

        binding.spinnerWeek.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                loadWeekWorkouts(position + 1)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun loadWeekWorkouts(week: Int) {
        val workouts = viewModel.getWorkoutsByWeek(week)
        val dayNames = workouts.map { "${getDayLabel(it.dayNumber)} - ${it.name}" }

        val dayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, dayNames)
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDay.adapter = dayAdapter

        binding.spinnerDay.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (workouts.isNotEmpty() && position < workouts.size) {
                    loadWorkoutDay(workouts[position])
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        if (workouts.isNotEmpty()) loadWorkoutDay(workouts[0])
    }

    private fun getDayLabel(day: Int) = when (day) {
        1 -> "Dia A"
        2 -> "Dia B"
        3 -> "Dia C"
        4 -> "Dia D"
        5 -> "Dia E"
        else -> "Dia $day"
    }

    private fun loadWorkoutDay(day: WorkoutDay) {
        selectedWorkoutDay = day
        completedExercises.clear()
        isWorkoutActive = false

        binding.tvWorkoutTitle.text = day.name
        binding.tvWorkoutType.text = day.type
        binding.tvWorkoutDuration.text = "⏱ ~${day.estimatedMinutes} min"
        binding.tvWorkoutCalories.text = "🔥 ~${day.caloriesBurned} kcal"
        binding.tvExerciseCount.text = "${day.exercises.size} exercícios"

        exerciseAdapter.submitList(day.exercises)

        updateProgress()
        updateStartButton()
    }

    private fun setupExerciseList() {
        exerciseAdapter = ExerciseAdapter { index, completed ->
            if (completed) completedExercises.add(index) else completedExercises.remove(index)
            updateProgress()
        }
        binding.rvExercises.layoutManager = LinearLayoutManager(requireContext())
        binding.rvExercises.adapter = exerciseAdapter
    }

    private fun setupObservers() {
        viewModel.completedCount.observe(viewLifecycleOwner) { count ->
            binding.tvTotalCompleted.text = "$count treinos concluídos"
        }

        viewModel.todayWorkout.observe(viewLifecycleOwner) { log ->
            if (log?.completed == true) {
                binding.btnStartWorkout.text = "✅ Treino Concluído Hoje!"
                binding.btnStartWorkout.isEnabled = false
            }
        }
    }

    private fun updateProgress() {
        val total = selectedWorkoutDay?.exercises?.size ?: 0
        val done = completedExercises.size
        binding.progressWorkout.max = total
        binding.progressWorkout.progress = done
        binding.tvWorkoutProgress.text = "$done / $total exercícios"

        if (done == total && total > 0) {
            binding.btnFinishWorkout.visibility = View.VISIBLE
        }
    }

    private fun updateStartButton() {
        binding.btnStartWorkout.text = "▶ Começar Treino"
        binding.btnStartWorkout.isEnabled = true
        binding.btnFinishWorkout.visibility = View.GONE
        binding.tvElapsedTime.visibility = View.GONE
    }

    private fun setupClickListeners() {
        binding.btnStartWorkout.setOnClickListener {
            if (!isWorkoutActive) startWorkout() else pauseWorkout()
        }

        binding.btnFinishWorkout.setOnClickListener {
            finishWorkout()
        }
    }

    private fun startWorkout() {
        isWorkoutActive = true
        workoutStartTime = System.currentTimeMillis()
        binding.btnStartWorkout.text = "⏸ Pausar"
        binding.tvElapsedTime.visibility = View.VISIBLE
        startTimer()
    }

    private fun pauseWorkout() {
        isWorkoutActive = false
        workoutTimer?.cancel()
        binding.btnStartWorkout.text = "▶ Continuar"
    }

    private fun startTimer() {
        workoutTimer?.cancel()
        workoutTimer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val elapsed = (System.currentTimeMillis() - workoutStartTime) / 1000
                val minutes = elapsed / 60
                val seconds = elapsed % 60
                binding.tvElapsedTime.text = String.format("⏱ %02d:%02d", minutes, seconds)
            }
            override fun onFinish() {}
        }.start()
    }

    private fun finishWorkout() {
        workoutTimer?.cancel()
        val elapsed = ((System.currentTimeMillis() - workoutStartTime) / 1000 / 60).toInt()
            .coerceAtLeast(1)

        val day = selectedWorkoutDay ?: return

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("🎉 Treino Concluído!")
            .setMessage(buildString {
                appendLine("Parabéns, Luigi!")
                appendLine()
                appendLine("✅ ${completedExercises.size}/${day.exercises.size} exercícios")
                appendLine("⏱ $elapsed minutos")
                appendLine("🔥 ~${day.caloriesBurned * elapsed / day.estimatedMinutes} kcal queimadas")
            })
            .setPositiveButton("Guardar") { _, _ ->
                viewModel.logWorkout(day, completedExercises.size, elapsed)
                binding.btnStartWorkout.text = "✅ Treino Concluído Hoje!"
                binding.btnStartWorkout.isEnabled = false
                binding.btnFinishWorkout.visibility = View.GONE
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onDestroyView() {
        workoutTimer?.cancel()
        super.onDestroyView()
        _binding = null
    }
}

// ─── EXERCISE ADAPTER ─────────────────────────────────────────────────────────

class ExerciseAdapter(
    private val onToggle: (Int, Boolean) -> Unit
) : RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {

    private var exercises = listOf<Exercise>()

    fun submitList(list: List<Exercise>) {
        exercises = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox = view.findViewById(R.id.checkExercise)
        val tvName: TextView = view.findViewById(R.id.tvExerciseName)
        val tvSets: TextView = view.findViewById(R.id.tvSetsReps)
        val tvMuscle: TextView = view.findViewById(R.id.tvMuscleGroup)
        val tvDesc: TextView = view.findViewById(R.id.tvExerciseDesc)
        val tvMod: TextView = view.findViewById(R.id.tvModification)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exercise, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ex = exercises[position]
        holder.checkBox.isChecked = false
        holder.tvName.text = ex.name
        holder.tvSets.text = "${ex.sets} séries × ${ex.reps} | ⏱ ${ex.restSeconds}seg descanso"
        holder.tvMuscle.text = "💪 ${ex.muscleGroup}"
        holder.tvDesc.text = ex.description
        if (ex.modification.isNotEmpty()) {
            holder.tvMod.visibility = View.VISIBLE
            holder.tvMod.text = "💡 Modificação: ${ex.modification}"
        } else {
            holder.tvMod.visibility = View.GONE
        }

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            onToggle(position, isChecked)
        }

        holder.itemView.alpha = if (holder.checkBox.isChecked) 0.5f else 1f
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            holder.itemView.alpha = if (isChecked) 0.5f else 1f
            onToggle(position, isChecked)
        }
    }

    override fun getItemCount() = exercises.size
}