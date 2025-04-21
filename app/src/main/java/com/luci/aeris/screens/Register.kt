package com.luci.aeris.screens

import AtomicOutlinedTextField
import BodyText
import HeaderText
import PaddingConstants
import VerticalSpacer16
import VerticalSpacer24
import VerticalSpacer8
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luci.aeris.viewmodel.RegisterViewModel
import com.luci.aeris.ui.theme.AerisTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luci.aeris.constants.NavigationRoutes
import com.luci.aeris.utils.Navigator

@Composable
fun RegisterScreen(navigator: Navigator, viewModel: RegisterViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(PaddingConstants.ScreenPadding),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(PaddingConstants.Medium))

        // ORTA KISIM (Form)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PaddingConstants.Large),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                VerticalSpacer8()

                Icon(
                    imageVector = Icons.Default.AccountBox,
                    contentDescription = null,
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.size(60.dp)
                )

                VerticalSpacer24()

                HeaderText(
                    text = "Sign Up",
                    fontWeight = FontWeight.Bold,
                    centerTitle = true,
                    textColor = Color.Black
                )

                VerticalSpacer24()

                AtomicOutlinedTextField(
                    value = viewModel.email,
                    onValueChange = viewModel::onEmailChange,
                    label = "Email",
                    isError = viewModel.emailError,
                    errorText = if (viewModel.emailError) "Please enter a valid email" else null
                )

                VerticalSpacer16()

                AtomicOutlinedTextField(
                    value = viewModel.password,
                    onValueChange = viewModel::onPasswordChange,
                    label = "Create Password",
                    isError = viewModel.passwordError,
                    errorText = if (viewModel.passwordError) "Password must be at least 6 characters" else null,
                    isPassword = true
                )

                VerticalSpacer16()

                AtomicOutlinedTextField(
                    value = viewModel.confirmPassword,
                    onValueChange = viewModel::onConfirmPasswordChange,
                    label = "Confirm Password",
                    isError = viewModel.confirmPasswordError,
                    errorText = if (viewModel.confirmPasswordError) "Passwords do not match" else null,
                    isPassword = true
                )

                VerticalSpacer24()

                Button(
                    onClick = {
                        if (viewModel.isFormValid()) {
                            // Kayıt işlemi
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFFFFC107))
                ) {
                    BodyText(text = "Sign Up", textColor = Color.White)
                }
            }
        }

        // ALT KISIM
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = PaddingConstants.Large),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            VerticalSpacer8()

            BodyText(
                "By Signing Up, you agree to our\nTerms & Privacy Policy",
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                textColor = Color.Gray,
                modifier = Modifier.fillMaxWidth()
            )

            VerticalSpacer8()

            BodyText("or", textColor = Color.Gray)

            VerticalSpacer8()

            Row {
                BodyText(text = "Already have an account? ", textColor = Color.Black)
                BodyText(
                    "Login",
                    textColor = Color(0xFFFFC107),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navigator.navigate(NavigationRoutes.Login)
                    }
                )
            }
        }
    }
}
