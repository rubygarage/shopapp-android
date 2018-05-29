package com.shopapp.magento.api

import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.gateway.ApiCallback
import com.shopapp.gateway.entity.Config
import org.junit.Assert.assertNotNull
import org.junit.Test

class MagentoApiShopTest : BaseMagentoApiTest() {

    @Test
    fun getConfigShouldReturnConfig() {
        val callback: ApiCallback<Config> = mock()
        api.getConfig(callback)

        argumentCaptor<Config>().apply {
            verify(callback).onResult(capture())

            assertNotNull(firstValue)
        }
    }
}