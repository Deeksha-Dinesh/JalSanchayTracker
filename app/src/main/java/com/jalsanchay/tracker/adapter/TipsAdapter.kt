package com.jalsanchay.tracker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jalsanchay.tracker.databinding.ItemTipBinding
import com.jalsanchay.tracker.utils.WaterTip

class TipsAdapter : ListAdapter<WaterTip, TipsAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemTipBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(tip: WaterTip) {
            binding.tvTipEmoji.text = tip.emoji
            binding.tvTipTitle.text = tip.title
            binding.tvTipDescription.text = tip.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTipBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<WaterTip>() {
        override fun areItemsTheSame(old: WaterTip, new: WaterTip) = old.title == new.title
        override fun areContentsTheSame(old: WaterTip, new: WaterTip) = old == new
    }
}
