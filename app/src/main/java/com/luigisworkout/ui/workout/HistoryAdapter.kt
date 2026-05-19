package com.luigisworkout.ui.workout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.luigisworkout.R
import com.luigisworkout.data.database.entities.WorkoutLog

/**
 * Workout history list. ListAdapter + DiffUtil gives us proper item-level animations
 * and skips the O(n) rebind storm that notifyDataSetChanged caused on every insert.
 */
class HistoryAdapter : ListAdapter<WorkoutLog, HistoryAdapter.ViewHolder>(DIFF) {

    object DIFF : DiffUtil.ItemCallback<WorkoutLog>() {
        override fun areItemsTheSame(oldItem: WorkoutLog, newItem: WorkoutLog) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: WorkoutLog, newItem: WorkoutLog) = oldItem == newItem
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView = view.findViewById(R.id.tvHistoryDate)
        val tvTitle: TextView = view.findViewById(R.id.tvHistoryTitle)
        val tvDetails: TextView = view.findViewById(R.id.tvHistoryDetails)
        val tvCalories: TextView = view.findViewById(R.id.tvHistoryCalories)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_workout_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val log = getItem(position)
        val context = holder.itemView.context
        holder.tvDate.text = log.date
        holder.tvTitle.text = log.workoutName
        holder.tvDetails.text = context.getString(
            R.string.workout_history_details,
            log.exercisesCompleted, log.totalExercises, log.totalMinutes
        )
        holder.tvCalories.text = context.getString(R.string.calories_burned_unit, log.caloriesBurned)
    }
}
