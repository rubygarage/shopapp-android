package com.shopapp.magento.api.response

import com.shopapp.magento.retrofit.RestClient
import com.shopapp.magento.test.JsonFileHelper
import org.junit.Before

abstract class BaseResponseTest<T : Any>(private val clazz: Class<T>) {

    lateinit var response: T

    @Before
    fun setUp() {
        val fileHelper = JsonFileHelper()
        val json = fileHelper.getJsonContents(getFilename())
        response = RestClient.createGson().fromJson(json, clazz)
    }

    abstract fun getFilename(): String
}