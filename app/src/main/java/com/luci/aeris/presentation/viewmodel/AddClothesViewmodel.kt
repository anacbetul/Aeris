package com.luci.aeris.presentation.viewmodel

import Clothes
import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.yourapp.repository.ModelRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.luci.aeris.presentation.ui.createImageUri
import com.luci.aeris.utils.constants.StringConstants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class AddClothesViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri

    private val _cameraImageUri = MutableStateFlow<Uri?>(null)
    val cameraImageUri: StateFlow<Uri?> = _cameraImageUri

    private val _hasCameraPermission = MutableStateFlow(
        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
    )
    val hasCameraPermission: StateFlow<Boolean> = _hasCameraPermission

    private val _hasGalleryPermission = MutableStateFlow(
        ContextCompat.checkSelfPermission(
            context,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                Manifest.permission.READ_MEDIA_IMAGES
            else
                Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    )
    val hasGalleryPermission: StateFlow<Boolean> = _hasGalleryPermission

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _detectedType = MutableStateFlow<String?>(null)
    val detectedType: StateFlow<String?> = _detectedType

    private val _suitableConditions = MutableStateFlow<List<String>>(emptyList())
    val suitableConditions: StateFlow<List<String>> = _suitableConditions
    private val modelRepository = ModelRepository(application)

    fun updateCameraPermission(granted: Boolean) {
        _hasCameraPermission.value = granted
        if (granted) {
            val uri = createImageUri(context)
            _cameraImageUri.value = uri
        }
    }

    fun updateGalleryPermission(granted: Boolean) {
        _hasGalleryPermission.value = granted
    }

    fun setSelectedImage(uri: Uri?) {
        _selectedImageUri.value = uri
        if (uri != null) {
            analyzeImage(uri)
        }
    }

    fun setCameraImageUri(uri: Uri?) {
        _cameraImageUri.value = uri
    }

    private fun analyzeImage(uri: Uri) {
        viewModelScope.launch {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            val predictedLabel = modelRepository.predict(bitmap)
            _detectedType.value = predictedLabel
            _suitableConditions.value = listOf("Soğuk", "Rüzgarlı") // Bunu da modele entegre edebilirsiniz
        }
    }
    fun saveClothes(onResult: (Boolean, String?) -> Unit) {
        val photoUri = _selectedImageUri.value
        val type = _detectedType.value
        val conditions = _suitableConditions.value

        if (photoUri == null) {
            onResult(false, StringConstants.photoNotSelected)
            return
        }

        val userId = auth.currentUser?.uid
        if (userId == null) {
            onResult(false, StringConstants.userSessionNotFound)
            return
        }

        viewModelScope.launch {
            try {
                val newClothes = Clothes(
                    id = UUID.randomUUID().toString(),
                    photoPath = photoUri.toString(),
                    dateAdded = SimpleDateFormat(StringConstants.dateFormat, Locale.getDefault()).format(Date()),
                    type = type ?: "Bilinmeyen",
                    suitableWeather = conditions
                )

                firestore.collection(StringConstants.users)
                    .document(userId)
                    .collection(StringConstants.clothes)
                    .document(newClothes.id)
                    .set(newClothes)
                    .await()

                onResult(true, null)
            } catch (e: Exception) {
                onResult(false, e.message)
            }
        }
    }

    fun clearSelection() {
        _selectedImageUri.value = null
        _detectedType.value = null
        _suitableConditions.value = emptyList()
    }
}
