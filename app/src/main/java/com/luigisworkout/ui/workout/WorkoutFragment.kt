package com.luigisworkout.ui.workout

import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.luigisworkout.R
import com.luigisworkout.data.Exercise
import com.luigisworkout.data.WorkoutDay
import com.luigisworkout.databinding.FragmentWorkoutBinding
import com.luigisworkout.luigiApp
import com.luigisworkout.util.MuscleWiki
import com.luigisworkout.viewmodel.WorkoutViewModel
import com.luigisworkout.viewmodel.appViewModelFactory

class WorkoutFragment : Fragment() {

    private var _binding: FragmentWorkoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WorkoutViewModel by activityViewModels {
        appViewModelFactory(requireActivity().luigiApp)
    }

    private lateinit var exerciseAdapter: ExerciseAdapter
    private lateinit var historyAdapter: HistoryAdapter

    private var selectedWorkoutDay: WorkoutDay? = null
    private var completedExercises = mutableSetOf<Int>()
    private var isCurrentDayDone: Boolean = false

    /** Cached so the chip row is only rebuilt when the underlying list actually changes. */
    private var lastChipSignature: Triple<Int, List<Int>, Set<Int>>? = null

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

        if (viewModel.isWorkoutActive) {
            binding.tvElapsedTime.visibility = View.VISIBLE
            binding.btnStartWorkout.text = getString(R.string.pause_workout)
        }

        binding.root.layoutAnimation = AnimationUtils.loadLayoutAnimation(
            requireContext(), R.anim.layout_animation_fall_down
        )
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

        // Default to the user's current week the first time we have a profile.
        viewModel.userProfile.observe(viewLifecycleOwner) { profile ->
            val week = (profile?.currentWeekNumber ?: 1).coerceIn(1, 12)
            if (binding.spinnerWeek.selectedItemPosition != week - 1) {
                binding.spinnerWeek.setSelection(week - 1)
            }
        }

