package com.domain.interactors

import com.domain.interactor.order.OrderListUseCase
import com.domain.repository.OrderRepository
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.robolectric.annotation.Config

@Suppress("FunctionName")
@RunWith(MockitoJUnitRunner::class)
@Config(manifest = Config.NONE)
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
        verify(orderRepository).getOrdersList(perPage, pagination)
    }
}