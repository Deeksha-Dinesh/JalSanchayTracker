package com.jalsanchay.tracker.ui.setup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.jalsanchay.tracker.R
import com.jalsanchay.tracker.databinding.FragmentSetupBinding
import com.jalsanchay.tracker.utils.InputValidator
import com.jalsanchay.tracker.utils.ValidationResult
import com.jalsanchay.tracker.viewmodel.WaterViewModel

class SetupFragment : Fragment() {

    private var _binding: FragmentSetupBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WaterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Pre-fill if already set up
        if (viewModel.roofAreaSqFt > 0) {
            val roofDisplay = if (viewModel.roofAreaSqFt % 1.0 == 0.0)
                viewModel.roofAreaSqFt.toInt().toString()
            else String.format("%.2f", viewModel.roofAreaSqFt)
            binding.etRoofArea.setText(roofDisplay)
        }
        if (viewModel.tankCapacityLitres > 0) {
            val tankDisplay = if (viewModel.tankCapacityLitres % 1.0 == 0.0)
                viewModel.tankCapacityLitres.toInt().toString()
            else String.format("%.2f", viewModel.tankCapacityLitres)
            binding.etTankCapacity.setText(tankDisplay)
        }

        // Runoff coefficient dropdown
        val runoffOptions = arrayOf(
            "Concrete / Tile Roof (0.85)",
            "Metal Sheet Roof (0.75)",
            "Gravel / Rough Surface (0.60)"
        )
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, runoffOptions)
        binding.actvRunoffType.setAdapter(adapter)
        binding.actvRunoffType.setText(runoffOptions[0], false)

        binding.btnSaveSetup.setOnClickListener {
            saveSetup()
        }
    }

    private fun saveSetup() {
        var hasError = false

        val roofResult = InputValidator.validateRoofArea(binding.etRoofArea.text.toString())
        when (roofResult) {
            is ValidationResult.Error -> {
                binding.tilRoofArea.error = roofResult.message
                hasError = true
            }
            is ValidationResult.Success -> {
                binding.tilRoofArea.error = null
                viewModel.roofAreaSqFt = roofResult.value
            }
        }

        val tankResult = InputValidator.validateTankCapacity(binding.etTankCapacity.text.toString())
        when (tankResult) {
            is ValidationResult.Error -> {
                binding.tilTankCapacity.error = tankResult.message
                hasError = true
            }
            is ValidationResult.Success -> {
                binding.tilTankCapacity.error = null
                viewModel.tankCapacityLitres = tankResult.value
            }
        }

        if (hasError) return

        // Set runoff coefficient based on selection
        val selectedRunoff = binding.actvRunoffType.text.toString()
        viewModel.runoffCoefficient = when {
            selectedRunoff.contains("0.75") -> 0.75
            selectedRunoff.contains("0.60") -> 0.60
            else -> 0.85
        }

        viewModel.markSetupDone()

        Snackbar.make(binding.root, "Setup saved! Start tracking your water wealth 💧", Snackbar.LENGTH_SHORT).show()

        findNavController().navigate(R.id.action_setupFragment_to_dashboardFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
