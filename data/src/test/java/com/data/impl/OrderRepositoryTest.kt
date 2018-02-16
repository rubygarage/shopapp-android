package com.data.impl

import com.client.shop.getaway.Api
import com.client.shop.getaway.ApiCallback
import com.client.shop.getaway.entity.Error
import com.client.shop.getaway.entity.Order
import com.data.RxImmediateSchedulerRule
import com.domain.repository.OrderRepository
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.robolectric.annotation.Config

@Suppress("FunctionName")
@RunWith(MockitoJUnitRunner::class)
@Config(manifest = Config.NONE)
class OrderRepositoryTest {

    companion object {
        private const val perPage = 5
        private const val paginationValue = "pagination"
    }

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var api: Api

    @Mock
    private lateinit var orderList: List<Order>

    private lateinit var repository: OrderRepository

    private lateinit var observer: TestObserver<List<Order>>

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        repository = OrderRepositoryImpl(api)
        observer = TestObserver()
    }

    @Test
    fun getOrdersShouldDelegateCallToApi() {
        repository.getOrdersList(perPage, paginationValue).subscribe()
        verify(api).getOrders(eq(perPage), eq(paginationValue), any())
    }

    @Test
    fun getOrdersShouldReturnValueWhenOnResult() {
        given(api.getOrders(eq(perPage), eq(paginationValue), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<List<Order>>>(2)
            callback.onResult(orderList)
        })
        repository.getOrdersList(perPage, paginationValue).subscribe(observer)
        observer.assertValue(orderList)
    }

    @Test
    fun getOrdersShouldReturnErrorOnFailure() {
        val error = Error.Content()
        given(api.getOrders(eq(perPage), eq(paginationValue), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<List<Order>>>(2)
            callback.onFailure(error)
        })
        repository.getOrdersList(perPage, paginationValue).subscribe(observer)
        observer.assertError(error)
    }
}