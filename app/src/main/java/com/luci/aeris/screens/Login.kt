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
import com.luci.aeris.viewmodel.LoginViewModel
import com.luci.aeris.ui.theme.AerisTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.luci.aeris.constants.NavigationRoutes
import com.luci.aeris.utils.Navigator

@Composable
fun LoginScreen(navigator: Navigator, viewModel: LoginViewModel = viewModel()) {
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

                // Log In Header
                HeaderText(
                    text = "Login",
                    fontWeight = FontWeight.Bold,
                    centerTitle = true,
                    textColor = Color.Black
                )

                VerticalSpacer24()

                // Email Input
                AtomicOutlinedTextField(
                    value = viewModel.email.value,
                    onValueChange = viewModel::onEmailChange,
                    label = "Email",
                    isError = viewModel.emailError.value,
                    errorText = if (viewModel.emailError.value) "Please enter a valid email" else null
                )

                VerticalSpacer16()

                // Password Input
                AtomicOutlinedTextField(
                    value = viewModel.password.value,
                    onValueChange = viewModel::onPasswordChange,
                    label = "Password",
                    isError = viewModel.passwordError.value,
                    errorText = if (viewModel.passwordError.value) "Password must be at least 6 characters" else null,
                    isPassword = true
                )

                VerticalSpacer24()

                // Login Button
                Button(
                    onClick = {
                        if (viewModel.isFormValid()) {
                            // Giriş işlemi
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFFFFC107))
                ) {
                    BodyText(text = "Login", textColor = Color.White)
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

            // Signup redirect text
            Row {
                BodyText(text = "Don't have an account?", textColor = Color.Black)
                BodyText(
                    "Sign Up",
                    textColor = Color(0xFFFFC107),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navigator.navigate(NavigationRoutes.Register)
                    }
                )
            }
        }
    }
}


