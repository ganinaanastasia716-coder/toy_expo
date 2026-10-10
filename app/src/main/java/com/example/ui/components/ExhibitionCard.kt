package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AttendanceStatus
import com.example.data.model.Exhibition
import com.example.ui.theme.AcidYellow
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.NeonPink

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExhibitionCard(
    exhibition: Exhibition,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isHappening = exhibition.isHappeningNow()
    val days = exhibition.daysUntil()

    val cardBorderColor by animateColorAsState(
        if (exhibition.isFavorite) NeonPink.copy(alpha = 0.6f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
        label = "cardBorderColor"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("exhibition_card_${exhibition.id}")
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, cardBorderColor, RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(100.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = androidx.compose.foundation.BorderStroke(1.dp, ElectricCyan.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${exhibition.category.chipEmoji} ${exhibition.category.displayNameRu}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = ElectricCyan
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    val (badgeText, badgeBg, badgeTextColor) = when {
                        isHappening -> Triple("ИДЕТ СЕЙЧАС 🔥", NeonPink, Color.White)
                        days == 0 -> Triple("Сегодня! 🚀", AcidYellow, Color.Black)
                        days <= 7 -> Triple("Через $days дн. ⚡", AcidYellow, Color.Black)
                        else -> Triple("Через $days дн.", MaterialTheme.colorScheme.surface, MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = badgeBg,
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Text(
                            text = badgeText,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = badgeTextColor
                        )
                    }

                    IconButton(
                        onClick = onToggleFavorite,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("favorite_button_${exhibition.id}")
                    ) {
                        Icon(
                            imageVector = if (exhibition.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = if (exhibition.isFavorite) "Удалить из избранного" else "В избранное",
                            tint = if (exhibition.isFavorite) NeonPink else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = exhibition.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = exhibition.edition,
                style = MaterialTheme.typography.bodyMedium,
                color = NeonPink,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.DateRange, contentDescription = null, tint = ElectricCyan, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = exhibition.displayDates, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1)
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = AcidYellow, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${exhibition.city}, ${exhibition.country}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = exhibition.description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 16.sp
            )

            if (exhibition.featuredArtists.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    maxItemsInEachRow = 3
                ) {
                    exhibition.featuredArtists.take(3).forEach { artist ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "👤 $artist",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            if (exhibition.attendance != AttendanceStatus.NONE) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when (exhibition.attendance) {
                        AttendanceStatus.GOING -> NeonPink.copy(alpha = 0.15f)
                        AttendanceStatus.INTERESTED -> AcidYellow.copy(alpha = 0.15f)
                        AttendanceStatus.ONLINE_STREAM -> ElectricCyan.copy(alpha = 0.15f)
                        AttendanceStatus.ATTENDED -> Color(0xFF26DE81).copy(alpha = 0.15f)
                        AttendanceStatus.NONE -> Color.Transparent
                    },
                    border = androidx.compose.foundation.BorderStroke(
                        0.5.dp,
                        when (exhibition.attendance) {
                            AttendanceStatus.GOING -> NeonPink
                            AttendanceStatus.INTERESTED -> AcidYellow
                            AttendanceStatus.ONLINE_STREAM -> ElectricCyan
                            AttendanceStatus.ATTENDED -> Color(0xFF26DE81)
                            AttendanceStatus.NONE -> Color.Transparent
                        }
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${exhibition.attendance.emoji} ${exhibition.attendance.titleRu}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (exhibition.plannedBudget > 0) {
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = "Бюджет: ${exhibition.plannedBudget.toInt()} ${exhibition.budgetCurrency}",
                                fontSize = 11.sp,
                                color = AcidYellow,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}
