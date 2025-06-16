package com.luci.aeris.presentation.ui

import BodyText
import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.luci.aeris.utils.navigator.Navigator
import com.luci.aeris.presentation.viewmodel.AddClothesViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luci.aeris.utils.CameraGalleryManager
import com.luci.aeris.utils.constants.NavigationRoutes
import com.luci.aeris.utils.constants.StringConstants
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddClothes(
    navigator: Navigator,
    viewModel: AddClothesViewModel = viewModel()
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val isLoading by viewModel.isLoading.collectAsState()
    val isSaving by viewModel.isSaving.collectAsState()
    val selectedImageUri by viewModel.selectedImageUri.collectAsState()
    val backgroundRemovedBitmap by viewModel.backgroundRemovedBitmap.collectAsState()
    val hasCameraPermission by viewModel.hasCameraPermission.collectAsState()
    val hasGalleryPermission by viewModel.hasGalleryPermission.collectAsState()
    val detectedType by viewModel.detectedType.collectAsState()
    val suitableConditions by viewModel.suitableConditions.collectAsState()
    val colorscheme = MaterialTheme.colorScheme

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }

    val currentDate = remember {
        java.text.SimpleDateFormat(StringConstants.dateFormat, java.util.Locale.getDefault()).format(java.util.Date())
    }

    val takePictureLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            viewModel.setSelectedImage(viewModel.cameraImageUri.value!!)
        }
    }

    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let { viewModel.setSelectedImage(it) }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        viewModel.updateCameraPermission(granted)
    }

    val galleryPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        viewModel.updateGalleryPermission(granted)
    }

    val cameraGalleryManager = remember {
        CameraGalleryManager(
            context = context,
            pickImageLauncher = pickImageLauncher,
            takePictureLauncher = takePictureLauncher,
            requestCameraPermission = {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            },
            requestGalleryPermission = {
                val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    Manifest.permission.READ_MEDIA_IMAGES
                else Manifest.permission.READ_EXTERNAL_STORAGE
                galleryPermissionLauncher.launch(permission)
            },
            showSettingsSnackbar = { message ->
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = message,
                        duration = SnackbarDuration.Short
                    )
                }
            },
            setCameraImageUri = { uri ->
                viewModel.setCameraImageUri(uri)
            },
            createImageUri = {

                createImageUri(context)
            }
        )
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    shape = RoundedCornerShape(16.dp),
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    actionColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth()
                        .wrapContentHeight()
                )
            }
        }
    ) { paddingValues ->

        Box(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .clickable { showBottomSheet = true },
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        backgroundRemovedBitmap != null -> {
                            Image(
                                bitmap = backgroundRemovedBitmap!!.asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(20.dp)),
                                contentScale = ContentScale.Fit
                            )
                        }
                        selectedImageUri != null -> {
                            Image(
                                painter = rememberAsyncImagePainter(selectedImageUri),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(20.dp)),
                                contentScale = ContentScale.Fit
                            )
                        }
                        else -> {
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Color.Gray.copy(alpha = 0.4f),
                                tonalElevation = 2.dp,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AddAPhoto,
                                        contentDescription = null,
                                        tint = colorscheme.onSurfaceVariant,
                                        modifier = Modifier.size(40.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    BodyText(text = StringConstants.addAPhoto)
                                }
                            }
                        }
                    }

                    if (isLoading) {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(Color.Black.copy(alpha = 0.4f)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (detectedType != null) {
                        BodyText(text = StringConstants.clotheDetail, fontWeight = FontWeight.W700)
                        Divider()
                        Text(text = "${StringConstants.addedOn} $currentDate")
                        Text(text = "${StringConstants.category}: $detectedType")
                    }
                    if (suitableConditions.isNotEmpty()) {
                        Text(text = "${StringConstants.suitableCondition} ${suitableConditions.joinToString()}")
                    }
                }

                if (selectedImageUri != null && detectedType != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedButton(
                            onClick = {
                                viewModel.saveClothes { success, errorMessage ->
                                    coroutineScope.launch {
                                        if (success) {
                                            navigator.navigate(
                                                route = NavigationRoutes.Wardrobe,
                                                popUpTo = NavigationRoutes.AddClothes,
                                                inclusive = true
                                            )
                                            snackbarHostState.showSnackbar(
                                                message = StringConstants.clothesSuccesfly,
                                                actionLabel = StringConstants.success
                                            )
                                            viewModel.clearSelection()
                                        } else {
                                            snackbarHostState.showSnackbar(
                                                message = "Hata: $errorMessage",
                                                actionLabel = StringConstants.error
                                            )
                                        }
                                    }
                                }
                            },
                            enabled = !isSaving
                        ) {
                            if (isSaving) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                BodyText(StringConstants.save)
                            }
                        }

                        OutlinedButton(onClick = { viewModel.clearSelection() }) {
                            BodyText(StringConstants.deleteCancel)
                        }
                    }
                }

                if (showBottomSheet) {
                    ModalBottomSheet(
                        onDismissRequest = { showBottomSheet = false },
                        sheetState = sheetState,
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                        containerColor = Color.White
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            BodyText(text = StringConstants.pickImage, fontWeight = FontWeight.W700)

                            OutlinedButton(
                                onClick = {
                                    cameraGalleryManager.openGallery(hasGalleryPermission)
                                    showBottomSheet = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                BodyText(StringConstants.selectGallery)
                            }

                            OutlinedButton(
                                onClick = {
                                    cameraGalleryManager.openCamera(hasCameraPermission)
                                    showBottomSheet = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.CameraAlt, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                BodyText(StringConstants.takePhoto)
                            }
                        }
                    }
                }
            }
        }
    }
}

fun createImageUri(context: android.content.Context): Uri {
    val timestamp = java.text.SimpleDateFormat("yyyyMMdd_HHmmss", java.util.Locale.getDefault()).format(java.util.Date())
    val contentValues = android.content.ContentValues().apply {
        put(android.provider.MediaStore.Images.Media.DISPLAY_NAME, "JPEG_$timestamp.jpg")
        put(android.provider.MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
    }
    return context.contentResolver.insert(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
}
