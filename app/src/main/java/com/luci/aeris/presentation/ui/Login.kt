package com.luci.aeris.presentation.ui

import AtomicOutlinedTextField
import BodyText
import HeaderText
import HorizontalSpacer4
import PaddingConstants
import VerticalSpacer16
import VerticalSpacer24
import VerticalSpacer8
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luci.aeris.presentation.viewmodel.LoginViewModel
import com.luci.aeris.utils.constants.NavigationRoutes
import com.luci.aeris.utils.constants.StringConstants
import com.luci.aeris.domain.repository.FirebaseAuthRepository
import com.luci.aeris.domain.repository.FirestoreUserRepository
import com.luci.aeris.utils.navigator.Navigator
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.luci.aeris.R
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navigator: Navigator) {
    val authRepository: FirebaseAuthRepository = FirebaseAuthRepository()
    val firestoreRepository: FirestoreUserRepository = FirestoreUserRepository()
    val snackbarHostState = remember { SnackbarHostState() }
    val viewModel: LoginViewModel = viewModel(
        factory = LoginViewModel.Factory(authRepository, firestoreRepository)
    )
    val emailFocus = remember { FocusRequester() }
    val passwordFocus = remember { FocusRequester() }
    val confirmPasswordFocus = remember { FocusRequester() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Google Sign-In launcher
    val googleSignInLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            val idToken = account.idToken
            if (idToken != null) {
                viewModel.signInWithGoogle(account) { success, error ->
                    if (success) {
                        scope.launch {
                            snackbarHostState.showSnackbar(StringConstants.successRegister)
                        }
                        navigator.navigateAndClearBackStack(NavigationRoutes.Main)
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar(error ?: StringConstants.somethingWentWrong)
                        }
                    }
                }
            }
        } catch (e: ApiException) {
            scope.launch {
                snackbarHostState.showSnackbar("${StringConstants.somethingWentWrongOnGoogle}: ${e.localizedMessage}")
            }
        }
    }

    val googleSignInClient = remember {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    LaunchedEffect(viewModel.isSuccess) {
        viewModel.isSuccess?.let { success ->
            if (success) {
                navigator.navigateAndClearBackStack(NavigationRoutes.Main)
            } else {
                viewModel.errorMessage?.let { message ->
                    snackbarHostState.showSnackbar(message)
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(PaddingConstants.ScreenPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.weight(1f))

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
                    Icon(
                        imageVector = Icons.Default.AccountBox,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(60.dp)
                    )
                    VerticalSpacer8()

                    HeaderText(
                        text = StringConstants.login,
                        fontWeight = FontWeight.Bold,
                        centerTitle = true,
                        textColor = Color.Black
                    )

                    VerticalSpacer24()

                    AtomicOutlinedTextField(
                        value = viewModel.email.value,
                        onValueChange = viewModel::onEmailChange,
                        label = StringConstants.email,
                        isError = viewModel.emailError,
                        prefix = {
                            Icon(
                                imageVector = Icons.Outlined.Mail,
                                contentDescription = null,
                                tint = Color.Gray
                            )
                        },
                        errorText = if (viewModel.emailError) StringConstants.validEmail else null,
                                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { passwordFocus.requestFocus() }),
                        modifier = Modifier.focusRequester(emailFocus)
                    )

                    VerticalSpacer16()

                    AtomicOutlinedTextField(
                        value = viewModel.password.value,
                        onValueChange = viewModel::onPasswordChange,
                        label = StringConstants.password,
                        isError = viewModel.passwordError,
                        errorText = if (viewModel.passwordError) StringConstants.passwordRule else null,
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { confirmPasswordFocus.requestFocus() }),
                        modifier = Modifier.focusRequester(passwordFocus),
                        prefix = {
                            Icon(
                                imageVector = Icons.Outlined.Lock,
                                contentDescription = null,
                                tint = Color.Gray
                            )
                        },
                        isPassword = true
                    )

                    VerticalSpacer24()

                    Button(
                        onClick = { viewModel.login() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                        enabled = !viewModel.isLoading
                    ) {
                        if (viewModel.isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            BodyText(StringConstants.login, textColor = Color.White)
                        }
                    }

                    VerticalSpacer16()
                    BodyText(
                        text = StringConstants.or,
                        fontSize = 14.sp,
                        textColor = Color.Gray,
                        modifier = Modifier.padding(vertical = 8.dp),
                        textAlign = TextAlign.Center
                    )
                    VerticalSpacer16()
                    // Google Sign-In Button
                    OutlinedButton(
                        onClick = {
                            viewModel.isLoadingGoogle = true
                            googleSignInClient.signOut().addOnCompleteListener {
                                googleSignInLauncher.launch(googleSignInClient.signInIntent)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_google),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        HorizontalSpacer4()
                        BodyText(StringConstants.loginWithGoogle)
                    }

                    VerticalSpacer16()
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ALT KISIM
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                VerticalSpacer8()

                Row {
                    BodyText(text = StringConstants.dontHaveAccount, fontSize = 14.sp, textColor = Color.Gray)
                    BodyText(
                        StringConstants.signUp,
                        textColor = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            navigator.navigateAndClearBackStack(NavigationRoutes.Register)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }

        // Snackbar
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            snackbar = { snackbarData ->
                Snackbar(
                    containerColor = if (viewModel.isSuccess ?: false)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    shape = MaterialTheme.shapes.medium
                ) {
                    BodyText(snackbarData.visuals.message)
                }
            }
        )
    }
}
