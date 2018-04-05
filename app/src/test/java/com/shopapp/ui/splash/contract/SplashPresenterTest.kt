package com.shopapp.ui.splash.contract

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.inOrder
import com.shopapp.domain.interactor.cart.CartValidationUseCase
import com.shopapp.gateway.entity.Error
import com.shopapp.test.RxImmediateSchedulerRule
import com.shopapp.test.ext.mock
import io.reactivex.Completable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class SplashPresenterTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var view: SplashView

    @Mock
    private lateinit var cartValidationUseCase: CartValidationUseCase

    private lateinit var presenter: SplashPresenter

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        presenter = SplashPresenter(cartValidationUseCase)
        presenter.attachView(view)
        cartValidationUseCase.mock()
    }

    @Test
    fun shouldShowContentWhenUseCaseComplete() {
        given(cartValidationUseCase.buildUseCaseCompletable(Unit)).willReturn(Completable.complete())
        presenter.validateItems()

        val inOrder = inOrder(view, cartValidationUseCase)
        inOrder.verify(cartValidationUseCase).execute(any(), any(), any())
        inOrder.verify(view).showContent(Unit)
    }

    @Test
    fun shouldCallValidationErrorWhenUseCaseReturnError() {
        given(cartValidationUseCase.buildUseCaseCompletable(Unit)).willReturn(Completable.error(Error.Critical()))
        presenter.validateItems()

        val inOrder = inOrder(view, cartValidationUseCase)
        inOrder.verify(cartValidationUseCase).execute(any(), any(), any())
        inOrder.verify(view).validationError()
    }
}