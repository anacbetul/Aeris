package com.luci.aeris.screens

import BodyText
import android.R.attr.label
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luci.aeris.constants.StringConstants
import com.luci.aeris.utils.Navigator
import com.luci.aeris.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch

@Composable
fun Profile(
    navigator: Navigator,
    viewModel: ProfileViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var expanded by remember { mutableStateOf(false) }
    val isDarkMode = remember { mutableStateOf(false) }
    var selectedGender by remember { mutableStateOf("") }
    val user = viewModel.user
    val isLoading = viewModel.isLoading
    val scope = rememberCoroutineScope()

    // Kullanıcıyı ilk kez yükle
    LaunchedEffect(Unit) {
        val currentUser = viewModel.authRepository.currentUser
        currentUser?.uid?.let { uid ->
            viewModel.loadUser(uid)
        }
    }

    if (isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(PaddingConstants.PagePadding)
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
                contentDescription = StringConstants.profilePhoto,
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
                .animateContentSize(animationSpec = tween(durationMillis = 100))
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
                BodyText(StringConstants.accountInfo)
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = StringConstants.accountInfo
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = user?.email ?: StringConstants.emptyString,
                        onValueChange = {},
                        label = { BodyText(StringConstants.email) },
                        readOnly = true,
                        enabled = false,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = if(user?.password== StringConstants.emptyString) StringConstants.loggedGoogle else StringConstants.star,
                        onValueChange = {},
                        label = { BodyText(StringConstants.password) },
                        readOnly = true,
                        enabled = false,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        BodyText("${StringConstants.gender}: ")
                        GenderDropdownMenu(
                            selectedGender = selectedGender,
                            onGenderSelected = { selectedGender = it }
                        )
                    }

                    Button(
                        onClick = { /* Düzenleme işlemi */ },
                        modifier = Modifier.align(Alignment.End),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                    ) {
                        BodyText(StringConstants.edit, textColor = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                BodyText(StringConstants.darkMode)
                Switch(checked = isDarkMode.value, onCheckedChange = { isDarkMode.value = it })
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(modifier = Modifier.align(Alignment.End)) {
            Button(
                onClick = { viewModel.onSignOut(navigator) },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
            ) {
                BodyText(StringConstants.exit, textColor = MaterialTheme.colorScheme.onPrimary)
            }

            Spacer(modifier = Modifier.width(8.dp))

            TextButton(onClick = { /* hesap silme işlemi */ }) {
                BodyText(StringConstants.deleteAccount, fontSize = 12.sp, textColor = Color.Red)
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
    val genders = listOf(StringConstants.woman, StringConstants.man, StringConstants.other)

    Box {
        TextButton(onClick = { expanded = true }, colors = ButtonDefaults.textButtonColors()) {
            BodyText(text = selectedGender.ifEmpty { StringConstants.selectGender}, textColor = MaterialTheme.colorScheme.primary)
        }

        DropdownMenu(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            genders.forEach { gender ->
                DropdownMenuItem(
                    text = { BodyText(gender) },
                    onClick = {
                        onGenderSelected(gender)
                        expanded = false
                    }
                )
            }
        }
    }
}
