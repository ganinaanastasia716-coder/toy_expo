package com.example.ui.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.AcidYellow
import com.example.ui.theme.NeonPink

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDropDialog(
    exhibitionId: String,
    onDismiss: () -> Unit,
    onConfirm: (
        exhibitionId: String,
        toyName: String,
        artistOrBrand: String,
        editionSize: String,
        estimatedPrice: String,
        dropType: String,
        isGrail: Boolean
    ) -> Unit
) {
    var toyName by remember { mutableStateOf("") }
    var artistOrBrand by remember { mutableStateOf("") }
    var editionSize by remember { mutableStateOf("Лимит 100 экз.") }
    var estimatedPrice by remember { mutableStateOf("$150") }
    var dropType by remember { mutableStateOf("Лотерея (Lotto)") }
    var isGrail by remember { mutableStateOf(true) }

    val dropTypes = listOf(
        "Лотерея (Lotto)",
        "Живая очередь (FCFS)",
        "Предзаказ на стенде",
        "Блайндбокс (Chase)",
        "VIP-дроп"
    )
    var typeExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Добавить эксклюзивный релиз",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = toyName,
                    onValueChange = { toyName = it },
                    label = { Text("Название фигурки / арт-той *") },
                    placeholder = { Text("Например: Space Molly Cyberpunk") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_drop_name_input")
                )

                OutlinedTextField(
                    value = artistOrBrand,
                    onValueChange = { artistOrBrand = it },
                    label = { Text("Художник или бренд *") },
                    placeholder = { Text("Например: Kenny Wong x POP MART") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = editionSize,
                        onValueChange = { editionSize = it },
                        label = { Text("Тираж") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = estimatedPrice,
                        onValueChange = { estimatedPrice = it },
                        label = { Text("Цена") },
                        modifier = Modifier.weight(1f)
                    )
                }

                ExposedDropdownMenuBox(
                    expanded = typeExpanded,
                    onExpandedChange = { typeExpanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = dropType,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Тип дропа") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = typeExpanded,
                        onDismissRequest = { typeExpanded = false }
                    ) {
                        dropTypes.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type) },
                                onClick = {
                                    dropType = type
                                    typeExpanded = false
                                }
                            )
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Checkbox(
                        checked = isGrail,
                        onCheckedChange = { isGrail = it },
                        colors = CheckboxDefaults.colors(checkedColor = AcidYellow)
                    )
                    Text(
                        text = "Отметить как Священный Грааль 🎯",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (toyName.isNotBlank() && artistOrBrand.isNotBlank()) {
                        onConfirm(
                            exhibitionId,
                            toyName,
                            artistOrBrand,
                            editionSize,
                            estimatedPrice,
                            dropType,
                            isGrail
                        )
                    }
                },
                enabled = toyName.isNotBlank() && artistOrBrand.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = NeonPink),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("submit_add_drop_button")
            ) {
                Text("Добавить в радар", color = Color.White, fontWeight = FontWeight.Bold)
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
