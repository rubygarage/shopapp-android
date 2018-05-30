package com.shopapp.magento.api

import android.content.SharedPreferences
import com.nhaarman.mockito_kotlin.*
import com.shopapp.magento.api.Constant.ACCESS_KEY
import com.shopapp.magento.api.Constant.ACCESS_TOKEN
import com.shopapp.magento.retrofit.RestClient
import com.shopapp.magento.test.JsonFileHelper
import com.shopapp.magento.test.TestConstant.DEFAULT_HOST
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

abstract class BaseMagentoApiTest {

    @Rule
    @JvmField
    val server = MockWebServer()

    protected lateinit var api: MagentoApi
    protected val jsonHelper = JsonFileHelper()
    protected val sharedPreferences: SharedPreferences = mock()

    @Before
    fun setUp() {
        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(RestClient.createGson()))
            .build()

        api = MagentoApi(DEFAULT_HOST, retrofit, sharedPreferences)
        mockSharedPreferences()
    }

    private fun mockSharedPreferences() {
        val editor: SharedPreferences.Editor = mock()
        given(sharedPreferences.edit()).willReturn(editor)
        given(editor.putString(any(), any())).willReturn(editor)
        given(editor.putLong(any(), any())).willReturn(editor)
        given(editor.remove(any())).willReturn(editor)
    }

    protected fun mockSession(token: String?, password: String?) {
        given(sharedPreferences.getString(eq(ACCESS_TOKEN), anyOrNull())).willReturn(token)
        given(sharedPreferences.getString(eq(ACCESS_KEY), anyOrNull())).willReturn(password)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }
}