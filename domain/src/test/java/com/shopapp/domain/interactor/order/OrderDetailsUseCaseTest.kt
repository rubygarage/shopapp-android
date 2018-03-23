package com.shopapp.domain.interactor.order

import com.nhaarman.mockito_kotlin.verify
import com.shopapp.domain.repository.OrderRepository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class OrderDetailsUseCaseTest {

    private lateinit var useCase: OrderDetailsUseCase

    @Mock
    private lateinit var orderRepository: OrderRepository

    @Before
    fun setUpTest() {
        useCase = OrderDetailsUseCase(orderRepository)
    }

    @Test
    fun shouldDelegateCallToRepository() {
        val orderId = "orderId"
        useCase.buildUseCaseSingle(orderId)
        verify(orderRepository).getOrder(orderId)
    }
}