        binding.spinnerWeek.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.selectWeek(position + 1)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupExerciseList() {
        exerciseAdapter = ExerciseAdapter(viewModel, viewLifecycleOwner) { index, completed ->
            if (isCurrentDayDone) return@ExerciseAdapter // checks are read-only on done days
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

        // (Re)build the day-chip row whenever the selected week or its completion
        // set changes. We diff a Triple signature so we don't rebuild on every emit.
        viewModel.selectedWeek.observe(viewLifecycleOwner) { week ->
            rebuildDayChipsIfNeeded(week, viewModel.completedDaysForSelectedWeek.value ?: emptyList())
        }
        viewModel.completedDaysForSelectedWeek.observe(viewLifecycleOwner) { doneDays ->
            val week = viewModel.selectedWeek.value ?: 1
            rebuildDayChipsIfNeeded(week, doneDays)
            renderWeekProgress(week, doneDays.size)
        }

        // When the selected (week, day) lands on a previously-logged day, pre-tick
        // every exercise and switch the action row into "Done / Redo" mode.
        viewModel.selectedDayLog.observe(viewLifecycleOwner) { log ->
            val done = log?.completed == true
            isCurrentDayDone = done
            applyDoneState(done)
        }

        viewModel.elapsedTime.observe(viewLifecycleOwner) { time ->
            binding.tvElapsedTime.text = time
        }

        viewModel.weekAdvanced.observe(viewLifecycleOwner) { newWeek ->
            val justFinished = newWeek - 1
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.week_complete_title, justFinished))
                .setMessage(getString(R.string.week_complete_msg, justFinished, newWeek))
                .setPositiveButton(R.string.onward) { _, _ ->
                    // Jump the spinner to the new week so the next workout is one tap away.
                    binding.spinnerWeek.setSelection(newWeek - 1)
                }
                .setCancelable(false)
                .show()
        }
    }

    /** Rebuilds the horizontal day chips for [week] from the planned days + [doneDays]. */
    private fun rebuildDayChipsIfNeeded(week: Int, doneDays: List<Int>) {
        val workouts = viewModel.getWorkoutsByWeek(week)
        val planSignature = workouts.map { it.dayNumber }
        val doneSet = doneDays.toSet()
        val signature = Triple(week, planSignature, doneSet)
        if (signature == lastChipSignature) return
        lastChipSignature = signature

        binding.chipGroupDays.removeAllViews()
        if (workouts.isEmpty()) return

        workouts.forEach { day ->
            val isDone = day.dayNumber in doneSet
            val chip = Chip(requireContext()).apply {
                text = if (isDone) getString(R.string.day_done_chip, day.dayNumber)
                       else getString(R.string.day_chip, day.dayNumber)
                isCheckable = true
                isClickable = true
                isCheckedIconVisible = false
                id = View.generateViewId()
                setOnClickListener {
                    if (isChecked) {
                        viewModel.selectDay(day.dayNumber)
                        loadWorkoutDay(day)
                    }
                }
            }
            binding.chipGroupDays.addView(chip)
        }

        // Preserve the previously-selected day if it still exists in this week,
        // otherwise default to the first not-yet-done day, otherwise day 1.
        val preferredDay = workouts.firstOrNull { it.dayNumber !in doneSet }?.dayNumber
            ?: workouts.first().dayNumber
        val target = workouts.firstOrNull { it.dayNumber == (selectedWorkoutDay?.dayNumber ?: preferredDay) }
            ?: workouts.first()
        binding.chipGroupDays.children().forEachIndexed { i, chip ->
            (chip as Chip).isChecked = workouts[i].dayNumber == target.dayNumber
        }
        viewModel.selectDay(target.dayNumber)
        loadWorkoutDay(target)
    }

    private fun renderWeekProgress(week: Int, doneCount: Int) {
        val total = viewModel.getWorkoutsByWeek(week).size
        binding.tvWeekProgress.text = getString(R.string.week_progress_format, week, doneCount, total)
        binding.progressWeek.max = total.coerceAtLeast(1)
        binding.progressWeek.setProgressCompat(doneCount.coerceIn(0, total), true)
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

        exerciseAdapter.setAllPreChecked(false)
        exerciseAdapter.submitList(day.exercises)
        binding.rvExercises.scheduleLayoutAnimation()

        updateProgress()
        updateStartButton()
    }

    /** Drives the "this day is already done" presentation. */
    private fun applyDoneState(done: Boolean) {
        binding.tvDoneBadge.isVisible = done
        binding.btnRedoDay.isVisible = done

        if (done) {
            val day = selectedWorkoutDay
            if (day != null) {
                // Mark every exercise as checked in our tracking set so progress UI agrees.
                completedExercises.clear()
                completedExercises.addAll(day.exercises.indices)
                exerciseAdapter.setAllPreChecked(true)
                exerciseAdapter.notifyItemRangeChanged(0, day.exercises.size)
            }
            binding.btnStartWorkout.isEnabled = false
            binding.btnStartWorkout.text = getString(R.string.done_today)
            binding.btnFinishWorkout.visibility = View.GONE
            binding.progressWorkout.setProgress(
                selectedWorkoutDay?.exercises?.size ?: 0, true
            )
        } else {
            exerciseAdapter.setAllPreChecked(false)
            updateStartButton()
        }
    }

    private fun updateProgress() {
        val total = selectedWorkoutDay?.exercises?.size ?: 0
        val done = completedExercises.size
        binding.progressWorkout.max = total
        binding.progressWorkout.setProgress(done, true)
        binding.tvWorkoutProgress.text = getString(R.string.exercises_count_format, done, total)

        // The Finish button only appears for an in-progress (not-yet-done) day.
        if (!isCurrentDayDone && done == total && total > 0) {
            binding.btnFinishWorkout.visibility = View.VISIBLE
            binding.btnFinishWorkout.animate().alpha(1f).setDuration(300).start()
        } else if (!isCurrentDayDone) {
            binding.btnFinishWorkout.visibility = View.GONE
        }
    }

    private fun updateStartButton() {
        if (isCurrentDayDone) return // applyDoneState owns the buttons in this case
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
            if (isCurrentDayDone) return@setOnClickListener
            if (!viewModel.isWorkoutActive) startWorkout() else pauseWorkout()
        }
        binding.btnFinishWorkout.setOnClickListener { finishWorkout() }
        binding.btnRedoDay.setOnClickListener {
            // Optimistic UI reset — the underlying logs stay, but the user gets a
            // fresh attempt. Re-completing the day inserts another WorkoutLog row.
            isCurrentDayDone = false
            binding.tvDoneBadge.isVisible = false
            binding.btnRedoDay.isVisible = false
            completedExercises.clear()
            exerciseAdapter.setAllPreChecked(false)
            selectedWorkoutDay?.exercises?.size?.let { exerciseAdapter.notifyItemRangeChanged(0, it) }
            updateProgress()
            updateStartButton()
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
        val estimated = day.estimatedMinutes.coerceAtLeast(1)
        val calories = (day.caloriesBurned.toFloat() * elapsed / estimated).toInt()

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
                // The selectedDayLog observer will flip us into done-state once the
                // insert lands in Room; in the meantime show the badge optimistically.
                binding.tvDoneBadge.isVisible = true
                binding.btnRedoDay.isVisible = true
                isCurrentDayDone = true
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

/** Small helper to iterate a ViewGroup's children without pulling in core-ktx. */
private fun ViewGroup.children(): Sequence<View> = sequence {
    for (i in 0 until childCount) yield(getChildAt(i))
}

// ─── EXERCISE ADAPTER ─────────────────────────────────────────────────────────

class ExerciseAdapter(
    private val viewModel: WorkoutViewModel,
    private val viewLifecycleOwner: androidx.lifecycle.LifecycleOwner,
    private val onToggle: (Int, Boolean) -> Unit
) : ListAdapter<Exercise, ExerciseAdapter.ViewHolder>(DIFF) {

    object DIFF : DiffUtil.ItemCallback<Exercise>() {
        override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise) = oldItem.name == newItem.name
        override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise) = oldItem == newItem
    }

    /** When true, every row binds pre-checked + disabled to reflect a logged day. */
    private var preCheckedAll: Boolean = false

    fun setAllPreChecked(value: Boolean) {
        if (preCheckedAll != value) {
            preCheckedAll = value
        }
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
        val btnExample: Button = view.findViewById(R.id.btnExample)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exercise, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ex = getItem(position)

        holder.tvName.text = ex.name
        holder.tvMuscle.text = ex.muscleGroup.uppercase()
        holder.tvDifficulty.text = ex.difficulty
        holder.tvSets.text = ex.sets.toString()
        holder.tvReps.text = ex.reps
        holder.tvRest.text = "${ex.restSeconds}s"
        holder.tvDesc.text = ex.description

        // Detach the listener before mutating state — otherwise recycled checkboxes
        // can fire toggle callbacks with stale positions.
        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = preCheckedAll
        holder.checkBox.isEnabled = !preCheckedAll
        holder.statusView.visibility = if (preCheckedAll) View.VISIBLE else View.INVISIBLE
        holder.card.alpha = if (preCheckedAll) 0.6f else 1.0f

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
            showTrackExerciseDialog(holder.itemView.context, ex)
        }

        holder.btnExample.setOnClickListener {
            // Opens https://musclewiki.com/?s=<exercise name> in the user's browser.
            MuscleWiki.openExample(holder.itemView.context, ex.name)
        }
    }

    private fun showTrackExerciseDialog(context: android.content.Context, exercise: Exercise) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_track_exercise, null)
        val tvTitle: TextView = dialogView.findViewById(R.id.tvDialogTitle)
        val etWeight: EditText = dialogView.findViewById(R.id.etWeight)
        val etSets: EditText = dialogView.findViewById(R.id.etSets)
        val etReps: EditText = dialogView.findViewById(R.id.etReps)
        val rvHistory: RecyclerView = dialogView.findViewById(R.id.rvExerciseHistory)

        tvTitle.text = exercise.name
        etSets.setText(exercise.sets.toString())

        val defaultReps = exercise.reps.split("-").last().split(" ").first().filter { it.isDigit() }
        etReps.setText(defaultReps)

        val historyAdapter = ExerciseHistoryAdapter()
        rvHistory.layoutManager = LinearLayoutManager(context)
        rvHistory.adapter = historyAdapter

        viewModel.getExerciseHistory(exercise.name).observe(viewLifecycleOwner) { history ->
            historyAdapter.submitList(history)
            if (history.isNotEmpty() && etWeight.text.isEmpty()) {
                etWeight.setText(history.first().weightKg.toString())
            }
        }

        MaterialAlertDialogBuilder(context)
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val weight = etWeight.text.toString().toFloatOrNull() ?: 0f
                val sets = etSets.text.toString().toIntOrNull() ?: 0
                val reps = etReps.text.toString().toIntOrNull() ?: 0
                if (weight > 0 && sets > 0 && reps > 0) {
                    viewModel.logExercise(exercise.name, weight, reps, sets)
                }
            }
            .setNegativeButton("Close", null)
            .show()
    }
}

class ExerciseHistoryAdapter :
    ListAdapter<com.luigisworkout.data.database.entities.ExerciseLog, ExerciseHistoryAdapter.ViewHolder>(DIFF) {

    object DIFF : DiffUtil.ItemCallback<com.luigisworkout.data.database.entities.ExerciseLog>() {
        override fun areItemsTheSame(
            oldItem: com.luigisworkout.data.database.entities.ExerciseLog,
            newItem: com.luigisworkout.data.database.entities.ExerciseLog
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: com.luigisworkout.data.database.entities.ExerciseLog,
            newItem: com.luigisworkout.data.database.entities.ExerciseLog
        ) = oldItem == newItem
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvPerformance: TextView = view.findViewById(R.id.tvPerformance)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exercise_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val log = getItem(position)
        holder.tvDate.text = log.date
        holder.tvPerformance.text = "${log.weightKg}kg • ${log.sets}x${log.reps}"
    }
}
