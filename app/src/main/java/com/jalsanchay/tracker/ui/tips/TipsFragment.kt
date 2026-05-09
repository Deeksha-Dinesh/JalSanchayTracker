package com.jalsanchay.tracker.ui.tips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jalsanchay.tracker.adapter.TipsAdapter
import com.jalsanchay.tracker.databinding.FragmentTipsBinding
import com.jalsanchay.tracker.utils.TipsData

class TipsFragment : Fragment() {

    private var _binding: FragmentTipsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTipsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TipsAdapter()
        binding.rvTips.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTips.adapter = adapter
        adapter.submitList(TipsData.tips)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
