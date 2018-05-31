package com.shopapp.magento.api.retrofit

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.magento.retrofit.CacheInterceptor
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class CacheInterceptorTest {

    companion object {
        private const val CACHED_URL = "product/path"
    }

    @Mock
    private lateinit var mockedChain: Interceptor.Chain
    @Mock
    private lateinit var mockedRequest: Request
    @Mock
    private lateinit var mockedResponse: Response
    @Mock
    private lateinit var mockedUrl: HttpUrl
    @Mock
    private lateinit var responseBuilder: Response.Builder

    private lateinit var interceptor: Interceptor

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        given(mockedChain.request()).willReturn(mockedRequest)
        given(mockedChain.proceed(mockedRequest)).willReturn(mockedResponse)
        given(mockedResponse.request()).willReturn(mockedRequest)
        given(mockedRequest.url()).willReturn(mockedUrl)
        given(mockedResponse.newBuilder()).willReturn(responseBuilder)
        given(responseBuilder.removeHeader(any())).willReturn(responseBuilder)
        given(responseBuilder.header(any(), any())).willReturn(responseBuilder)
        given(responseBuilder.build()).willReturn(mockedResponse)

        interceptor = CacheInterceptor(CACHED_URL)
    }

    @Test
    fun shouldSetShortCacheAge() {
        given(mockedUrl.encodedPath()).willReturn("test/category/path")

        interceptor.intercept(mockedChain)

        verify(responseBuilder).removeHeader(CacheInterceptor.PRAGMA_HEADER)
        verify(responseBuilder).removeHeader(CacheInterceptor.CACHE_CONTROL_HEADER)
        verify(responseBuilder).header(CacheInterceptor.CACHE_CONTROL_HEADER, "max-age=60, only-if-cached")
    }

    @Test
    fun shouldSetLongCacheAge() {
        given(mockedUrl.encodedPath()).willReturn("test/product/path")

        interceptor.intercept(mockedChain)

        verify(responseBuilder).removeHeader(CacheInterceptor.PRAGMA_HEADER)
        verify(responseBuilder).removeHeader(CacheInterceptor.CACHE_CONTROL_HEADER)
        verify(responseBuilder).header(CacheInterceptor.CACHE_CONTROL_HEADER, "max-age=3600, only-if-cached")
    }
}