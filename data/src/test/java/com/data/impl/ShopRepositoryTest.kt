package com.data.impl

import com.client.shop.gateway.Api
import com.client.shop.gateway.ApiCallback
import com.client.shop.gateway.entity.Error
import com.client.shop.gateway.entity.Shop
import com.data.RxImmediateSchedulerRule
import com.domain.repository.ShopRepository
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ShopRepositoryTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var api: Api

    private lateinit var repository: ShopRepository

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        repository = ShopRepositoryImpl(api)
    }

    @Test
    fun changePasswordShouldDelegateCallToApi() {
        repository.getShop().subscribe()
        verify(api).getShopInfo(any())
    }

    @Test
    fun changePasswordShouldCompleteOnApiResult() {
        val shop: Shop = mock()
        val observer = TestObserver<Shop>()
        given(api.getShopInfo(any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Shop>>(0)
            callback.onResult(shop)
        })
        repository.getShop().subscribe(observer)
        observer.assertComplete()
        observer.assertValue(shop)
    }

    @Test
    fun changePasswordShouldErrorOnApiFailure() {
        val error = Error.Content()
        val observer = TestObserver<Shop>()
        given(api.getShopInfo(any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Shop>>(0)
            callback.onFailure(error)
        })
        repository.getShop().subscribe(observer)
        observer.assertError(error)
    }

}