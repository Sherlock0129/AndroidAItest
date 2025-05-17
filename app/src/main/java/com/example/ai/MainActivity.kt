// MainActivity.kt
package com.example.ai
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ChatScreen()
            }
        }
    }
}

@Composable
fun ChatScreen() {
    var userInput by remember { mutableStateOf("") }
    var chatHistory by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text(
            "DeepSeek AI Chat",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Text(chatHistory)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = userInput,
            onValueChange = { userInput = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Enter your message") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (userInput.isNotBlank()) {
                    isLoading = true
                    chatHistory += "\n\n👤: $userInput"
                    val messages = listOf(
                        Message("user", userInput)
                    )
                    val request = ChatRequest("deepseek-chat", messages)

                    RetrofitClient.api.chatCompletion(request).enqueue(object : Callback<ChatResponse> {
                        override fun onResponse(call: Call<ChatResponse>, response: Response<ChatResponse>) {
                            isLoading = false
                            val reply = response.body()?.choices?.firstOrNull()?.message?.content
                            chatHistory += "\n🤖: ${reply ?: "No response"}"
                        }

                        override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                            isLoading = false
                            chatHistory += "\n❌ Error: ${t.localizedMessage}"
                        }
                    })

                    userInput = ""
                }
            },
            enabled = !isLoading,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(if (isLoading) "Loading..." else "Send")
        }
    }
}
