package com.example.data.model

enum class Region(val displayNameRu: String, val icon: String) {
    ALL("Все регионы", "🌍"),
    ASIA("Азия", "⛩️"),
    NORTH_AMERICA("Северная Америка", "🗽"),
    EUROPE("Европа", "🏛️")
}

enum class ToyCategory(val displayNameRu: String, val chipEmoji: String) {
    ALL("Все стили", "✨"),
    SOFUBI("Софуби (Sofubi)", "👾"),
    VINYL("Urban Vinyl", "🧸"),
    BLIND_BOX("Блайндбоксы", "🎁"),
    RESIN("Смола / Резин", "💎"),
    CUSTOM("Кастом & Арт", "🎨")
}

enum class AttendanceStatus(val titleRu: String, val emoji: String) {
    NONE("Не отслеживается", "⚪"),
    INTERESTED("В вишлисте", "⭐"),
    GOING("Иду / Билет куплен", "🎟️"),
    ONLINE_STREAM("Смотрю онлайн / Дропы", "💻"),
    ATTENDED("Уже посетил", "✅")
}

enum class DropStatus(val titleRu: String) {
    WANTED("В вишлисте 🎯"),
    WON_LOTTO("Выиграл лото 🏆"),
    PURCHASED("Куплено 🛍️"),
    MISSED("Пропущено ❌")
}

data class ExclusiveDropItem(
    val id: Long = 0,
    val exhibitionId: String,
    val toyName: String,
    val artistOrBrand: String,
    val editionSize: String,
    val estimatedPrice: String,
    val dropType: String,
    val isWishlistGrail: Boolean = false,
    val status: DropStatus = DropStatus.WANTED
)

data class Exhibition(
    val id: String,
    val name: String,
    val edition: String,
    val city: String,
    val country: String,
    val region: Region,
    val venue: String,
    val startDate: String,
    val endDate: String,
    val displayDates: String,
    val category: ToyCategory,
    val description: String,
    val ticketInfo: String,
    val ticketPriceEstimate: String,
    val officialWebsite: String,
    val featuredArtists: List<String>,
    val featuredBrands: List<String>,
    val lotteryAndRaffleTips: String,
    val lat: Double,
    val lng: Double,
    val isCustom: Boolean = false,
    val isFavorite: Boolean = false,
    val attendance: AttendanceStatus = AttendanceStatus.NONE,
    val userNotes: String = "",
    val plannedBudget: Double = 0.0,
    val budgetCurrency: String = "USD",
    val completedChecklist: List<String> = emptyList(),
    val customDrops: List<ExclusiveDropItem> = emptyList()
) {
    fun daysUntil(): Int {
        return try {
            val parts = startDate.split("-")
            if (parts.size == 3) {
                val year = parts[0].toInt()
                val month = parts[1].toInt()
                val day = parts[2].toInt()
                val targetCal = java.util.Calendar.getInstance()
                targetCal.set(year, month - 1, day, 0, 0, 0)
                val now = java.util.Calendar.getInstance()
                val diffMillis = targetCal.timeInMillis - now.timeInMillis
                val days = (diffMillis / (1000 * 60 * 60 * 24)).toInt()
                if (days < 0) 0 else days
            } else 30
        } catch (_: Exception) {
            30
        }
    }

    fun isHappeningNow(): Boolean {
        return try {
            val now = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US).format(java.util.Date())
            now >= startDate && now <= endDate
        } catch (_: Exception) {
            false
        }
    }
}
