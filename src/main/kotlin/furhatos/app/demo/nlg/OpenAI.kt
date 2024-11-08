package furhatos.app.demo.nlg

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class ChatGPT(private val apiKey: String) {

    private val client = OkHttpClient()

    suspend fun chat(message: String): String = suspendCancellableCoroutine { continuation ->
        val json = JsonObject()
        json.addProperty("model", "gpt-4o-mini")
        val messages = JsonArray()
        val systemMessage = JsonObject()
        systemMessage.addProperty("role", "system")
        systemMessage.addProperty("content", "You are a helpful assistant.")
        val userMessage = JsonObject()
        userMessage.addProperty("role", "user")
        userMessage.addProperty("content", message)
        messages.add(systemMessage)
        messages.add(userMessage)
        json.add("messages", messages)

        // Adding max_tokens property
        json.addProperty("max_tokens", 150)  // Adjust as needed

        val body = json.toString().toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(body)
            .build()

        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                continuation.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        continuation.resumeWithException(IOException("Unexpected code $response"))
                        return
                    }

                    val responseData = response.body?.string()
                    if (responseData == null) {
                        continuation.resumeWithException(IOException("Empty response body"))
                        return
                    }

                    try {
                        val jsonResponse = JsonParser.parseString(responseData).asJsonObject

                        val choicesArray = jsonResponse.get("choices")?.asJsonArray
                        if (choicesArray == null || choicesArray.size() == 0) {
                            throw IOException("Invalid API response: Missing or empty 'choices' array")
                        }

                        val messageObject = choicesArray[0].asJsonObject.get("message")?.asJsonObject
                            ?: throw IOException("Invalid API response: Missing 'message' object")
                        val reply = messageObject.get("content")?.asString
                            ?: throw IOException("Invalid API response: Missing 'content'")  // Safe call

                        continuation.resume(reply)


                    } catch (e: JsonParseException) {
                        continuation.resumeWithException(IOException("Error parsing JSON response: ${e.message}"))
                    } catch (e: Exception) {  // Catch other potential exceptions
                        continuation.resumeWithException(IOException("Error processing API response: ${e.message}"))
                    }
                }
            }
        })

        continuation.invokeOnCancellation {
            call.cancel()
        }
    }
}