package com.example.myapplication.util

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.myapplication.data.HealthRecord
import java.io.File
import java.io.FileOutputStream

object ExportUtils {

    fun shareCardAsImage(context: Context, bitmap: Bitmap) {
        val cachePath = File(context.cacheDir, "shared_images")
        cachePath.mkdirs()
        val file = File(cachePath, "MediPlus_ID_Card.png")
        val stream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.close()

        val uri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Share MediPlus ID Card"))
    }

    fun pictureToBitmap(picture: Picture): Bitmap {
        val bitmap = Bitmap.createBitmap(picture.width, picture.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawPicture(picture)
        return bitmap
    }

    fun shareCardAsPdf(context: Context, record: HealthRecord) {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size
        val page = document.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint()

        // Background
        paint.color = Color.parseColor("#1E3C72")
        canvas.drawRect(50f, 50f, 545f, 300f, paint)

        // Text
        paint.color = Color.WHITE
        paint.textSize = 24f
        paint.isFakeBoldText = true
        canvas.drawText("MediPlus ID Card", 70f, 100f, paint)

        paint.textSize = 18f
        paint.isFakeBoldText = false
        canvas.drawText("Name: ${record.personalInfo.name}", 70f, 150f, paint)
        canvas.drawText("Blood Type: ${record.bloodType}", 70f, 180f, paint)
        canvas.drawText("Mobile: ${record.personalInfo.mobileNumber}", 70f, 210f, paint)
        
        if (record.medicalConditions.isNotEmpty()) {
            canvas.drawText("Conditions: ${record.medicalConditions.joinToString(", ")}", 70f, 240f, paint)
        }

        document.finishPage(page)

        val cachePath = File(context.cacheDir, "shared_images")
        cachePath.mkdirs()
        val file = File(cachePath, "MediPlus_Record.pdf")
        document.writeTo(FileOutputStream(file))
        document.close()

        val uri: Uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Share MediPlus PDF"))
    }
}
