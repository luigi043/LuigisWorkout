package com.luigisworkout.ui.exercises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.luigisworkout.R
import com.luigisworkout.data.AppContent
import com.luigisworkout.data.LibraryExercise
import com.luigisworkout.databinding.FragmentExerciseLibraryBinding
import com.luigisworkout.util.ExampleSheet

/**
 * Browsable, musclewiki.com-style catalogue of exercises (the "Exercises" tab).
 * A single-select chip row filters by muscle group; tapping a card opens the
 * in-app MuscleWiki video example via [ExampleSheet].
 */
class ExerciseLibraryFragment : Fragment() {

    private var _binding: FragmentExerciseLibraryBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: LibraryAdapter

    /** "All" sentinel + the real muscle groups. */
    private val filters: List<String> by lazy { listOf(ALL) + AppContent.libraryMuscles }
    private var selectedFilter: String = ALL

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentExerciseLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = LibraryAdapter { ex -> ExampleSheet.show(requireContext(), ex.name) }
        binding.rvLibrary.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLibrary.adapter = adapter

        buildFilterChips()
        applyFilter(selectedFilter)
    }

    private fun buildFilterChips() {
        binding.chipGroupMuscles.removeAllViews()
        filters.forEach { label ->
            val chip = Chip(requireContext()).apply {
                text = label
                isCheckable = true
                isClickable = true
                isCheckedIconVisible = false
                id = View.generateViewId()
                isChecked = label == selectedFilter
                setOnClickListener {
                    if (isChecked && selectedFilter != label) applyFilter(label)
                }
            }
            binding.chipGroupMuscles.addView(chip)
        }
    }

    private fun applyFilter(filter: String) {
        selectedFilter = filter
        val list = if (filter == ALL) AppContent.exerciseLibrary
        else AppContent.exerciseLibrary.filter { it.muscle == filter }

        adapter.submitList(list)
        binding.tvLibraryEmpty.isVisible = list.isEmpty()
        binding.rvLibrary.isVisible = list.isNotEmpty()
        if (list.isNotEmpty()) {
            binding.rvLibrary.layoutAnimation =
                AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_fall_down)
            binding.rvLibrary.scheduleLayoutAnimation()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private companion object {
        const val ALL = "All"
    }
}

// ─── ADAPTER ────────────────────────────────────────────────────────────────────

class LibraryAdapter(
    private val onWatch: (LibraryExercise) -> Unit
) : ListAdapter<LibraryExercise, LibraryAdapter.ViewHolder>(DIFF) {

    object DIFF : DiffUtil.ItemCallback<LibraryExercise>() {
        override fun areItemsTheSame(oldItem: LibraryExercise, newItem: LibraryExercise) =
            oldItem.name == newItem.name
        override fun areContentsTheSame(oldItem: LibraryExercise, newItem: LibraryExercise) =
            oldItem == newItem
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvLibName)
        val muscle: TextView = view.findViewById(R.id.tvLibMuscle)
        val equipment: TextView = view.findViewById(R.id.tvLibEquipment)
        val difficulty: TextView = view.findViewById(R.id.tvLibDifficulty)
        val tip: TextView = view.findViewById(R.id.tvLibTip)
        val watch: MaterialButton = view.findViewById(R.id.btnLibWatch)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_library_exercise, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ex = getItem(position)
        holder.name.text = ex.name
        holder.muscle.text = ex.muscle.uppercase()
        holder.equipment.text = ex.equipment
        holder.difficulty.text = ex.difficulty
        holder.tip.isVisible = ex.tip.isNotBlank()
        holder.tip.text = ex.tip

        val open = { onWatch(ex) }
        holder.itemView.setOnClickListener { open() }
        holder.watch.setOnClickListener { open() }
    }
}
