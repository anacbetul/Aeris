package com.luci.aeris.data

import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class RemoveBackgroundRepository(
    private val apiKey: String,
    private val client: OkHttpClient = OkHttpClient()
) {

    /**
     * Remove.bg API ile arka planı siler.
     * @param imageFile Silinmesi gereken arka planlı resim dosyası
     * @return Arka planı silinmiş PNG dosyasının Uri'si, başarısızsa null döner
     */
    suspend fun removeBackground(imageFile: File): Uri? {
        return withContext(Dispatchers.IO) {
            try {
                val requestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(
                        "image_file",
                        imageFile.name,
                        imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                    )
                    .build()

                val request = Request.Builder()
                    .url("https://api.remove.bg/v1.0/removebg")
                    .addHeader("X-Api-Key", apiKey)
                    .post(requestBody)
                    .build()

                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val bytes = response.body?.bytes()
                    if (bytes != null) {
                        // Aynı klasörde "removed_bg_" prefix'li dosya oluştur
                        val outputFile = File(imageFile.parentFile, "removed_bg_${imageFile.name}")
                        outputFile.writeBytes(bytes)

                        return@withContext Uri.fromFile(outputFile)
                    }
                }

                null
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}
