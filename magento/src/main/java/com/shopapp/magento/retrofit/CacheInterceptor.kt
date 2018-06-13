package com.shopapp.magento.retrofit

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class CacheInterceptor(
    private val longCachedUrls: List<String>,
    private val shortCachedUrls: List<String>
) : Interceptor {

    companion object {
        const val PRAGMA_HEADER = "Pragma"
        const val CACHE_CONTROL_HEADER = "Cache-Control"
        private const val NO_CACHE_TIME = 0
        private const val LONG_CACHE_TIME = 60
        private const val SHORT_CACHE_TIME = 1
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        val url = response.request().url().encodedPath()
        var cacheAge = NO_CACHE_TIME
        cacheAge = longCachedUrls.getUrlCacheTime(url, LONG_CACHE_TIME, cacheAge)
        cacheAge = shortCachedUrls.getUrlCacheTime(url, SHORT_CACHE_TIME, cacheAge)

        return if (cacheAge == NO_CACHE_TIME) {
            response
        } else {
            val cacheControl = CacheControl.Builder()
                .onlyIfCached()
                .maxAge(cacheAge, TimeUnit.MINUTES)
                .build()

            response.newBuilder()
                .removeHeader(PRAGMA_HEADER)
                .removeHeader(CACHE_CONTROL_HEADER)
                .header(CACHE_CONTROL_HEADER, cacheControl.toString())
                .build()
        }
    }

    private fun List<String>.getUrlCacheTime(url: String, cacheTime: Int, defaultCacheTime: Int) =
        find { url.endsWith(it) }?.let { cacheTime } ?: defaultCacheTime
}