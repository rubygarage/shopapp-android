package com.shopapp.magento.retrofit

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class CacheInterceptor(private vararg val cachedUrls: String) : Interceptor {

    companion object {
        const val PRAGMA_HEADER = "Pragma"
        const val CACHE_CONTROL_HEADER = "Cache-Control"
        private const val SHORT_CACHE_TIME = 1
        private const val LONG_CACHE_TIME = 60
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        val url = response.request().url().encodedPath()
        var cacheAge = SHORT_CACHE_TIME
        for (path in cachedUrls) {
            if (url.endsWith(path)) {
                cacheAge = LONG_CACHE_TIME
                break
            }
        }

        val cacheControl = CacheControl.Builder()
            .onlyIfCached()
            .maxAge(cacheAge, TimeUnit.MINUTES)
            .build()

        return response.newBuilder()
            .removeHeader(PRAGMA_HEADER)
            .removeHeader(CACHE_CONTROL_HEADER)
            .header(CACHE_CONTROL_HEADER, cacheControl.toString())
            .build()
    }
}