package com.luci.aeris.presentation.ui

import BodyText
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.luci.aeris.utils.constants.StringConstants
import com.luci.aeris.presentation.viewmodel.ThemeViewModel
import com.luci.aeris.presentation.viewmodel.ProfileViewModel
import com.luci.aeris.utils.components.AirisAlertDialog
import com.luci.aeris.utils.navigator.Navigator

@Composable
fun Profile(
    navigator: Navigator,
    viewModel: ProfileViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel
) {
    val isDarkMode by themeViewModel.isDarkTheme.collectAsState()
    var selectedGender by remember { mutableStateOf("") }
    val user = viewModel.user
    val isLoading = viewModel.isLoading
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val currentUser = viewModel.authRepository.currentUser
        currentUser?.uid?.let { uid ->
            viewModel.loadUser(uid)
        }
    }

    LaunchedEffect(user) {
        if (selectedGender.isEmpty()) {
            selectedGender = user?.gender ?: ""
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
                .background(MaterialTheme.colorScheme.secondary)
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
        ) {
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            BodyText(
                text = StringConstants.accountInfo,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

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
                value = if (user?.password == StringConstants.emptyString)
                    StringConstants.loggedGoogle else StringConstants.star,
                onValueChange = {},
                label = { BodyText(StringConstants.password) },
                readOnly = true,
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                BodyText(text = StringConstants.selectGender)
                GenderDropdownMenu(
                    selectedGender = selectedGender,
                    onGenderSelected = {
                        selectedGender = it ;
                        if (selectedGender.isNotBlank()) {
                        viewModel.updateUserGender(selectedGender)
                    } }
                )
            }


            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Alt Butonlar
        Row(modifier = Modifier.align(Alignment.End)) {
            OutlinedButton(
                onClick = { showLogoutDialog = true },
            ) {
                BodyText(StringConstants.exit)
            }

            Spacer(modifier = Modifier.width(8.dp))

            TextButton(onClick = {
                showDeleteDialog = true
            }) {
                BodyText(
                    StringConstants.deleteAccount,
//                    fontSize = 12.sp,
                    textColor = MaterialTheme.colorScheme.error
                )
            }
        }
    }

    // Çıkış Dialog
    if (showLogoutDialog) {
        AirisAlertDialog(
            title = StringConstants.exit,
            message = StringConstants.confirmExit,
            confirmText = StringConstants.signOut,
            dismissText = StringConstants.stayInApp,
            onConfirm = {
                showLogoutDialog = false
                viewModel.onSignOut(navigator)
            },
            onDismiss = { showLogoutDialog = false }
        )
    }

    // Hesap Silme Dialog
    if (showDeleteDialog) {
        AirisAlertDialog(
            title = StringConstants.deleteTitle,
            message = StringConstants.confirmDeleteAccount,
            confirmText = StringConstants.deleteConfirm,
            dismissText = StringConstants.deleteCancel,
            onConfirm = {
                showDeleteDialog = false
                viewModel.deleteAccount(navigator)
            },
            onDismiss = { showDeleteDialog = false }
        )
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
        TextButton(onClick = { expanded = true }) {
            BodyText(
                text = selectedGender.ifEmpty { StringConstants.selectGender },
                textColor = MaterialTheme.colorScheme.secondary
            )
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
