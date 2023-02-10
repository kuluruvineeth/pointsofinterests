package com.kuluruvineeth.data.features.poi.datasource

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.datetime.Clock
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class LocalImageDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {

    @RequiresApi(Build.VERSION_CODES.Q)
    suspend fun copyLocalImage(uri: String): String = suspendCoroutine {
        continuation ->
            val fileName = String.format(LOCAL_IMAGE_NAME, Clock.System.now().toEpochMilliseconds())
            val imageFile = File(context.externalCacheDir,fileName)
            imageFile.createNewFile()
            FileOutputStream(imageFile).use { outputStream ->
                context.contentResolver.openInputStream(Uri.parse(uri)).use { inputStream ->
                    inputStream?.let { steam ->
                        kotlin.runCatching {
                            copy(steam,outputStream)
                        }.onFailure {
                            continuation.resumeWithException(it)
                        }
                        outputStream.flush()
                        continuation.resume(Uri.fromFile(imageFile).toString())
                    } ?:
                    continuation.resumeWithException(NullPointerException("InputStream is null"))
                }
            }
    }

    private fun copy(inputStream: InputStream, outputStream: OutputStream){
        try {
            var length: Int
            val buffer = ByteArray(1024 * 4)
            while (inputStream.read(buffer).also { length = it } > 0){
                outputStream.write(buffer, 0, length)
            }
        }finally {
            inputStream.close()
            outputStream.close()
        }
    }

    companion object{
        private const val LOCAL_IMAGE_NAME = "poi_image_%s.jpg"
    }
}