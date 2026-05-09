package com.jalsanchay.tracker.ui.dashboard

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.jalsanchay.tracker.R
import com.jalsanchay.tracker.data.db.RainfallEntry
import com.jalsanchay.tracker.databinding.FragmentDashboardBinding
import com.jalsanchay.tracker.utils.WaterCalculator
import com.jalsanchay.tracker.viewmodel.WaterViewModel
import java.text.SimpleDateFormat
import java.util.*

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WaterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBarChart()

        // Today's savings observer - required by project spec
        val todayDate = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())
        viewModel.getDailyLitres(todayDate).observe(viewLifecycleOwner) { todayLitres ->
            val litres = todayLitres ?: 0.0
            binding.tvTodayLitres.text = WaterCalculator.formatLitres(litres)
            binding.tvTodayHouseholdDays.text =
                "${String.format("%.1f", WaterCalculator.toHouseholdDays(litres))} household water days"
        }

        // Total savings observer
        viewModel.totalLitresSaved.observe(viewLifecycleOwner) { total ->
            val litres = total ?: 0.0

            binding.tvTotalLitres.text = WaterCalculator.formatLitres(litres)
            binding.tvHouseholdDays.text =
                "${String.format("%.1f", WaterCalculator.toHouseholdDays(litres))} household water days"

            // Animate tank fill
            val fillPct = WaterCalculator.tankFillPercent(litres, viewModel.tankCapacityLitres)
            val animator = ObjectAnimator.ofInt(binding.progressTank, "progress", 0, fillPct)
            animator.duration = 1200
            animator.start()
            binding.tvTankPercent.text = "$fillPct%"

            // Impact Score: project spec says "Converts liters into Household water days"
            val householdDays = WaterCalculator.toHouseholdDays(litres)
            binding.tvImpactScore.text = "${String.format("%.1f", householdDays)} household water days"
        }

        // Monthly savings
        viewModel.getMonthlyLitres(viewModel.currentMonth()).observe(viewLifecycleOwner) { monthly ->
            val litres = monthly ?: 0.0
            binding.tvMonthlyLitres.text = WaterCalculator.formatLitres(litres)
            binding.tvMonthLabel.text = "Saved in ${viewModel.currentMonthName()}"
        }

        // Entry count
        viewModel.entryCount.observe(viewLifecycleOwner) { count ->
            binding.tvEntryCount.text = "$count entries logged"
        }

        // Bar chart - last 7 entries
        viewModel.lastSevenEntries.observe(viewLifecycleOwner) { entries ->
            updateChart(entries)
        }
    }

    private fun setupBarChart() {
        binding.barChart.apply {
            description.isEnabled = false
            legend.isEnabled = false
            setDrawGridBackground(false)
            setTouchEnabled(false)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1f
            xAxis.setDrawGridLines(false)
            axisLeft.setDrawGridLines(true)
            axisRight.isEnabled = false
            animateY(800)
        }
    }

    private fun updateChart(entries: List<RainfallEntry>) {
        if (entries.isEmpty()) {
            binding.barChart.visibility = View.GONE
            binding.tvNoData.visibility = View.VISIBLE
            return
        }

        binding.barChart.visibility = View.VISIBLE
        binding.tvNoData.visibility = View.GONE

        val reversed = entries.reversed()
        val barEntries = reversed.mapIndexed { index, entry ->
            BarEntry(index.toFloat(), entry.litresSaved.toFloat())
        }

        val labels = reversed.map { entry ->
            try {
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val date = sdf.parse(entry.date)
                SimpleDateFormat("dd/MM", Locale.getDefault()).format(date!!)
            } catch (e: Exception) {
                entry.date.takeLast(5)
            }
        }

        val dataSet = BarDataSet(barEntries, "Litres Saved").apply {
            color = requireContext().getColor(R.color.water_blue)
            valueTextSize = 9f
            setDrawValues(true)
        }

        binding.barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        binding.barChart.data = BarData(dataSet)
        binding.barChart.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
