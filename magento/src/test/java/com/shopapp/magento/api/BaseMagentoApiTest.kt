package com.shopapp.magento.api

import com.shopapp.magento.retrofit.RestClient
import com.shopapp.magento.test.JsonFileHelper
import com.shopapp.magento.test.TestConstant.DEFAULT_HOST
import okhttp3.mockwebserver.MockWebServer
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

    @Before
    fun setUp() {
        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(RestClient.createGson()))
            .build()

        api = MagentoApi(DEFAULT_HOST, retrofit)
    }
}