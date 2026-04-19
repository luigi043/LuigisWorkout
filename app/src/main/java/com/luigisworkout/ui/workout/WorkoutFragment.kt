package com.luigisworkout.ui.workout

import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.view.animation.AnimationUtils
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
        
        // Initial animation
        binding.root.layoutAnimation = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_fall_down)
    }

    private fun setupWeekSelector() {
        val weeks = (1..12).map { "Week $it" }
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
        val dayNames = workouts.map { "Day ${it.dayNumber}: ${it.name}" }

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

    private fun loadWorkoutDay(day: WorkoutDay) {
        selectedWorkoutDay = day
        completedExercises.clear()
        isWorkoutActive = false

        binding.tvWorkoutTitle.text = day.name
        binding.tvWorkoutType.text = day.type
        binding.tvWorkoutDuration.text = "⏱ ~${day.estimatedMinutes} min"
        binding.tvWorkoutCalories.text = "🔥 ~${day.caloriesBurned} kcal"
        binding.tvExerciseCount.text = "${day.exercises.size} exercises"

        exerciseAdapter.submitList(day.exercises)
        binding.rvExercises.scheduleLayoutAnimation()

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
            binding.tvTotalCompleted.text = "$count workouts completed in total"
        }

        viewModel.todayWorkout.observe(viewLifecycleOwner) { log ->
            if (log?.completed == true) {
                binding.btnStartWorkout.text = "Completed Today ✅"
                binding.btnStartWorkout.isEnabled = false
            }
        }
    }

    private fun updateProgress() {
        val total = selectedWorkoutDay?.exercises?.size ?: 0
        val done = completedExercises.size
        binding.progressWorkout.max = total
        binding.progressWorkout.setProgress(done, true)
        binding.tvWorkoutProgress.text = "$done / $total exercises"

        if (done == total && total > 0) {
            binding.btnFinishWorkout.visibility = View.VISIBLE
            binding.btnFinishWorkout.animate().alpha(1f).setDuration(300).start()
        } else {
            binding.btnFinishWorkout.visibility = View.GONE
        }
    }

    private fun updateStartButton() {
        binding.btnStartWorkout.text = "START WORKOUT"
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
        binding.btnStartWorkout.text = "PAUSE"
        binding.tvElapsedTime.visibility = View.VISIBLE
        binding.tvElapsedTime.alpha = 0f
        binding.tvElapsedTime.animate().alpha(1f).setDuration(500).start()
        startTimer()
    }

    private fun pauseWorkout() {
        isWorkoutActive = false
        workoutTimer?.cancel()
        binding.btnStartWorkout.text = "RESUME"
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
        val elapsed = ((System.currentTimeMillis() - workoutStartTime) / 1000 / 60).toInt().coerceAtLeast(1)
        val day = selectedWorkoutDay ?: return

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Workout Complete! 🎉")
            .setMessage("Awesome job, Luigi!\n\n✅ ${completedExercises.size}/${day.exercises.size} Exercises\n⏱ $elapsed Minutes\n🔥 Approx. ${day.caloriesBurned * elapsed / day.estimatedMinutes} kcal")
            .setPositiveButton("Save Result") { _, _ ->
                viewModel.logWorkout(day, completedExercises.size, elapsed)
                binding.btnStartWorkout.text = "Done for Today! ✅"
                binding.btnStartWorkout.isEnabled = false
                binding.btnFinishWorkout.visibility = View.GONE
            }
            .setNegativeButton("Discard", null)
            .show()
    }

    override fun onDestroyView() {
        workoutTimer?.cancel()
        super.onDestroyView()
        _binding = null
    }
}

// ─── IMPROVED EXERCISE ADAPTER ────────────────────────────────────────────────

class ExerciseAdapter(
    private val onToggle: (Int, Boolean) -> Unit
) : RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {

    private var exercises = listOf<Exercise>()

    fun submitList(list: List<Exercise>) {
        exercises = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: View = view.findViewById(R.id.exerciseCard)
        val checkBox: CheckBox = view.findViewById(R.id.checkExercise)
        val tvName: TextView = view.findViewById(R.id.tvExerciseName)
        val tvMuscle: TextView = view.findViewById(R.id.tvMuscleGroup)
        val tvDifficulty: TextView = view.findViewById(R.id.tvDifficulty)
        val tvSets: TextView = view.findViewById(R.id.tvSets)
        val tvReps: TextView = view.findViewById(R.id.tvReps)
        val tvRest: TextView = view.findViewById(R.id.tvRest)
        val tvDesc: TextView = view.findViewById(R.id.tvExerciseDesc)
        val statusView: View = view.findViewById(R.id.viewStatus)
        val btnProgress: Button = view.findViewById(R.id.btnProgress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exercise, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ex = exercises[position]
        
        holder.tvName.text = ex.name
        holder.tvMuscle.text = ex.muscleGroup.uppercase()
        holder.tvDifficulty.text = ex.difficulty
        holder.tvSets.text = ex.sets.toString()
        holder.tvReps.text = ex.reps
        holder.tvRest.text = "${ex.restSeconds}s"
        holder.tvDesc.text = ex.description
        
        // Reset state
        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = false
        holder.statusView.visibility = View.INVISIBLE
        holder.card.alpha = 1.0f

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            onToggle(position, isChecked)
            if (isChecked) {
                holder.card.animate().alpha(0.6f).scaleX(0.98f).scaleY(0.98f).setDuration(200).start()
                holder.statusView.visibility = View.VISIBLE
            } else {
                holder.card.animate().alpha(1.0f).scaleX(1.0f).scaleY(1.0f).setDuration(200).start()
                holder.statusView.visibility = View.INVISIBLE
            }
        }
        
        holder.btnProgress.setOnClickListener {
            MaterialAlertDialogBuilder(holder.itemView.context)
                .setTitle("Track Progress: ${ex.name}")
                .setMessage("Feature coming soon! You'll be able to see your weight history for this exercise.")
                .setPositiveButton("OK", null)
                .show()
        }
    }

    override fun getItemCount() = exercises.size
}
