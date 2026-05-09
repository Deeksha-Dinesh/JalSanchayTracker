package com.jalsanchay.tracker.utils

sealed class ValidationResult {
    data class Success(val value: Double) : ValidationResult()
    data class Error(val message: String) : ValidationResult()
}

object InputValidator {

    fun validateRainfall(input: String): ValidationResult {
        if (input.isBlank()) return ValidationResult.Error("Please enter rainfall amount")
        val value = input.trim().toDoubleOrNull()
            ?: return ValidationResult.Error("Enter a valid number (e.g. 25.5)")
        if (value < 0) return ValidationResult.Error("Rainfall cannot be negative")
        if (value > 1500) return ValidationResult.Error("Value seems too high — check units (mm)")
        return ValidationResult.Success(value)
    }

    fun validateRoofArea(input: String): ValidationResult {
        if (input.isBlank()) return ValidationResult.Error("Please enter roof area")
        val value = input.trim().toDoubleOrNull()
            ?: return ValidationResult.Error("Enter a valid number (e.g. 500)")
        if (value <= 0) return ValidationResult.Error("Area must be greater than 0")
        if (value > 100000) return ValidationResult.Error("Area seems too large — check units (sq.ft)")
        return ValidationResult.Success(value)
    }

    fun validateTankCapacity(input: String): ValidationResult {
        if (input.isBlank()) return ValidationResult.Error("Please enter tank capacity")
        val value = input.trim().toDoubleOrNull()
            ?: return ValidationResult.Error("Enter a valid number (e.g. 1000)")
        if (value <= 0) return ValidationResult.Error("Capacity must be greater than 0")
        if (value > 500000) return ValidationResult.Error("Capacity seems too large — check units (litres)")
        return ValidationResult.Success(value)
    }
}
