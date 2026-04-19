package com.luigisworkout.ui.workout

import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
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
    private lateinit var historyAdapter: HistoryAdapter
    private var selectedWorkoutDay: WorkoutDay? = null
    private var completedExercises = mutableSetOf<Int>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentWorkoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTabs()
        setupWeekSelector()
        setupExerciseList()
        setupHistoryList()
        setupObservers()
        setupClickListeners()

        // Restore UI if workout is already running
        if (viewModel.isWorkoutActive) {
            binding.tvElapsedTime.visibility = View.VISIBLE
            binding.btnStartWorkout.text = getString(R.string.pause_workout)
        }
        
        // Initial animation
        binding.root.layoutAnimation = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_fall_down)
    }

    private fun setupTabs() {
        binding.tabLayoutWorkout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        binding.planContainer.isVisible = true
                        binding.rvWorkoutHistory.isVisible = false
                    }
                    1 -> {
                        binding.planContainer.isVisible = false
                        binding.rvWorkoutHistory.isVisible = true
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupWeekSelector() {
        val weeks = (1..12).map { getString(R.string.week_format, it) }
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
        val dayNames = workouts.map { getString(R.string.day_format, it.dayNumber, it.name) }

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
        
        if (!viewModel.isWorkoutActive) {
            viewModel.resetWorkout()
            binding.tvElapsedTime.visibility = View.GONE
        }

        binding.tvWorkoutTitle.text = day.name
        binding.tvWorkoutProgress.text = getString(R.string.exercises_count_format, 0, day.exercises.size)
        
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

    private fun setupHistoryList() {
        historyAdapter = HistoryAdapter()
        binding.rvWorkoutHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvWorkoutHistory.adapter = historyAdapter
    }

    private fun setupObservers() {
        viewModel.allWorkouts.observe(viewLifecycleOwner) { logs ->
            historyAdapter.submitList(logs)
        }

        viewModel.todayWorkout.observe(viewLifecycleOwner) { log ->
            if (log?.completed == true) {
                binding.btnStartWorkout.text = getString(R.string.done_today)
                binding.btnStartWorkout.isEnabled = false
            }
        }

        viewModel.elapsedTime.observe(viewLifecycleOwner) { time ->
            binding.tvElapsedTime.text = time
        }
    }

    private fun updateProgress() {
        val total = selectedWorkoutDay?.exercises?.size ?: 0
        val done = completedExercises.size
        binding.progressWorkout.max = total
        binding.progressWorkout.setProgress(done, true)
        binding.tvWorkoutProgress.text = getString(R.string.exercises_count_format, done, total)

        if (done == total && total > 0) {
            binding.btnFinishWorkout.visibility = View.VISIBLE
            binding.btnFinishWorkout.animate().alpha(1f).setDuration(300).start()
        } else {
            binding.btnFinishWorkout.visibility = View.GONE
        }
    }

    private fun updateStartButton() {
        if (!viewModel.isWorkoutActive) {
            binding.btnStartWorkout.text = getString(R.string.start_workout)
            binding.btnStartWorkout.isEnabled = true
            binding.btnFinishWorkout.visibility = View.GONE
        } else {
            binding.btnStartWorkout.text = getString(R.string.pause_workout)
        }
    }

    private fun setupClickListeners() {
        binding.btnStartWorkout.setOnClickListener {
            if (!viewModel.isWorkoutActive) {
                startWorkout()
            } else {
                pauseWorkout()
            }
        }

        binding.btnFinishWorkout.setOnClickListener {
            finishWorkout()
        }
    }

    private fun startWorkout() {
        viewModel.startWorkout()
        binding.btnStartWorkout.text = getString(R.string.pause_workout)
        binding.tvElapsedTime.visibility = View.VISIBLE
        binding.tvElapsedTime.alpha = 0f
        binding.tvElapsedTime.animate().alpha(1f).setDuration(500).start()
    }

    private fun pauseWorkout() {
        viewModel.pauseWorkout()
        binding.btnStartWorkout.text = getString(R.string.resume_workout)
    }

    private fun finishWorkout() {
        viewModel.pauseWorkout()
        val elapsed = ((System.currentTimeMillis() - viewModel.workoutStartTime) / 1000 / 60).toInt().coerceAtLeast(1)
        val day = selectedWorkoutDay ?: return
        val calories = day.caloriesBurned * elapsed / day.estimatedMinutes

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.workout_complete_title)
            .setMessage(getString(R.string.workout_complete_msg, completedExercises.size, day.exercises.size, elapsed, calories))
            .setPositiveButton(R.string.save_result) { _, _ ->
                viewModel.logWorkout(day, completedExercises.size, elapsed)
                viewModel.resetWorkout()
                binding.btnStartWorkout.text = getString(R.string.done_today)
                binding.btnStartWorkout.isEnabled = false
                binding.btnFinishWorkout.visibility = View.GONE
                binding.tvElapsedTime.visibility = View.GONE
            }
            .setNegativeButton(R.string.discard) { _, _ ->
                viewModel.resumeWorkout()
            }
            .show()
    }

    override fun onDestroyView() {
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
            val progressMessage = if (ex.setsHistory.isEmpty()) {
                "No history recorded for this exercise yet. Complete workouts to see your progress!"
            } else {
                buildString {
                    appendLine("Recent performance:")
                    ex.setsHistory.takeLast(5).forEach { record ->
                        appendLine("• $record")
                    }
                }
            }

            MaterialAlertDialogBuilder(holder.itemView.context)
                .setTitle("Progress: ${ex.name}")
                .setMessage(progressMessage)
                .setPositiveButton("Got it", null)
                .show()
        }
    }

    override fun getItemCount() = exercises.size
}
