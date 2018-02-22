package com.shopapp.ui.order.details.contract

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.inOrder
import com.shopapp.domain.interactor.order.OrderDetailsUseCase
import com.shopapp.gateway.entity.Error
import com.shopapp.gateway.entity.Order
import com.shopapp.util.RxImmediateSchedulerRule
import com.shopapp.util.ext.mock
import io.reactivex.Single
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class OrderDetailsPresenterTest {

    companion object {
        private const val orderId = "orderId"
    }

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var view: OrderDetailsView

    @Mock
    private lateinit var useCase: OrderDetailsUseCase

    @Mock
    private lateinit var order: Order

    private lateinit var presenter: OrderDetailsPresenter

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        presenter = OrderDetailsPresenter(useCase)
        presenter.attachView(view)
        useCase.mock()
    }

    @After
    fun tearDown() {
        presenter.detachView(false)
    }

    @Test
    fun shouldShowContentOnSingleSuccess() {
        given(useCase.buildUseCaseSingle(any())).willReturn(Single.just(order))
        presenter.loadOrderDetails(orderId)

        val inOrder = inOrder(view, useCase)
        inOrder.verify(useCase).execute(any(), any(), eq(orderId))
        inOrder.verify(view).showContent(order)
    }

    @Test
    fun shouldShowMessageOnUseCaseNonCriticalError() {
        given(useCase.buildUseCaseSingle(any())).willReturn(Single.error(Error.NonCritical("ErrorMessage")))
        presenter.loadOrderDetails(orderId)

        val inOrder = inOrder(view, useCase)
        inOrder.verify(useCase).execute(any(), any(), eq(orderId))
        inOrder.verify(view).showMessage("ErrorMessage")
    }

    @Test
    fun shouldShowErrorOnUseCaseContentError() {
        given(useCase.buildUseCaseSingle(any())).willReturn(Single.error(Error.Content(false)))
        presenter.loadOrderDetails(orderId)

        val inOrder = inOrder(view, useCase)
        inOrder.verify(useCase).execute(any(), any(), eq(orderId))
        inOrder.verify(view).showError(false)
    }
}