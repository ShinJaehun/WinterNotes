package com.shinjaehun.winternotes.common

import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import java.io.*

object FileUtils {
    lateinit var application: Application

    lateinit var cRes : ContentResolver

    @Throws(IOException::class)
    fun getInputStream(uri: Uri): InputStream? {
        return if (isVirtualFile(uri)) {
            getInputStreamForVirtualFile(uri, getMimeType(uri))
        } else {
            cRes.openInputStream(uri)
        }
    }

    fun getMimeType(uri: Uri): String? {
        return cRes.getType(uri)
    }

    private fun isVirtualFile(uri: Uri): Boolean {
        if (!DocumentsContract.isDocumentUri(application, uri)) {
            return false
        }

        val cursor = cRes.query(uri, arrayOf(DocumentsContract.Document.COLUMN_FLAGS), null, null, null)
        val flags: Int = cursor?.use {
            if (cursor.moveToFirst()) {
                cursor.getInt(0)
            } else {
                0
            }
        } ?: 0

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            flags and DocumentsContract.Document.FLAG_VIRTUAL_DOCUMENT != 0 // 이게 대체 뭐야?
        } else {
            false
        }
    }

    @Throws(IOException::class)
    private fun getInputStreamForVirtualFile(uri: Uri, mimeTypeFilter: String?): FileInputStream? {
        if (mimeTypeFilter==null) {
            throw FileNotFoundException()
        }
        val openableMimeTypes: Array<String>? = cRes.getStreamTypes(uri, mimeTypeFilter)

        return if (openableMimeTypes?.isNotEmpty() == true) {
            cRes.openTypedAssetFileDescriptor(uri, openableMimeTypes[0], null)?.createInputStream()
        } else {
            throw FileNotFoundException()
        }
    }

    fun copyStreamToFile(inputStream: InputStream, outputFile: File) {
        inputStream.use { input ->
            val outputStream = FileOutputStream(outputFile)
            outputStream.use { output ->
                val buffer = ByteArray(4 * 1024)
                while(true){
                    val byteCount = input.read(buffer)
                    if (byteCount < 0) break
                    output.write(buffer, 0, byteCount)
                }
                output.flush()
            }
            outputStream.close()
        }
    }

}