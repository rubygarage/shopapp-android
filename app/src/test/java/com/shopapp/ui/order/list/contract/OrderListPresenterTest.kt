package com.shopapp.ui.order.list.contract

import com.nhaarman.mockito_kotlin.*
import com.shopapp.domain.interactor.order.OrderListUseCase
import com.shopapp.gateway.entity.Error
import com.shopapp.gateway.entity.Order
import com.shopapp.test.RxImmediateSchedulerRule
import com.shopapp.test.ext.mock
import io.reactivex.Single
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class OrderListPresenterTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var view: OrderListView

    @Mock
    private lateinit var useCase: OrderListUseCase

    @Mock
    private lateinit var orderList: List<Order>

    private lateinit var presenter: OrderListPresenter


    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        presenter = OrderListPresenter(useCase)
        presenter.attachView(view)
        useCase.mock()
    }

    @Test
    fun shouldShowContentOnSingleSuccess() {
        given(useCase.buildUseCaseSingle(any())).willReturn(Single.just(orderList))
        presenter.getOrders(1, null)

        val inOrder = inOrder(view, useCase)
        inOrder.verify(useCase).execute(any(), any(), eq(OrderListUseCase.Params(1, null)))
        inOrder.verify(view).showContent(orderList)
    }

    @Test
    fun shouldShowMessageOnUseCaseNonCriticalError() {
        given(useCase.buildUseCaseSingle(any())).willReturn(Single.error(Error.NonCritical("ErrorMessage")))
        presenter.getOrders(1, null)

        val inOrder = inOrder(view, useCase)
        inOrder.verify(useCase).execute(any(), any(), eq(OrderListUseCase.Params(1, null)))
        inOrder.verify(view).showMessage("ErrorMessage")
    }

    @Test
    fun shouldShowErrorOnUseCaseContentError() {
        given(useCase.buildUseCaseSingle(any())).willReturn(Single.error(Error.Content()))
        presenter.getOrders(1, null)

        val inOrder = inOrder(view, useCase)
        inOrder.verify(useCase).execute(any(), any(), eq(OrderListUseCase.Params(1, null)))
        argumentCaptor<Error>().apply {
            inOrder.verify(view).showError(capture())
            assertTrue(firstValue is Error.Content)
        }
    }

    @After
    fun tearDown() {
        presenter.detachView(false)
    }
}
