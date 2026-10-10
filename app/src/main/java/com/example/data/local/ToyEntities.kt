package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_exhibitions")
data class UserExhibitionEntity(
    @PrimaryKey val id: String,
    val name: String,
    val edition: String,
    val city: String,
    val country: String,
    val regionStr: String,
    val venue: String,
    val startDate: String,
    val endDate: String,
    val displayDates: String,
    val categoryStr: String,
    val description: String,
    val ticketInfo: String,
    val ticketPriceEstimate: String,
    val officialWebsite: String,
    val featuredArtistsStr: String,
    val featuredBrandsStr: String,
    val lotteryAndRaffleTips: String,
    val lat: Double = 0.0,
    val lng: Double = 0.0
)

@Entity(tableName = "exhibition_tracking")
data class ExhibitionTrackingEntity(
    @PrimaryKey val exhibitionId: String,
    val isFavorite: Boolean = false,
    val attendanceStatusStr: String = "NONE",
    val userNotes: String = "",
    val plannedBudget: Double = 0.0,
    val budgetCurrency: String = "USD",
    val completedChecklistStr: String = ""
)

@Entity(tableName = "exclusive_drops")
data class ExclusiveDropEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val exhibitionId: String,
    val toyName: String,
    val artistOrBrand: String,
    val editionSize: String,
    val estimatedPrice: String,
    val dropType: String,
    val isWishlistGrail: Boolean = false,
    val statusStr: String = "WANTED"
)
