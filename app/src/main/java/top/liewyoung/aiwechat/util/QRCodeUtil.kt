package top.liewyoung.aiwechat.util

import android.graphics.Bitmap
import android.graphics.Color
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import top.liewyoung.aiwechat.model.Contact

/** Utility for QR code generation and parsing */
object QRCodeUtil {

    private val gson = Gson()

    data class ContactQRData(val name: String, val prompt: String, val avatar: String = "😀")

    /** Generate QR code bitmap from contact data */
    fun generateQRCode(contact: Contact, size: Int = 512): Bitmap {
        val qrData =
                ContactQRData(name = contact.name, prompt = contact.prompt, avatar = contact.avatar)

        val jsonData = gson.toJson(qrData)

        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(jsonData, BarcodeFormat.QR_CODE, size, size)

        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
        for (x in 0 until size) {
            for (y in 0 until size) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }

        return bitmap
    }

    /** Parse contact data from QR code JSON string */
    fun parseContactData(qrContent: String): Contact? {
        return try {
            val qrData = gson.fromJson(qrContent, ContactQRData::class.java)
            Contact(
                    id = "", // Will be assigned by repository
                    name = qrData.name,
                    prompt = qrData.prompt,
                    avatar = qrData.avatar
            )
        } catch (e: Exception) {
            null
        }
    }
}
