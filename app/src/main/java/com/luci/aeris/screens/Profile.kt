package com.luci.aeris.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.luci.aeris.constants.StringConstants

@Composable
fun Profile() {
    val isDarkMode = remember { mutableStateOf(false) }
    var selectedGender by remember { mutableStateOf("") }
    val email = "kullanici@mail.com"
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        // Profil Fotoğrafı
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .align(Alignment.CenterHorizontally)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profil Fotoğrafı",
                tint = Color.White,
                modifier = Modifier
                    .size(60.dp)
                    .align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .animateContentSize( // içeriğe göre animasyonla büyü
                    animationSpec = tween(durationMillis = 100)
                )
        ) {
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Hesap Bilgilerim",
                    style = MaterialTheme.typography.bodyLarge
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = "Aç/Kapa"
                )
            }

            // Animasyonlu görünürlük
            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = "kullanici@mail.com",
                        onValueChange = {},
                        label = { Text("E-posta") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = "********",
                        onValueChange = {},
                        label = { Text("Şifre") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("${StringConstants.gender}: ", style = MaterialTheme.typography.bodyLarge)
                        GenderDropdownMenu(
                            selectedGender = selectedGender,
                            onGenderSelected = { selectedGender = it }
                        )
                    }
                    Button(modifier = Modifier.align(alignment = Alignment.End),
                        onClick = { /* çıkış işlemi */ },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                    ) {
                        Text("Düzenle")
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            // Tema Ayarı
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Karanlık Mod", style = MaterialTheme.typography.bodyLarge)
                Switch(checked = isDarkMode.value, onCheckedChange = { isDarkMode.value = it })
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
        }
        Spacer(modifier = Modifier.height(32.dp))
        Row (modifier = Modifier.align(alignment = Alignment.End) ){
            // Çıkış ve Hesabı Sil Butonları
            Button(
                onClick = { /* çıkış işlemi */ },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
            ) {
                Text("Çıkış Yap")
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = { /* hesap silme uyarısı aç */ }) {
                Text("Hesabımı Sil", color = Color.Red)
            }
        }
    }
}

@Composable
fun GenderDropdownMenu(
    selectedGender: String,
    onGenderSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val genders = listOf("Kadın", "Erkek", "Diğer")

    Box {
        TextButton(onClick = { expanded = true },colors = ButtonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.secondary,
            disabledContainerColor = MaterialTheme.colorScheme.secondary,
            disabledContentColor = MaterialTheme.colorScheme.secondary,
        )) {
            Text(text = selectedGender.ifEmpty { "Cinsiyet Seç" })
        }

        DropdownMenu(
            modifier= Modifier.background(color = MaterialTheme.colorScheme.background),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            genders.forEach { gender ->
                DropdownMenuItem(
                    modifier= Modifier.background(color = MaterialTheme.colorScheme.background),
                    text = { Text(gender, style = MaterialTheme.typography.bodyLarge) },
                    onClick = {
                        onGenderSelected(gender)
                        expanded = false
                    }
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    Profile()
}