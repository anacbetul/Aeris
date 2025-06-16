package com.luci.aeris.utils

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.luci.aeris.utils.constants.StringConstants
import java.text.SimpleDateFormat
import java.util.*

class CameraGalleryManager(
    private val context: Context,
    private val pickImageLauncher: ActivityResultLauncher<PickVisualMediaRequest>,
    private val takePictureLauncher: ActivityResultLauncher<Uri>,
    private val requestCameraPermission: () -> Unit,
    private val requestGalleryPermission: () -> Unit,
    private val showSettingsSnackbar: (String) -> Unit,
    private val setCameraImageUri: (Uri) -> Unit,
    private val createImageUri: () -> Uri
) {
    private var galleryDeniedCount = 0
    private var cameraDeniedCount = 0

    fun openCamera(hasCameraPermission: Boolean) {
        if (hasCameraPermission) {
            val uri = createImageUri()
            setCameraImageUri(uri)
            takePictureLauncher.launch(uri)
        } else {
            cameraDeniedCount++
            if (cameraDeniedCount >= 3) {
                showSettingsSnackbar(StringConstants.cameraDenied)
            } else {
                requestCameraPermission()
            }
        }
    }

    fun openGallery(hasGalleryPermission: Boolean) {
        if (hasGalleryPermission) {
            pickImageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else {
            galleryDeniedCount++
            if (galleryDeniedCount >= 3) {
                showSettingsSnackbar(StringConstants.galleryDenied)
            } else {
                requestGalleryPermission()
            }
        }
    }

    private fun createImageUri(context: Context): Uri {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "JPEG_$timestamp.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        return context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
    }
}
