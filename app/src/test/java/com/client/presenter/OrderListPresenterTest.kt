package com.client.presenter

import com.client.RxImmediateSchedulerRule
import com.client.presenter.ext.mockUseCase
import com.client.shop.getaway.entity.Error
import com.client.shop.getaway.entity.Order
import com.client.shop.ui.order.list.contract.OrderListPresenter
import com.client.shop.ui.order.list.contract.OrderListView
import com.domain.interactor.order.OrderListUseCase
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Suppress("FunctionName")
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
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
        useCase.mockUseCase()
    }

    @Test
    fun orderListPresenterTest_HappyCase() {
        given(useCase.buildUseCaseSingle(any())).willReturn(Single.just(orderList))
        presenter.getOrders(1, null)
        verify(useCase).execute(any(), any(), eq(OrderListUseCase.Params(1, null)))
        verify(view).showContent(orderList)
    }

    @Test
    fun orderListPresenterTest_SadCase_NonCriticalError() {
        given(useCase.buildUseCaseSingle(any())).willReturn(Single.error(Error.NonCritical("ErrorMessage")))
        presenter.getOrders(1, null)
        verify(useCase).execute(any(), any(), eq(OrderListUseCase.Params(1, null)))
        verify(view).showMessage("ErrorMessage")
    }

    @Test
    fun orderListPresenterTest_SadCase_ContentError() {
        given(useCase.buildUseCaseSingle(any())).willReturn(Single.error(Error.Content(false)))
        presenter.getOrders(1, null)
        verify(useCase).execute(any(), any(), eq(OrderListUseCase.Params(1, null)))
        verify(view).showError(false)
    }

}
