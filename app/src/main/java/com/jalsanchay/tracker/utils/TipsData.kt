package com.jalsanchay.tracker.utils

data class WaterTip(
    val title: String,
    val description: String,
    val emoji: String
)

object TipsData {
    val tips = listOf(
        WaterTip(
            "First-Flush Diverter",
            "Always discard the first 10-15 minutes of rainfall. It washes away dust, bird droppings, and pollutants from your roof. A simple diverter pipe handles this automatically.",
            "🚿"
        ),
        WaterTip(
            "Keep Gutters Clean",
            "Clean your gutters and downpipes before every monsoon season. Blocked gutters reduce effective catchment area and become breeding grounds for mosquitoes.",
            "🌿"
        ),
        WaterTip(
            "Use a Mesh Screen",
            "Fit fine mesh over all tank openings and inlet pipes. This keeps out debris, insects, and small animals — essential for water quality and safety.",
            "🔲"
        ),
        WaterTip(
            "Maximize Runoff Coefficient",
            "Smooth concrete or clay tile roofs give the best runoff (0.85–0.95). Rough surfaces or asbestos reduce it to 0.70. Repaint or seal your roof surface for better yield.",
            "🏠"
        ),
        WaterTip(
            "Monsoon Preparation",
            "Inspect tank seals, inlet joints, and overflow pipes before June. A small leak can lose hundreds of litres overnight. A yearly check prevents major losses.",
            "⛈️"
        ),
        WaterTip(
            "Tank Shade Matters",
            "Keep your storage tank in shade or paint it white/light blue. Direct sunlight heats stored water and encourages algae growth, reducing usability.",
            "🌡️"
        ),
        WaterTip(
            "Multiple Small Tanks",
            "Two 500-litre tanks are often better than one 1000-litre tank. You can isolate one for cleaning without cutting off supply. Overflow from tank 1 feeds tank 2.",
            "💧"
        ),
        WaterTip(
            "Log Every Shower",
            "Enter rainfall data within 24 hours for accurate monthly reports. Consistent logging helps you identify your best and worst catchment months over the year.",
            "📊"
        ),
        WaterTip(
            "Rooftop Garden Tip",
            "Green rooftop gardens reduce runoff by 40-50%. While beautiful, they are not ideal for harvesting. Use terrace gardens on a separate non-catchment area.",
            "🌱"
        ),
        WaterTip(
            "Overflow = Opportunity",
            "An overflowing tank means lost water. Direct overflow to a soakpit or garden. This recharges groundwater and counts as indirect water saving.",
            "♻️"
        )
    )
}
