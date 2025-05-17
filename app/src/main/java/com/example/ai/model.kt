// model.kt
package com.example.ai

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.Call

data class Message(val role: String, val content: String)
data class ChatRequest(val model: String, val messages: List<Message>)
data class ChatResponse(val choices: List<Choice>)
data class Choice(val message: Message)

interface DeepSeekApi {
    @Headers("Content-Type: application/json")
    @POST("v1/chat/completions")
    fun chatCompletion(
        @Body request: ChatRequest
    ): Call<ChatResponse>
}
