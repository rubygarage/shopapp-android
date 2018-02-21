package com.data.impl

import com.client.shop.gateway.Api
import com.client.shop.gateway.ApiCallback
import com.client.shop.gateway.entity.Error
import com.client.shop.gateway.entity.Order
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

@RunWith(MockitoJUnitRunner::class)
@Config(manifest = Config.NONE)
class OrderRepositoryTest {

    companion object {
        private const val perPage = 5
        private const val paginationValue = "pagination"
        private const val orderId = "orderId"
    }

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var api: Api

    @Mock
    private lateinit var orderList: List<Order>

    @Mock
    private lateinit var order: Order

    private lateinit var repository: OrderRepository
    private lateinit var orderListObserver: TestObserver<List<Order>>
    private lateinit var orderObserver: TestObserver<Order>

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        repository = OrderRepositoryImpl(api)
        orderListObserver = TestObserver()
        orderObserver = TestObserver()
    }

    @Test
    fun getOrderListShouldDelegateCallToApi() {
        repository.getOrderList(perPage, paginationValue).subscribe()
        verify(api).getOrders(eq(perPage), eq(paginationValue), any())
    }

    @Test
    fun getOrderListShouldReturnValueWhenOnResult() {
        given(api.getOrders(eq(perPage), eq(paginationValue), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<List<Order>>>(2)
            callback.onResult(orderList)
        })
        repository.getOrderList(perPage, paginationValue).subscribe(orderListObserver)
        orderListObserver.assertValue(orderList)
    }

    @Test
    fun getOrderListShouldReturnErrorOnFailure() {
        val error = Error.Content()
        given(api.getOrders(eq(perPage), eq(paginationValue), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<List<Order>>>(2)
            callback.onFailure(error)
        })
        repository.getOrderList(perPage, paginationValue).subscribe(orderListObserver)
        orderListObserver.assertError(error)
    }

    @Test
    fun getOrderShouldDelegateCallToApi() {
        repository.getOrder(orderId).subscribe()
        verify(api).getOrder(eq(orderId), any())
    }

    @Test
    fun getOrderShouldReturnValueWhenOnResult() {
        given(api.getOrder(eq(orderId), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Order>>(1)
            callback.onResult(order)
        })
        repository.getOrder(orderId).subscribe(orderObserver)
        orderObserver.assertValue(order)
    }

    @Test
    fun getOrderShouldReturnErrorOnFailure() {
        val error = Error.Content()
        given(api.getOrder(eq(orderId), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Order>>(1)
            callback.onFailure(error)
        })
        repository.getOrder(orderId).subscribe(orderObserver)
        orderObserver.assertError(error)
    }
}