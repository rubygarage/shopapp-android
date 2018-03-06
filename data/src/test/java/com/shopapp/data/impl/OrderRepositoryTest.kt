package com.shopapp.data.impl

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.data.RxImmediateSchedulerRule
import com.shopapp.domain.repository.OrderRepository
import com.shopapp.gateway.Api
import com.shopapp.gateway.ApiCallback
import com.shopapp.gateway.entity.Error
import com.shopapp.gateway.entity.Order
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

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