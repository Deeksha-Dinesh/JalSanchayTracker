package com.jalsanchay.tracker.ui.entry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import androidx.navigation.fragment.findNavController
import com.jalsanchay.tracker.R
import com.jalsanchay.tracker.databinding.FragmentDataEntryBinding
import com.jalsanchay.tracker.utils.InputValidator
import com.jalsanchay.tracker.utils.ValidationResult
import com.jalsanchay.tracker.utils.WaterCalculator
import com.jalsanchay.tracker.viewmodel.WaterViewModel
import java.text.SimpleDateFormat
import java.util.*

class DataEntryFragment : Fragment() {

    private var _binding: FragmentDataEntryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WaterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDataEntryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Warn user if setup was never saved
        if (!viewModel.isSetupDone) {
            com.google.android.material.snackbar.Snackbar.make(
                requireView(),
                "⚠️ Please complete Setup first for accurate calculations!",
                com.google.android.material.snackbar.Snackbar.LENGTH_LONG
            ).setAction("Go to Setup") {
                findNavController().navigate(R.id.setupFragment)
            }.show()
        }

        // Show today's date
        val today = SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault()).format(Date())
        binding.tvEntryDate.text = today

        // Show current setup info
        binding.tvSetupInfo.text =
            "Roof: ${viewModel.roofAreaSqFt.toInt()} sq.ft  |  Runoff: ${String.format("%.2f", viewModel.runoffCoefficient)}"

        // Live preview: update estimated litres as user types
        binding.etRainfallMm.addTextChangedListener(object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                val input = s.toString()
                val result = InputValidator.validateRainfall(input)
                if (result is ValidationResult.Success) {
                    val litres = WaterCalculator.calculateLitresSaved(
                        viewModel.roofAreaSqFt,
                        result.value,
                        viewModel.runoffCoefficient
                    )
                    binding.tvLitresPreview.text = "≈ ${WaterCalculator.formatLitres(litres)} will be saved"
                    binding.tvLitresPreview.visibility = View.VISIBLE
                } else {
                    binding.tvLitresPreview.visibility = View.GONE
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.btnLogRainfall.setOnClickListener {
            logRainfall()
        }

        // Observe duplicate date warning
        viewModel.duplicateEntryEvent.observe(viewLifecycleOwner) { isDuplicate ->
            if (isDuplicate == true) {
                Snackbar.make(
                    binding.root,
                    "⚠️ You already logged rainfall for today!",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun logRainfall() {
        val input = binding.etRainfallMm.text.toString()
        when (val result = InputValidator.validateRainfall(input)) {
            is ValidationResult.Error -> {
                binding.tilRainfallMm.error = result.message
            }
            is ValidationResult.Success -> {
                binding.tilRainfallMm.error = null
                viewModel.saveRainfallEntry(result.value)

                val litres = WaterCalculator.calculateLitresSaved(
                    viewModel.roofAreaSqFt,
                    result.value,
                    viewModel.runoffCoefficient
                )

                Snackbar.make(
                    binding.root,
                    "Logged! ${WaterCalculator.formatLitres(litres)} saved today 💧",
                    Snackbar.LENGTH_LONG
                ).show()

                binding.etRainfallMm.text?.clear()
                binding.tvLitresPreview.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}