package com.example.myapplication.util

import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.util.Log
import com.example.myapplication.data.HealthRecord
import org.json.JSONObject
import java.nio.charset.Charset

object NfcHandler {
    // Shorter MIME type to save bytes
    private const val MIME_TYPE = "m/p" 

    fun createNdefMessage(record: HealthRecord): NdefMessage {
        // Create a highly compressed JSON object with single-letter keys
        val compressed = JSONObject().apply {
            put("n", record.personalInfo.name.take(20)) // Name
            put("b", record.bloodType) // Blood Type
            
            if (record.allergies.isNotEmpty()) {
                put("a", record.allergies.joinToString(",").take(30)) // Allergies
            }
            
            if (record.medicalConditions.isNotEmpty()) {
                put("c", record.medicalConditions.joinToString(",").take(30)) // Conditions
            }
            
            record.emergencyContact?.let {
                put("e", "${it.name.take(10)}:${it.phone}") // Emergency Contact
            }
        }

        val payload = compressed.toString().toByteArray(Charset.forName("UTF-8"))
        val ndefRecord = NdefRecord.createMime(MIME_TYPE, payload)
        
        return NdefMessage(arrayOf(ndefRecord))
    }

    fun writeTag(tag: Tag, message: NdefMessage): Result<Unit> {
        val ndef = Ndef.get(tag) ?: return Result.failure(Exception("Tag does not support NDEF"))
        
        return try {
            ndef.connect()
            if (!ndef.isWritable) {
                ndef.close()
                return Result.failure(Exception("Tag is read-only"))
            }
            
            val size = message.toByteArray().size
            if (ndef.maxSize < size) {
                val error = "Tag too small: ${ndef.maxSize} bytes available, need $size"
                ndef.close()
                return Result.failure(Exception(error))
            }

            ndef.writeNdefMessage(message)
            ndef.close()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("NfcHandler", "Error writing to tag", e)
            Result.failure(e)
        }
    }
}
