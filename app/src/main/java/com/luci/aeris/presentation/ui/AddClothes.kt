package com.luci.aeris.presentation.ui

import BodyText
import android.Manifest
import android.R.attr.text
import android.app.Activity
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.luci.aeris.utils.navigator.Navigator
import com.luci.aeris.presentation.viewmodel.AddClothesViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luci.aeris.utils.constants.StringConstants
import kotlinx.coroutines.launch
import retrofit2.http.Body

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddClothes(
    navigator: Navigator,
    viewModel: AddClothesViewModel = viewModel()
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var showBottomSheet by remember { mutableStateOf(false) }
    val selectedImageUri by viewModel.selectedImageUri.collectAsState()
    val hasCameraPermission by viewModel.hasCameraPermission.collectAsState()
    val hasGalleryPermission by viewModel.hasGalleryPermission.collectAsState()
    val detectedType by viewModel.detectedType.collectAsState()
    val suitableConditions by viewModel.suitableConditions.collectAsState()
    val colorscheme = MaterialTheme.colorScheme
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val currentDate = remember {
        java.text.SimpleDateFormat(StringConstants.dateFormat, java.util.Locale.getDefault()).format(java.util.Date())
    }

    // Launchers
    val takePictureLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            viewModel.setSelectedImage(viewModel.cameraImageUri.value)
        }
    }

    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let { viewModel.setSelectedImage(it) }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        viewModel.updateCameraPermission(granted)
        if (granted) {
            viewModel.cameraImageUri.value?.let {
                takePictureLauncher.launch(it)
            } ?: run {
                val uri = createImageUri(context)
                viewModel.setCameraImageUri(uri)
                takePictureLauncher.launch(uri)
            }
        }
    }

    val galleryPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        viewModel.updateGalleryPermission(granted)
        if (granted) {
            pickImageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = { data ->
                    val isError = data.visuals.actionLabel == StringConstants.error
                    Snackbar(
                        containerColor = if (isError) colorscheme.errorContainer else colorscheme.primaryContainer,
                        contentColor = if (isError) colorscheme.onErrorContainer else colorscheme.onPrimaryContainer,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = if (isError) Icons.Default.Error else Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = if (isError) colorscheme.error else colorscheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            BodyText(text = data.visuals.message)
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
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
                if (selectedImageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(selectedImageUri),
                        contentDescription = StringConstants.pickedImage,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(20.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = colorscheme.surfaceVariant,
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
                                contentDescription = StringConstants.addAPhoto,
                                tint = colorscheme.onSurfaceVariant,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            BodyText(text = StringConstants.addAPhoto)
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                BodyText(
                    text = StringConstants.clotheDetail,
                    fontWeight = FontWeight.W700
                )
                Divider()
                Text(text = "${StringConstants.adddeOn} $currentDate")
                if (detectedType != null) {
                    Text(text = "${StringConstants.category}: $detectedType")
                }
                if (!suitableConditions.isNullOrEmpty()) {
                    Text(text = "${StringConstants.suitableCondition} ${suitableConditions.joinToString()}")
                }
            }

            if (selectedImageUri != null && detectedType != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Divider()
                    BodyText(text = "Model Tahmini", fontWeight = FontWeight.W700)
                    Text(text = "Tür: $detectedType")
                    Text(text = "Koşullar: ${suitableConditions.joinToString()}")

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        OutlinedButton(
                            onClick = {
                                viewModel.saveClothes { success, errorMessage ->
                                    coroutineScope.launch {
                                        if (success) {
                                            snackbarHostState.showSnackbar(
                                                message = StringConstants.clothesSuccesfly,
                                                actionLabel = StringConstants.success,
                                                duration = SnackbarDuration.Short
                                            )
                                            viewModel.clearSelection()
                                        } else {
                                            snackbarHostState.showSnackbar(
                                                message = "Hata: $errorMessage",
                                                actionLabel = StringConstants.error,
                                                duration = SnackbarDuration.Short
                                            )
                                        }
                                    }
                                }
                            }
                        ) {
                            BodyText("Kaydet")
                        }

                        OutlinedButton(
                            onClick = {
                                viewModel.clearSelection()
                            }
                        ) {
                            BodyText("İptal Et")
                        }
                    }
                }
            }

            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showBottomSheet = false },
                    sheetState = sheetState,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    containerColor = colorscheme.surface
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
                                if (hasGalleryPermission) {
                                    pickImageLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                } else {
                                    galleryPermissionLauncher.launch(
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                                            Manifest.permission.READ_MEDIA_IMAGES
                                        else
                                            Manifest.permission.READ_EXTERNAL_STORAGE
                                    )
                                }
                                showBottomSheet = false
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.PhotoLibrary, null, Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            BodyText(StringConstants.selectGallery)
                        }

                        OutlinedButton(
                            onClick = {
                                if (hasCameraPermission) {
                                    val uri = createImageUri(context)
                                    viewModel.setCameraImageUri(uri)
                                    takePictureLauncher.launch(uri)
                                } else {
                                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                                }
                                showBottomSheet = false
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.CameraAlt, null, Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            BodyText(StringConstants.takePhoto)
                        }
                    }
                }
            }
        }
    }
}


fun createImageUri(context: android.content.Context): Uri {
    val timestamp = java.text.SimpleDateFormat("yyyyMMdd_HHmmss", java.util.Locale.getDefault()).format(java.util.Date())
    val file = java.io.File(context.cacheDir, "IMG_$timestamp.jpg")
    return androidx.core.content.FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )
}
