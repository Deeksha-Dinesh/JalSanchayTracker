package com.jalsanchay.tracker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jalsanchay.tracker.data.db.RainfallEntry
import com.jalsanchay.tracker.databinding.ItemRainfallEntryBinding
import com.jalsanchay.tracker.utils.WaterCalculator
import java.text.SimpleDateFormat
import java.util.*

class RainfallEntryAdapter(
    private val onDeleteClick: (RainfallEntry) -> Unit
) : ListAdapter<RainfallEntry, RainfallEntryAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemRainfallEntryBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(entry: RainfallEntry) {
            // Format date nicely
            try {
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val date = sdf.parse(entry.date)
                val displaySdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                binding.tvEntryDate.text = displaySdf.format(date!!)
            } catch (e: Exception) {
                binding.tvEntryDate.text = entry.date
            }

            binding.tvRainfallMm.text = "${String.format("%.1f", entry.rainfallMm)} mm"
            binding.tvLitresSaved.text = WaterCalculator.formatLitres(entry.litresSaved)
            binding.tvHouseholdDays.text =
                "${String.format("%.1f", WaterCalculator.toHouseholdDays(entry.litresSaved))} days"

            binding.btnDelete.setOnClickListener { onDeleteClick(entry) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRainfallEntryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<RainfallEntry>() {
        override fun areItemsTheSame(old: RainfallEntry, new: RainfallEntry) = old.id == new.id
        override fun areContentsTheSame(old: RainfallEntry, new: RainfallEntry) = old == new
    }
}
