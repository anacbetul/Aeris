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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.luci.aeris.R
import com.luci.aeris.utils.constants.NavigationRoutes
import com.luci.aeris.utils.constants.StringConstants
import com.luci.aeris.domain.repository.FirebaseAuthRepository
import com.luci.aeris.domain.repository.FirestoreUserRepository
import com.luci.aeris.utils.navigator.Navigator
import com.luci.aeris.presentation.viewmodel.RegisterViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navigator: Navigator) {
    val authRepository: FirebaseAuthRepository = FirebaseAuthRepository()
    val firebaseFireStoreRepository: FirestoreUserRepository =FirestoreUserRepository()
    val viewModel: RegisterViewModel = viewModel(
        factory = RegisterViewModel.Factory(authRepository,firebaseFireStoreRepository)
    )

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    val context = LocalContext.current
    val emailFocus = remember { FocusRequester() }
    val passwordFocus = remember { FocusRequester() }
    val confirmPasswordFocus = remember { FocusRequester() }

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

    // Snackbar ve Navigation effect, loading ile birlikte
    LaunchedEffect(viewModel.isSuccess) {
        viewModel.isSuccess?.let { success ->
            if (success) {
                snackbarHostState.showSnackbar(StringConstants.successRegister)
                navigator.navigateAndClearBackStack(NavigationRoutes.Main)
                viewModel.isLoading = false
            } else {
                val message = viewModel.errorMessage ?: StringConstants.somethingWentWrong
                snackbarHostState.showSnackbar(message)
                viewModel.isLoading = false
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(PaddingConstants.ScreenPadding)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
                elevation = CardDefaults.cardElevation(8.dp),
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
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(60.dp)
                    )

                    VerticalSpacer24()
                    HeaderText(
                        text = StringConstants.signUp,
                        fontWeight = FontWeight.Bold,
                        centerTitle = true,
                        textColor = Color.Black
                    )
                    VerticalSpacer24()

                    // Email
                    AtomicOutlinedTextField(
                        value = viewModel.email,
                        onValueChange = viewModel::onEmailChange,
                        label = StringConstants.email,
                        isError = viewModel.emailError,
                        errorText = if (viewModel.emailError) StringConstants.validEmail else null,
                        prefix = { Icon(Icons.Outlined.Mail, null, tint = Color.Gray) },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { passwordFocus.requestFocus() }),
                        modifier = Modifier.focusRequester(emailFocus)
                    )

                    VerticalSpacer16()

                    // Password
                    AtomicOutlinedTextField(
                        value = viewModel.password,
                        onValueChange = viewModel::onPasswordChange,
                        label = StringConstants.createPassword,
                        isError = viewModel.passwordError,
                        errorText = if (viewModel.passwordError) StringConstants.passwordRule else null,
                        isPassword = true,
                        prefix = { Icon(Icons.Outlined.Lock, null, tint = Color.Gray) },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { confirmPasswordFocus.requestFocus() }),
                        modifier = Modifier.focusRequester(passwordFocus)
                    )

                    VerticalSpacer16()

                    // Confirm Password
                    AtomicOutlinedTextField(
                        value = viewModel.confirmPassword,
                        onValueChange = viewModel::onConfirmPasswordChange,
                        label = StringConstants.confirmPassword,
                        isError = viewModel.confirmPasswordError,
                        errorText = if (viewModel.confirmPasswordError) StringConstants.passwordDoesntMatch else null,
                        isPassword = true,
                        prefix = { Icon(Icons.Outlined.Lock, null, tint = Color.Gray) },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                        modifier = Modifier.focusRequester(confirmPasswordFocus)
                    )

                    VerticalSpacer24()

                    // Sign Up Button
                    Button(
                        onClick = { viewModel.registerUser() },
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
                            BodyText(StringConstants.signUp, textColor = Color.White)
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
                        BodyText(StringConstants.signUpWithGoogle)
                    }

                    VerticalSpacer16()
                }
            }

            VerticalSpacer16()

            // Navigation to Login
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BodyText(text = StringConstants.alreadyHaveAccount, fontSize = 14.sp, textColor = Color.Gray)
                Spacer(modifier = Modifier.width(4.dp))
                BodyText(
                    text = StringConstants.login,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { navigator.navigateAndClearBackStack(NavigationRoutes.Login) }
                )
            }
        }

        // Loading overlay, snackbar görünürken bile çalışır
        if (viewModel.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        // Snackbar for errors & messages
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
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text(snackbarData.visuals.message)
                }
            }
        )
    }
}
