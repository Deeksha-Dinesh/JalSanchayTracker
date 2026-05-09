package com.jalsanchay.tracker.utils

object WaterCalculator {

    /**
     * Core Formula from project spec:
     * Litres = Area (sq.ft) × Rainfall (mm) × 0.0929 × Runoff Coefficient
     *
     * 0.0929 converts sq.ft to sq.metres
     * Runoff coefficient: 0.85 for concrete/tile, 0.75 for metal, 0.6 for gravel
     */
    fun calculateLitresSaved(
        roofAreaSqFt: Double,
        rainfallMm: Double,
        runoffCoefficient: Double = 0.85
    ): Double {
        return roofAreaSqFt * rainfallMm * 0.0929 * runoffCoefficient
    }

    /**
     * Convert litres to household water days
     * Average Indian household uses ~135-150 litres/day
     */
    fun toHouseholdDays(totalLitres: Double, dailyUsageLitres: Double = 140.0): Double {
        if (dailyUsageLitres <= 0) return 0.0
        return totalLitres / dailyUsageLitres
    }

    /**
     * Tank fill percentage for progress bar visual (0-100)
     */
    fun tankFillPercent(litresSaved: Double, tankCapacityLitres: Double): Int {
        if (tankCapacityLitres <= 0) return 0
        return ((litresSaved / tankCapacityLitres) * 100).toInt().coerceIn(0, 100)
    }

    /**
     * Convert litres to bottles (1 bottle = 1 litre) for fun display
     */
    fun toLitreBuckets(litres: Double): Int = litres.toInt()

    /**
     * Format litres nicely: show kL if >= 1000
     */
    fun formatLitres(litres: Double): String {
        return if (litres >= 1000) {
            String.format("%.2f kL", litres / 1000.0)
        } else {
            String.format("%.1f L", litres)
        }
    }
}
