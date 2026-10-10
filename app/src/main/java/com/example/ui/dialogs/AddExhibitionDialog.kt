package com.example.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Region
import com.example.data.model.ToyCategory
import com.example.ui.theme.NeonPink

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExhibitionDialog(
    onDismiss: () -> Unit,
    onConfirm: (
        name: String,
        edition: String,
        city: String,
        country: String,
        region: Region,
        venue: String,
        startDate: String,
        endDate: String,
        category: ToyCategory,
        description: String,
        ticketPrice: String,
        website: String,
        artists: String,
        brands: String,
        raffleTips: String
    ) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var edition by remember { mutableStateOf("2026/2027") }
    var city by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var venue by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("2026-12-15") }
    var endDate by remember { mutableStateOf("2026-12-17") }
    var ticketPrice by remember { mutableStateOf("Бесплатно") }
    var description by remember { mutableStateOf("") }
    var website by remember { mutableStateOf("") }
    var artists by remember { mutableStateOf("") }
    var brands by remember { mutableStateOf("") }
    var raffleTips by remember { mutableStateOf("") }

    var selectedRegion by remember { mutableStateOf(Region.ASIA) }
    var regionExpanded by remember { mutableStateOf(false) }

    var selectedCategory by remember { mutableStateOf(ToyCategory.VINYL) }
    var categoryExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Добавить выставку или поп-ап",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Название события *") },
                    placeholder = { Text("Например: Tokyo Indie Sofubi Fair") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_expo_name_input")
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = city,
                        onValueChange = { city = it },
                        label = { Text("Город *") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = country,
                        onValueChange = { country = it },
                        label = { Text("Страна *") },
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = venue,
                    onValueChange = { venue = it },
                    label = { Text("Локация / Галерея") },
                    placeholder = { Text("Например: Art Space Gallery") },
                    modifier = Modifier.fillMaxWidth()
                )

                ExposedDropdownMenuBox(
                    expanded = regionExpanded,
                    onExpandedChange = { regionExpanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = "${selectedRegion.icon} ${selectedRegion.displayNameRu}",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Регион") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = regionExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = regionExpanded,
                        onDismissRequest = { regionExpanded = false }
                    ) {
                        Region.values().filter { it != Region.ALL }.forEach { reg ->
                            DropdownMenuItem(
                                text = { Text("${reg.icon} ${reg.displayNameRu}") },
                                onClick = {
                                    selectedRegion = reg
                                    regionExpanded = false
                                }
                            )
                        }
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = "${selectedCategory.chipEmoji} ${selectedCategory.displayNameRu}",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Основной стиль") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        ToyCategory.values().filter { it != ToyCategory.ALL }.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text("${cat.chipEmoji} ${cat.displayNameRu}") },
                                onClick = {
                                    selectedCategory = cat
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = startDate,
                        onValueChange = { startDate = it },
                        label = { Text("Дата начала (ГГГГ-ММ-ДД)") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = endDate,
                        onValueChange = { endDate = it },
                        label = { Text("Дата окончания") },
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = ticketPrice,
                    onValueChange = { ticketPrice = it },
                    label = { Text("Стоимость билета") },
                    placeholder = { Text("Например: $20 или Free") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = artists,
                    onValueChange = { artists = it },
                    label = { Text("Художники (через запятую)") },
                    placeholder = { Text("Иван Иванов, KAWS, Quiccs") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = brands,
                    onValueChange = { brands = it },
                    label = { Text("Бренды / Организаторы") },
                    placeholder = { Text("Medicom, Indie Vinyl Co.") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Описание") },
                    minLines = 2,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = raffleTips,
                    onValueChange = { raffleTips = it },
                    label = { Text("Правила лотерей / Советы") },
                    placeholder = { Text("Электронная лотерея, живая очередь и т.д.") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = website,
                    onValueChange = { website = it },
                    label = { Text("Ссылка на сайт или соцсеть") },
                    placeholder = { Text("https://...") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && city.isNotBlank()) {
                        onConfirm(
                            name,
                            edition,
                            city,
                            country.ifEmpty { "Планета Земля" },
                            selectedRegion,
                            venue.ifEmpty { "Центральная галерея" },
                            startDate,
                            endDate,
                            selectedCategory,
                            description.ifEmpty { "Выставка и маркет авторских дизайнерских игрушек." },
                            ticketPrice,
                            website,
                            artists,
                            brands,
                            raffleTips
                        )
                    }
                },
                enabled = name.isNotBlank() && city.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = NeonPink),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("submit_add_expo_button")
            ) {
                Text("Сохранить", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        shape = RoundedCornerShape(24.dp),
        containerColor = MaterialTheme.colorScheme.surface
    )
}
