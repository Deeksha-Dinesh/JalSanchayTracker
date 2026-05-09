package com.jalsanchay.tracker.ui.report

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jalsanchay.tracker.adapter.RainfallEntryAdapter
import com.jalsanchay.tracker.databinding.FragmentMonthlyReportBinding
import com.jalsanchay.tracker.utils.WaterCalculator
import com.jalsanchay.tracker.viewmodel.WaterViewModel

class MonthlyReportFragment : Fragment() {

    private var _binding: FragmentMonthlyReportBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WaterViewModel by activityViewModels()
    private lateinit var adapter: RainfallEntryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMonthlyReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvReportMonth.text = viewModel.currentMonthName()

        adapter = RainfallEntryAdapter { entry ->
            AlertDialog.Builder(requireContext())
                .setTitle("Delete Entry")
                .setMessage("Delete entry for ${entry.date}?\nThis cannot be undone.")
                .setPositiveButton("Delete") { _, _ -> viewModel.deleteEntry(entry) }
                .setNegativeButton("Cancel", null)
                .show()
        }
        binding.rvEntries.layoutManager = LinearLayoutManager(requireContext())
        binding.rvEntries.adapter = adapter

        val currentMonth = viewModel.currentMonth()

        viewModel.getMonthlyEntries(currentMonth).observe(viewLifecycleOwner) { entries ->
            adapter.submitList(entries)

            if (entries.isEmpty()) {
                binding.tvNoEntries.visibility = View.VISIBLE
                binding.rvEntries.visibility = View.GONE
            } else {
                binding.tvNoEntries.visibility = View.GONE
                binding.rvEntries.visibility = View.VISIBLE

                val totalLitres = entries.sumOf { it.litresSaved }
                val totalRainfall = entries.sumOf { it.rainfallMm }
                val avgRainfall = totalRainfall / entries.size

                binding.tvMonthTotalLitres.text = WaterCalculator.formatLitres(totalLitres)
                binding.tvMonthTotalRainfall.text = String.format("%.1f mm total rainfall", totalRainfall)
                binding.tvMonthAvgRainfall.text = String.format("%.1f mm avg per entry", avgRainfall)
                binding.tvMonthHouseholdDays.text =
                    String.format("%.1f household water days", WaterCalculator.toHouseholdDays(totalLitres))
                binding.tvMonthEntryCount.text = "${entries.size} entries this month"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
