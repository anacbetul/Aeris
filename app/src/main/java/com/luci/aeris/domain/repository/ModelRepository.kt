package com.example.yourapp.repository

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import java.io.*
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel

class ModelRepository(private val context: Context) {

    private var interpreter: Interpreter? = null
    private val labels: List<String>

    private val MODEL_NAME = "mobilenet_giyim_modeli_datagen_dropout.tflite"
    private val LABEL_NAME = "labels.txt"

    private val INPUT_IMAGE_WIDTH = 128
    private val INPUT_IMAGE_HEIGHT = 128
    private val INPUT_IMAGE_CHANNELS = 3

    init {
        loadModel()
        labels = loadLabels()
    }

    private fun loadModel() {
        val assetFileDescriptor = context.assets.openFd(MODEL_NAME)
        val fileInputStream = FileInputStream(assetFileDescriptor.fileDescriptor)
        val fileChannel = fileInputStream.channel
        val startOffset = assetFileDescriptor.startOffset
        val declaredLength = assetFileDescriptor.declaredLength
        val modelBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
        interpreter = Interpreter(modelBuffer)
    }

    private fun loadLabels(): List<String> {
        val labelList = mutableListOf<String>()
        val inputStream: InputStream = context.assets.open(LABEL_NAME)
        val reader = BufferedReader(InputStreamReader(inputStream))
        var line: String? = reader.readLine()
        while (line != null) {
            labelList.add(line)
            line = reader.readLine()
        }
        return labelList
    }

    fun predict(bitmap: Bitmap): String {
        val resized = Bitmap.createScaledBitmap(bitmap, INPUT_IMAGE_WIDTH, INPUT_IMAGE_HEIGHT, true)
        val inputBuffer = convertBitmapToByteBuffer(resized)

        val output = Array(1) { FloatArray(labels.size) }
        interpreter?.run(inputBuffer, output)

        val maxIndex = output[0].indices.maxByOrNull { output[0][it] } ?: -1
        return labels.getOrElse(maxIndex) { "Unknown" }
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * INPUT_IMAGE_WIDTH * INPUT_IMAGE_HEIGHT * INPUT_IMAGE_CHANNELS)
        byteBuffer.order(ByteOrder.nativeOrder())

        for (y in 0 until INPUT_IMAGE_HEIGHT) {
            for (x in 0 until INPUT_IMAGE_WIDTH) {
                val pixel = bitmap.getPixel(x, y)

                // ResNet50 için [-1, 1] aralığına normalize et
                val r = ((pixel shr 16 and 0xFF) / 127.5f) - 1.0f
                val g = ((pixel shr 8 and 0xFF) / 127.5f) - 1.0f
                val b = ((pixel and 0xFF) / 127.5f) - 1.0f

                byteBuffer.putFloat(r)
                byteBuffer.putFloat(g)
                byteBuffer.putFloat(b)
            }
        }

        return byteBuffer
    }
}
