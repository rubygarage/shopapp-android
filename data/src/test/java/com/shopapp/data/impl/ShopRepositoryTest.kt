package com.shopapp.data.impl

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.data.RxImmediateSchedulerRule
import com.shopapp.domain.repository.ShopRepository
import com.shopapp.gateway.Api
import com.shopapp.gateway.ApiCallback
import com.shopapp.gateway.entity.Error
import com.shopapp.gateway.entity.Shop
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

class ShopRepositoryTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var api: Api

    private lateinit var repository: ShopRepository
    private lateinit var shopObserver: TestObserver<Shop>

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        repository = ShopRepositoryImpl(api)
        shopObserver = TestObserver()
    }

    @Test
    fun changePasswordShouldDelegateCallToApi() {
        repository.getShop().subscribe()
        verify(api).getShopInfo(any())
    }

    @Test
    fun changePasswordShouldCompleteOnApiResult() {
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
    fun changePasswordShouldErrorOnApiFailure() {
        val error = Error.Content()
        given(api.getShopInfo(any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Shop>>(0)
            callback.onFailure(error)
        })
        repository.getShop().subscribe(shopObserver)
        shopObserver.assertError(error)
    }

}