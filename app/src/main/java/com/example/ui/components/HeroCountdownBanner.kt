package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Exhibition
import com.example.ui.theme.AcidYellow
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.NeonPink
import kotlinx.coroutines.delay

@Composable
fun HeroCountdownBanner(
    nextExhibition: Exhibition?,
    onOpenDetails: (Exhibition) -> Unit,
    modifier: Modifier = Modifier
) {
    if (nextExhibition == null) return

    var currentMillis by remember { mutableLongStateOf(System.currentTimeMillis()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            currentMillis = System.currentTimeMillis()
        }
    }

    val (days, hours, minutes, seconds) = remember(nextExhibition.startDate, currentMillis) {
        try {
            val parts = nextExhibition.startDate.split("-")
            val cal = java.util.Calendar.getInstance()
            cal.set(parts[0].toInt(), parts[1].toInt() - 1, parts[2].toInt(), 10, 0, 0)
            val diff = cal.timeInMillis - currentMillis
            if (diff > 0) {
                val d = diff / (1000 * 60 * 60 * 24)
                val h = (diff / (1000 * 60 * 60)) % 24
                val m = (diff / (1000 * 60)) % 60
                val s = (diff / 1000) % 60
                listOf(d, h, m, s)
            } else {
                listOf(0L, 0L, 0L, 0L)
            }
        } catch (_: Exception) {
            listOf(0L, 0L, 0L, 0L)
        }
    }

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier
            .fillMaxWidth()
            .testTag("hero_countdown_banner")
            .clip(RoundedCornerShape(24.dp))
            .clickable { onOpenDetails(nextExhibition) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_toy_expo_hero),
                contentDescription = "Toy Expo banner",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.5f),
                                Color(0xFF0A0C14).copy(alpha = 0.85f),
                                Color(0xFF0A0C14).copy(alpha = 0.98f)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = NeonPink,
                        shape = RoundedCornerShape(100.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "БЛИЖАЙШИЙ МИРОВОЙ ЭКСПО",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }

                    Text(
                        text = "📍 ${nextExhibition.city}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ElectricCyan
                    )
                }

                Column {
                    Text(
                        text = nextExhibition.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "${nextExhibition.edition} • ${nextExhibition.displayDates}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = AcidYellow
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TimerUnitBox(value = days.toString(), label = "ДНЕЙ")
                        TimerUnitBox(value = hours.toString().padStart(2, '0'), label = "ЧАС")
                        TimerUnitBox(value = minutes.toString().padStart(2, '0'), label = "МИН")
                        TimerUnitBox(value = seconds.toString().padStart(2, '0'), label = "СЕК")
                    }

                    Button(
                        onClick = { onOpenDetails(nextExhibition) },
                        colors = ButtonDefaults.buttonColors(containerColor = NeonPink),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .height(44.dp)
                            .testTag("hero_open_details_button")
                    ) {
                        Text(
                            text = "Дропы",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TimerUnitBox(
    value: String,
    label: String
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFF1B2032).copy(alpha = 0.85f),
        border = androidx.compose.foundation.BorderStroke(1.dp, ElectricCyan.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier
                .width(44.dp)
                .padding(vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Text(
                text = label,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = ElectricCyan
            )
        }
    }
}
