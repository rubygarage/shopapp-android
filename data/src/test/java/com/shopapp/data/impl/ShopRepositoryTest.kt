package com.shopapp.data.impl

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.data.RxImmediateSchedulerRule
import com.shopapp.domain.repository.ShopRepository
import com.shopapp.gateway.Api
import com.shopapp.gateway.ApiCallback
import com.shopapp.gateway.entity.Config
import com.shopapp.gateway.entity.Error
import com.shopapp.gateway.entity.Shop
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ShopRepositoryTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var api: Api

    private lateinit var repository: ShopRepository
    private lateinit var shopObserver: TestObserver<Shop>
    private lateinit var configObserver: TestObserver<Config>

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        repository = ShopRepositoryImpl(api)
        shopObserver = TestObserver()
        configObserver = TestObserver()
    }

    @Test
    fun getShopShouldDelegateCallToApi() {
        repository.getShop().subscribe()
        verify(api).getShopInfo(any())
    }

    @Test
    fun getShopShouldCompleteOnApiResult() {
        val shop: Shop = mock()
        given(api.getShopInfo(any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Shop>>(0)
            callback.onResult(shop)
        })
        repository.getShop().subscribe(shopObserver)
        shopObserver.assertComplete()
        shopObserver.assertValue(shop)
    }

    @Test
    fun getShopShouldErrorOnApiFailure() {
        val error = Error.Content()
        given(api.getShopInfo(any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Shop>>(0)
            callback.onFailure(error)
        })
        repository.getShop().subscribe(shopObserver)
        shopObserver.assertError(error)
    }

    @Test
    fun getConfigShouldDelegateCallToApi() {
        repository.getConfig().subscribe()
        verify(api).getConfig(any())
    }

    @Test
    fun getConfigShouldCompleteOnApiResult() {
        val config: Config = mock()
        given(api.getConfig(any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Config>>(0)
            callback.onResult(config)
        })
        repository.getConfig().subscribe(configObserver)
        configObserver.assertComplete()
        configObserver.assertValue(config)
    }

    @Test
    fun getConfigShouldErrorOnApiFailure() {
        val error = Error.Content()
        given(api.getConfig(any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Config>>(0)
            callback.onFailure(error)
        })
        repository.getConfig().subscribe(configObserver)
        configObserver.assertError(error)
    }
}