package com.example.testapplogin.common.extensions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

fun String.encodeImageToBase64(): String {
    val imageBitmap: Bitmap = BitmapFactory.decodeFile(this)
    val desiredWidth = 800 // Set the desired width for resizing
    val desiredHeight = 600 // Set the desired height for resizing
    val resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, desiredWidth, desiredHeight, false)
    val outputStream = ByteArrayOutputStream()
    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 30, outputStream) // Adjust the compression quality as needed
    val imageBytes = outputStream.toByteArray()
    val base64String = Base64.encodeToString(imageBytes, Base64.DEFAULT)

    return base64String
}
