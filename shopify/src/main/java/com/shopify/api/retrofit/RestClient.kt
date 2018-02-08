package com.shopify.api.retrofit

import android.util.Base64
import com.shopify.api.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RestClient {

    private const val TIMEOUT: Long = 10

    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(providesOkHttp())
            .build()
    }

    private fun providesOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(getLoggingInterceptor())
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(getAuthInterceptor())
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    private fun getAuthInterceptor(): Interceptor {
        val tokenString = "${BuildConfig.PRIVATE_AUTH_KEY}:${BuildConfig.PRIVATE_AUTH_PASSWORD}"
        val base64Token = Base64.encodeToString(tokenString.toByteArray(), Base64.NO_WRAP)
        return Interceptor { chain ->
            val builder = chain.request().newBuilder()
            builder.addHeader("Authorization", "Basic $base64Token")
            return@Interceptor chain.proceed(builder.build())
        }
    }

    private fun getLoggingInterceptor(): Interceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

}