package com.shopapp.domain.interactor.order

import com.nhaarman.mockito_kotlin.verify
import com.shopapp.domain.repository.OrderRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class OrderListUseCaseTest {

    private lateinit var useCase: OrderListUseCase

    @Mock
    private lateinit var orderRepository: OrderRepository

    @Before
    fun setUpTest() {
        useCase = OrderListUseCase(orderRepository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        val perPage = 5
        val pagination = "pagination"
        val params = OrderListUseCase.Params(perPage, pagination)
        useCase.buildUseCaseSingle(params)
        verify(orderRepository).getOrderList(perPage, pagination)
    }
}