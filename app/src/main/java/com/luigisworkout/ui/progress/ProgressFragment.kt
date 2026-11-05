package com.luigisworkout.ui.progress

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.luigisworkout.R
import com.luigisworkout.data.database.entities.WeightLog
import com.luigisworkout.databinding.FragmentProgressBinding
import com.luigisworkout.luigiApp
import com.luigisworkout.ui.logweight.LogWeightActivity
import com.luigisworkout.viewmodel.ProgressViewModel
import com.luigisworkout.viewmodel.appViewModelFactory

class ProgressFragment : Fragment() {

    private var _binding: FragmentProgressBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProgressViewModel by activityViewModels {
        appViewModelFactory(requireActivity().luigiApp)
    }

    private lateinit var weightHistoryAdapter: WeightHistoryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProgressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCharts()
        setupWeightHistory()
        setupObservers()
        setupClickListeners()
        viewModel.refreshStats()
    }

    private fun setupCharts() {
        configureLineChart(binding.chartWeight)
        configureLineChart(binding.chartCalories)
    }

    private fun configureLineChart(chart: LineChart) {
        chart.apply {
            description.isEnabled = false
            legend.isEnabled = true
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(false)
            setPinchZoom(false)
            setDrawGridBackground(false)
            axisRight.isEnabled = false
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1f
            xAxis.setDrawGridLines(false)
            axisLeft.setDrawGridLines(true)
            axisLeft.gridColor = Color.parseColor("#22000000")
        }
    }

    private fun setupWeightHistory() {
        weightHistoryAdapter = WeightHistoryAdapter()
        binding.rvWeightHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvWeightHistory.adapter = weightHistoryAdapter
    }

    private fun setupObservers() {
        viewModel.weightLast30Days.observe(viewLifecycleOwner) { weights ->
            if (weights.isNotEmpty()) {
                updateWeightChart(weights)
                updateWeightStats(weights)
                weightHistoryAdapter.submitList(weights.sortedByDescending { it.date })
                binding.tvNoWeightData.visibility = View.GONE
                binding.chartWeight.visibility = View.VISIBLE
            } else {
                binding.tvNoWeightData.visibility = View.VISIBLE
                binding.chartWeight.visibility = View.GONE
            }
        }

        viewModel.caloriesLast30Days.observe(viewLifecycleOwner) { data ->
            if (data.isNotEmpty()) {
                updateCaloriesChart(data.map { it.date to it.calories })
                binding.chartCalories.visibility = View.VISIBLE
                binding.tvNoCalorieData.visibility = View.GONE
            } else {
                binding.tvNoCalorieData.visibility = View.VISIBLE
                binding.chartCalories.visibility = View.GONE
            }
        }

        viewModel.completedWorkouts.observe(viewLifecycleOwner) { count ->
            binding.tvWorkoutsCompleted.text = "$count"
            val weeks = count / 3
            binding.tvWeeksActive.text = "$weeks"
        }

        viewModel.totalCaloriesBurned.observe(viewLifecycleOwner) { burned ->
            binding.tvCaloriesBurned.text = "${burned} kcal"
        }

        viewModel.userProfile.observe(viewLifecycleOwner) { profile ->
            profile?.let {
                binding.tvGoalWeight.text = if (it.goalWeightKg > 0) "${it.goalWeightKg} kg" else getString(R.string.not_defined)
                binding.tvStartWeight.text = if (it.startWeightKg > 0) "${it.startWeightKg} kg" else getString(R.string.not_defined)
            }
        }
    }

    private fun updateWeightChart(weights: List<WeightLog>) {
        val sorted = weights.sortedBy { it.date }
        val entries = sorted.mapIndexed { i, w -> Entry(i.toFloat(), w.weightKg) }
        val labels = sorted.map { it.date.substring(5) } // MM-DD

        val dataSet = LineDataSet(entries, "Peso (kg)").apply {
            color = Color.parseColor("#4CAF50")
            valueTextColor = Color.parseColor("#333333")
            lineWidth = 2.5f
            circleRadius = 4f
            setCircleColor(Color.parseColor("#4CAF50"))
            setDrawValues(true)
            valueTextSize = 9f
            mode = LineDataSet.Mode.CUBIC_BEZIER
            setDrawFilled(true)
            fillColor = Color.parseColor("#4CAF50")
            fillAlpha = 30
        }

        binding.chartWeight.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        binding.chartWeight.data = LineData(dataSet)
        binding.chartWeight.invalidate()
    }

    private fun updateCaloriesChart(data: List<Pair<String, Int>>) {
        val entries = data.mapIndexed { i, (_, cal) -> Entry(i.toFloat(), cal.toFloat()) }
        val labels = data.map { it.first.substring(5) }

        val dataSet = LineDataSet(entries, "Calorias (kcal)").apply {
            color = Color.parseColor("#FF5722")
            lineWidth = 2f
            circleRadius = 3f
            setCircleColor(Color.parseColor("#FF5722"))
            setDrawValues(false)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            setDrawFilled(true)
            fillColor = Color.parseColor("#FF5722")
            fillAlpha = 25
        }

        // Goal line
        val goalEntries = data.indices.map { Entry(it.toFloat(), 1800f) }
        val goalSet = LineDataSet(goalEntries, "Objetivo").apply {
            color = Color.parseColor("#2196F3")
            lineWidth = 1.5f
            enableDashedLine(10f, 5f, 0f)
            setDrawCircles(false)
            setDrawValues(false)
        }

        binding.chartCalories.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        binding.chartCalories.data = LineData(dataSet, goalSet)
        binding.chartCalories.invalidate()
    }

    private fun updateWeightStats(weights: List<WeightLog>) {
        val sorted = weights.sortedBy { it.date }
        val latest = sorted.lastOrNull()?.weightKg ?: 0f
        val first = sorted.firstOrNull()?.weightKg ?: latest
        val change = latest - first

        binding.tvCurrentWeight.text = String.format("%.1f kg", latest)
        binding.tvWeightChange.text = if (change <= 0)
            String.format("%.1f kg 📉", change)
        else
            String.format("+%.1f kg 📈", change)

        val heightCm = viewModel.userProfile.value?.heightCm ?: 175f
        val heightM = heightCm / 100f
        val bmi = if (heightM > 0) latest / (heightM * heightM) else 0f
        
        binding.tvCurrentBmi.text = String.format("%.1f", bmi)
        binding.tvBmiCategory.text = when {
            bmi <= 0 -> "---"
            bmi < 18.5 -> "Abaixo do peso"
            bmi < 25 -> "Peso normal ✅"
            bmi < 30 -> "Excesso de peso"
            else -> "Obesidade"
        }
    }

    private fun setupClickListeners() {
        binding.btnLogWeight.setOnClickListener {
            startActivity(Intent(requireContext(), LogWeightActivity::class.java))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// ─── WEIGHT HISTORY ADAPTER ───────────────────────────────────────────────────

class WeightHistoryAdapter : ListAdapter<WeightLog, WeightHistoryAdapter.ViewHolder>(DIFF) {

    object DIFF : DiffUtil.ItemCallback<WeightLog>() {
        override fun areItemsTheSame(oldItem: WeightLog, newItem: WeightLog) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: WeightLog, newItem: WeightLog) = oldItem == newItem
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView = view.findViewById(R.id.tvWeightDate)
        val tvWeight: TextView = view.findViewById(R.id.tvWeight)
        val tvBmi: TextView = view.findViewById(R.id.tvBmiValue)
        val tvNote: TextView = view.findViewById(R.id.tvWeightNote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_weight_log, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.tvDate.text = item.date
        holder.tvWeight.text = String.format("%.1f kg", item.weightKg)
        holder.tvBmi.text = "BMI: ${String.format("%.1f", item.bmi)}"
        if (item.note.isNotEmpty()) {
            holder.tvNote.visibility = View.VISIBLE
            holder.tvNote.text = item.note
        } else {
            holder.tvNote.visibility = View.GONE
        }
    }
}