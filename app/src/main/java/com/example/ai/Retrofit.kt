// RetrofitClient.kt
package com.example.ai
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.Interceptor

object RetrofitClient {
    private const val BASE_URL = "https://api.deepseek.com" // 替换为实际地址
    private const val API_KEY = "sk-0dd7411c6de74f119e9a06be144a89d6"

    private val client = OkHttpClient.Builder()
        .addInterceptor(Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $API_KEY")
                .build()
            chain.proceed(request)
        }).build()

    val api: DeepSeekApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(DeepSeekApi::class.java)
}
