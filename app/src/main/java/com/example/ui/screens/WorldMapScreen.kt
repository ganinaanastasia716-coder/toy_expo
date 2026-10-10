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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Exhibition
import com.example.ui.theme.AcidYellow
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.NeonPink

data class ToyCapital(
    val cityName: String,
    val countryFlag: String,
    val countryName: String,
    val vibe: String,
    val mustVisitDistricts: String,
    val keyExpos: List<String>
)

@Composable
fun WorldMapScreen(
    allExhibitions: List<Exhibition>,
    onSelectExhibition: (Exhibition) -> Unit,
    modifier: Modifier = Modifier
) {
    val capitals = remember {
        listOf(
            ToyCapital(
                cityName = "Токио & Тиба",
                countryFlag = "🇯🇵",
                countryName = "Япония",
                vibe = "Мировая столица софуби, кайдзю и гараж-китов",
                mustVisitDistricts = "Nakano Broadway (Мандараке), Akihabara Radio Kaikan, Harajuku (Secret Base, Bounty Hunter)",
                keyExpos = listOf("Wonder Festival (WonFes Winter)", "Tokyo Comic Con & Sofubi Alley")
            ),
            ToyCapital(
                cityName = "Бангкок",
                countryFlag = "🇹🇭",
                countryName = "Таиланд",
                vibe = "Эпицентр азиатского смоляного и винилового арт-тоя под открытым небом",
                mustVisitDistricts = "CentralWorld (TTE), Mega Plaza Saphan Lek, Siam Paragon",
                keyExpos = listOf("Thailand Toy Expo (TTE)")
            ),
            ToyCapital(
                cityName = "Шанхай",
                countryFlag = "🇨🇳",
                countryName = "Китай",
                vibe = "Мегаполис блайндбоксов, флагманов POP MART и масштабных скульптур",
                mustVisitDistricts = "Nanjing East Road Pop Mart Global Flagship, TX Huaihai Youth Energy Center",
                keyExpos = listOf("Shanghai Toy Show (STS)")
            ),
            ToyCapital(
                cityName = "Анахайм & Лос-Анджелес",
                countryFlag = "🇺🇸",
                countryName = "США (Калифорния)",
                vibe = "Родина урбан-винила, поп-сюрреализма и наград Designer Toy Awards",
                mustVisitDistricts = "Melrose Avenue, Little Tokyo (Q Pop Shop, Giant Robot), Arts District",
                keyExpos = listOf("DesignerCon (D-Con)", "ComplexCon")
            ),
            ToyCapital(
                cityName = "Тайбэй",
                countryFlag = "🇹🇼",
                countryName = "Тайвань",
                vibe = "Классический независимый арт-той и кайдзю в исторических креативных парках",
                mustVisitDistricts = "Huashan 1914 Creative Park, Songshan Cultural Park, Ximending Toy Stores",
                keyExpos = listOf("Taipei Toy Festival (TTF)")
            ),
            ToyCapital(
                cityName = "Сингапур",
                countryFlag = "🇸🇬",
                countryName = "Сингапур",
                vibe = "Футуристичный хаб с эксклюзивными тиражами Mega Space Molly и Mighty Jaxx",
                mustVisitDistricts = "Marina Bay Sands Expo, Funan Mall (ActionCity & Pop Mart)",
                keyExpos = listOf("Pop Toy Show (PTS)")
            ),
            ToyCapital(
                cityName = "Нью-Йорк & Бруклин",
                countryFlag = "🇺🇸",
                countryName = "США (Нью-Йорк)",
                vibe = "Уличный арт, граффити, крафтовое пиво и кастом-платформы",
                mustVisitDistricts = "Brooklyn ZeroSpace, SoHo Designer Boutiques, Lower East Side Galleries",
                keyExpos = listOf("Five Points Festival")
            ),
            ToyCapital(
                cityName = "Сеул",
                countryFlag = "🇰🇷",
                countryName = "Южная Корея",
                vibe = "Минималистичный дизайнерский арт-той, городские персонажи и авторские фигурки",
                mustVisitDistricts = "Seongsu-dong (поп-апы и галереи), COEX Mall, Hongdae KT&G Sangsangmadang",
                keyExpos = listOf("Art Toy Culture (ATC)")
            ),
            ToyCapital(
                cityName = "Гонконг",
                countryFlag = "🇭🇰",
                countryName = "Гонконг",
                vibe = "Колыбель мирового винила 90-х (Michael Lau, Eric So, Hot Toys)",
                mustVisitDistricts = "Mong Kok Sino Centre, In's Point (винтажный и современный винил)",
                keyExpos = listOf("Hong Kong Toy Soul")
            ),
            ToyCapital(
                cityName = "Лидс & Лондон",
                countryFlag = "🇬🇧",
                countryName = "Великобритания",
                vibe = "Европейские независимые мастера смолы, стимпанк и авторский плюш",
                mustVisitDistricts = "Custard Factory, Camden Market, London Gallery Solo Shows",
                keyExpos = listOf("Toy Art UK")
            )
        )
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 96.dp, top = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Мировые столицы Design Toy",
                    fontSize = 24.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Главные города арт-тоя, где проходят знаковые конвенты и живут коллекционеры",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        items(capitals) { capital ->
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = capital.countryFlag, fontSize = 24.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = capital.cityName,
                                        fontSize = 17.sp,
                                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = capital.countryName,
                                        fontSize = 12.sp,
                                        color = ElectricCyan,
                                        fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = capital.vibe,
                            fontSize = 13.sp,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                            color = AcidYellow
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = NeonPink,
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Где искать магазины: ${capital.mustVisitDistricts}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 16.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Конвенты города:",
                            fontSize = 12.sp,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        capital.keyExpos.forEach { expoName ->
                            val matchedExpo = allExhibitions.firstOrNull { it.name.contains(expoName) || expoName.contains(it.name) }
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp)
                                    .clickable {
                                        if (matchedExpo != null) {
                                            onSelectExhibition(matchedExpo)
                                        }
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = expoName,
                                            fontSize = 13.sp,
                                            fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        if (matchedExpo != null) {
                                            Text(
                                                text = "${matchedExpo.displayDates} • ${matchedExpo.daysUntil()} дн.",
                                                fontSize = 11.sp,
                                                color = ElectricCyan
                                            )
                                        }
                                    }
                                    Icon(
                                        imageVector = Icons.Default.ArrowForward,
                                        contentDescription = "Перейти",
                                        tint = NeonPink,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
