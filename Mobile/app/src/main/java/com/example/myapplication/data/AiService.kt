package com.example.myapplication.data

import android.util.Log
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Serializable
data class ChatMessage(
    val role: String,
    val content: List<ContentPart>
) {
    constructor(role: String, text: String) : this(
        role,
        listOf(ContentPart(type = "text", text = text))
    )
}

@Serializable
data class ContentPart(
    val type: String,
    val text: String? = null,
    val image_url: ImageUrl? = null
)

@Serializable
data class ImageUrl(
    val url: String
)

@Serializable
data class ChatRequest(
    val messages: List<ChatMessage>,
    val model: String = "gpt-4o",
    val temperature: Double = 1.0,
    val top_p: Double = 1.0,
    val max_tokens: Int = 1000
)

@Serializable
data class ChatResponse(
    val choices: List<Choice>
)

@Serializable
data class Choice(
    val message: ResponseMessage
)

@Serializable
data class ResponseMessage(
    val role: String,
    val content: String
)

class AiService(private val getApiKey: suspend () -> String?) {

    private val json = Json { 
        ignoreUnknownKeys = true 
        encodeDefaults = true
    }
    private val endpoint = "https://models.inference.ai.azure.com/chat/completions"

    suspend fun getCompletion(messages: List<ChatMessage>): Result<String> = withContext(Dispatchers.IO) {
        try {
            val apiKey = getApiKey()
            if (apiKey.isNullOrBlank()) {
                return@withContext Result.failure(Exception("API Key is missing. Please set your GitHub Token in settings."))
            }
            
            val url = URL(endpoint)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Authorization", "Bearer $apiKey")
            connection.setRequestProperty("User-Agent", "MediPlus-Android-App")
            connection.doOutput = true
            connection.connectTimeout = 15000
            connection.readTimeout = 15000

            val requestBody = json.encodeToString(ChatRequest(messages = messages))

            connection.outputStream.use { 
                it.write(requestBody.toByteArray(Charsets.UTF_8)) 
            }

            val responseCode = connection.responseCode
            if (responseCode == 200) {
                val responseText = connection.inputStream.bufferedReader().readText()
                val response = json.decodeFromString<ChatResponse>(responseText)
                val content = response.choices.firstOrNull()?.message?.content ?: "No response from AI"
                Result.success(content)
            } else {
                val errorText = connection.errorStream?.bufferedReader()?.readText() ?: "No error details"
                Log.e("AiService", "API Error $responseCode: $errorText")
                Result.failure(Exception("AI Error ($responseCode): $errorText"))
            }
        } catch (e: Exception) {
            Log.e("AiService", "Exception: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun analyzeImage(base64Image: String, mode: String = "Food"): Result<String> {
        val prompt = when (mode) {
            "Food" -> "Identify the fruits or vegetables in this image. For each item, provide its name and the average nutritional abundance (calories, vitamins, minerals) per 100g. Format the output cleanly."
            "Document" -> "Analyze this medical document (prescription, lab report, or summary). Extract the key information like patient name, medications prescribed, test results, or doctor's advice. Summarize it clearly."
            "ID/Insurance" -> "Scan this health insurance card or hospital ID. Extract the provider name, policy number, member ID, and any other relevant identification details."
            "Symptom" -> "Analyze this image of a skin rash, injury, or symptom. Provide a general observation of what it looks like (without giving a formal medical diagnosis) and suggest if it's something that typically requires a doctor's visit. Advice on simple first aid if applicable."
            "Barcode" -> "Identify any medication barcodes or labels in this image. Extract the drug name, dosage, and manufacturer if possible."
            else -> "Identify and explain what is shown in this medical-related image."
        }

        val messages = listOf(
            ChatMessage(
                role = "user",
                content = listOf(
                    ContentPart(type = "text", text = prompt),
                    ContentPart(type = "image_url", image_url = ImageUrl(url = "data:image/jpeg;base64,$base64Image"))
                )
            )
        )
        return getCompletion(messages)
    }
}
