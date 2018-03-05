package com.shopapp.ui.cart.contract

import com.nhaarman.mockito_kotlin.*
import com.shopapp.domain.interactor.cart.CartItemsUseCase
import com.shopapp.gateway.entity.CartProduct
import com.shopapp.test.RxImmediateSchedulerRule
import com.shopapp.test.ext.mock
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class CartWidgetPresenterTest {

    companion object {
        private const val PRODUCT_COUNT = 5
    }

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var view: CartWidgetView

    @Mock
    private lateinit var cartItemsUseCase: CartItemsUseCase

    private lateinit var presenter: CartWidgetPresenter

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        presenter = CartWidgetPresenter(cartItemsUseCase)
        cartItemsUseCase.mock()
    }

    @Test
    fun shouldCallUseCaseOnLoadItems() {
        val products: List<CartProduct> = mock()
        given(cartItemsUseCase.buildUseCaseObservable(any())).willReturn(Observable.just(products))
        presenter.attachView(view)
        verify(cartItemsUseCase).execute(any(), any(), any(), any())
    }

    @Test
    fun shouldChangeBadgeCount() {
        val products: List<CartProduct> = mock {
            on { size } doReturn PRODUCT_COUNT
        }
        given(cartItemsUseCase.buildUseCaseObservable(any())).willReturn(Observable.just(products))
        presenter.attachView(view)
        verify(view).changeBadgeCount(PRODUCT_COUNT)
    }

    @Test
    fun shouldPrintStackTraceOnRemoveProductError() {
        val error: Throwable = mock()
        given(cartItemsUseCase.buildUseCaseObservable(any())).willReturn(Observable.error(error))
        presenter.attachView(view)
        verify(error).printStackTrace()
    }
}