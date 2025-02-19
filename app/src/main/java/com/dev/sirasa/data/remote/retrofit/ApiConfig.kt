package com.dev.sirasa.data.remote.retrofit

import com.dev.sirasa.BuildConfig
import com.dev.sirasa.data.local.UserPreference
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        private const val BASE_URL = BuildConfig.API_BASE_URL
        var API_TESTING: String? = null
        fun getApiService(userPreference: UserPreference) : ApiService {
            val loggingInterceptor = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }

            val authInterceptor = Interceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                val token = runBlocking { userPreference.getSession().firstOrNull()?.token }
                if (token != null) {
                    if (token.isNotEmpty()) {
                        requestBuilder.addHeader("Authorization", "Bearer $token")
                    }
                }
                chain.proceed(requestBuilder.build())
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(authInterceptor)
                .build()
            val baseUrl = API_TESTING ?: BASE_URL

            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}