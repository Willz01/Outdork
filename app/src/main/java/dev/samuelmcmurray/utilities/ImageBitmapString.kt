package dev.samuelmcmurray.utilities

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream


class ImageBitmapString {

    @TypeConverter
    fun BitMapToString(bitmap: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        val temp = Base64.encodeToString(b, Base64.DEFAULT)

        if (temp == null) {
            return null;
        } else {
            return temp;
        }
    }

    @TypeConverter
    fun StringToBitMap(encodedString: String): Bitmap? {
        try {
            val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)

            if (bitmap == null) {
                return null;
            } else {
                return bitmap;
            }

        } catch (e: Exception) {
            Log.d(TAG, "StringToBitMap: $e")
            return null;
        }
    }
}