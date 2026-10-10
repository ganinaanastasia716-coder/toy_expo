package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.Exhibition
import com.example.data.model.Region
import com.example.data.model.ToyCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    @Test
    fun readStringFromContext() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("ToyExpo", appName)
    }

    @Test
    fun testExhibitionDaysCalculation() {
        val expo = Exhibition(
            id = "test_expo",
            name = "Test Toy Con",
            edition = "2026",
            city = "Tokyo",
            country = "Japan",
            region = Region.ASIA,
            venue = "Big Sight",
            startDate = "2026-11-20",
            endDate = "2026-11-22",
            displayDates = "Nov 20-22, 2026",
            category = ToyCategory.SOFUBI,
            description = "Test description",
            ticketInfo = "Free",
            ticketPriceEstimate = "Free",
            officialWebsite = "",
            featuredArtists = listOf("Artist A"),
            featuredBrands = listOf("Brand B"),
            lotteryAndRaffleTips = "None",
            lat = 35.6,
            lng = 139.7
        )
        assertTrue(expo.daysUntil() >= 0)
        assertEquals("Tokyo", expo.city)
    }
}
