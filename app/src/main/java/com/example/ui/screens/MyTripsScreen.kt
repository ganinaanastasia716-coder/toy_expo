package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AttendanceStatus
import com.example.data.model.Exhibition
import com.example.ui.theme.AcidYellow
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.NeonPink

@Composable
fun MyTripsScreen(
    allExhibitions: List<Exhibition>,
    onSelectExhibition: (Exhibition) -> Unit,
    onNavigateToFeed: () -> Unit,
    modifier: Modifier = Modifier
) {
    val trackedExhibitions = allExhibitions.filter {
        it.attendance != AttendanceStatus.NONE || it.isFavorite
    }

    val goingExpos = trackedExhibitions.filter { it.attendance == AttendanceStatus.GOING }
    val interestedExpos = trackedExhibitions.filter { it.attendance == AttendanceStatus.INTERESTED }
    val onlineExpos = trackedExhibitions.filter { it.attendance == AttendanceStatus.ONLINE_STREAM }
    val favoriteExpos = trackedExhibitions.filter { it.isFavorite && it.attendance == AttendanceStatus.NONE }

    val totalBudget = trackedExhibitions.sumOf { it.plannedBudget }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 96.dp, top = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Мои поездки и вишлист",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Планы посещения, бюджет на фигурки и чек-листы подготовки",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item {
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonPink.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Сводка путешественника ✈️",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonPink
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = "Иду лично", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    text = "${goingExpos.size} конв.",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White
                                )
                            }
                            Column {
                                Text(text = "В вишлисте", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    text = "${interestedExpos.size + favoriteExpos.size}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = AcidYellow
                                )
                            }
                            Column {
                                Text(text = "Бюджет на дропы", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    text = if (totalBudget > 0) "~${totalBudget.toInt()} $" else "Не задан",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = ElectricCyan
                                )
                            }
                        }
                    }
                }
            }
        }

        if (trackedExhibitions.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp, horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "🎟️", fontSize = 52.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Вы пока не запланировали поездки",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Откройте любую выставку в календаре и отметьте статус «Иду» или добавьте в Избранное!",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onNavigateToFeed,
                        colors = ButtonDefaults.buttonColors(containerColor = NeonPink),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Перейти к выставкам", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            if (goingExpos.isNotEmpty()) {
                item {
                    Text(
                        text = "🎟️ Точно иду (${goingExpos.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = NeonPink,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                items(goingExpos) { expo ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        TripExpoCard(expo = expo, onClick = { onSelectExhibition(expo) })
                    }
                }
            }

            if (interestedExpos.isNotEmpty()) {
                item {
                    Text(
                        text = "⭐ Планирую / В вишлисте (${interestedExpos.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AcidYellow,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                items(interestedExpos) { expo ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        TripExpoCard(expo = expo, onClick = { onSelectExhibition(expo) })
                    }
                }
            }

            if (onlineExpos.isNotEmpty()) {
                item {
                    Text(
                        text = "💻 Онлайн-участие (${onlineExpos.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = ElectricCyan,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                items(onlineExpos) { expo ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        TripExpoCard(expo = expo, onClick = { onSelectExhibition(expo) })
                    }
                }
            }

            if (favoriteExpos.isNotEmpty()) {
                item {
                    Text(
                        text = "❤️ В избранном (${favoriteExpos.size})",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                items(favoriteExpos) { expo ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        TripExpoCard(expo = expo, onClick = { onSelectExhibition(expo) })
                    }
                }
            }
        }
    }
}

@Composable
private fun TripExpoCard(
    expo: Exhibition,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(100.dp),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Text(
                        text = "${expo.category.chipEmoji} ${expo.category.displayNameRu}",
                        fontSize = 11.sp,
                        color = ElectricCyan,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(100.dp),
                    color = if (expo.daysUntil() <= 14) AcidYellow else MaterialTheme.colorScheme.surface
                ) {
                    Text(
                        text = "Через ${expo.daysUntil()} дн.",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (expo.daysUntil() <= 14) Color.Black else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = expo.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 4.dp)) {
                Icon(Icons.Default.DateRange, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = expo.displayDates, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.width(10.dp))
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = AcidYellow, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = expo.city, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
            }

            if (expo.plannedBudget > 0 || expo.completedChecklist.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (expo.plannedBudget > 0) {
                        Text(
                            text = "Бюджет: ${expo.plannedBudget.toInt()} ${expo.budgetCurrency}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = AcidYellow
                        )
                    }
                    if (expo.completedChecklist.isNotEmpty()) {
                        Text(
                            text = "Чек-лист: ${expo.completedChecklist.size} собрано",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
