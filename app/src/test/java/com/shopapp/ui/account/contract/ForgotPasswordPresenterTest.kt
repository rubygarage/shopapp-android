package com.shopapp.ui.account.contract

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.domain.interactor.account.ForgotPasswordUseCase
import com.shopapp.domain.validator.FieldValidator
import com.shopapp.gateway.entity.Error
import com.shopapp.test.MockInstantiator
import com.shopapp.test.RxImmediateSchedulerRule
import com.shopapp.test.ext.mock
import io.reactivex.Completable
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ForgotPasswordPresenterTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var view: ForgotPasswordView

    @Mock
    private lateinit var useCase: ForgotPasswordUseCase

    @Mock
    private lateinit var validator: FieldValidator

    private lateinit var presenter: ForgotPasswordPresenter

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        presenter = ForgotPasswordPresenter(validator, useCase)
        presenter.attachView(view)
        useCase.mock()
        validator.mock()
    }

    @Test
    fun shouldShowInvalidEmailErrorWhenEmailInvalid() {
        given(validator.isEmailValid(any())).willReturn(false)
        presenter.forgotPassword(MockInstantiator.DEFAULT_EMAIL)

        verify(view).showEmailValidError()
        verify(useCase, never()).execute(any(), any(), any())
    }

    @Test
    fun shouldShowContentWhenReturnComplete() {
        given(useCase.buildUseCaseCompletable(any())).willReturn(Completable.complete())
        given(validator.isEmailValid(any())).willReturn(true)
        presenter.forgotPassword(MockInstantiator.DEFAULT_EMAIL)

        verify(view).showContent(any())
        verify(view, never()).showError(any())
        verify(view, never()).showMessage(any<Int>())
        verify(view, never()).showMessage(any<String>())
    }

    @Test
    fun shouldShowMessageWhenReturnNonCriticalError() {
        val errorMessage = "errorMessage"
        given(useCase.buildUseCaseCompletable(any())).willReturn(Completable.error(Error.NonCritical(errorMessage)))
        given(validator.isEmailValid(any())).willReturn(true)
        presenter.forgotPassword(MockInstantiator.DEFAULT_EMAIL)

        verify(view).showMessage(errorMessage)
        verify(view, never()).showContent(any())
        verify(view, never()).showError(any())
    }

    @Test
    fun shouldShowErrorWhenReturnContentError() {
        val isNetworkError = true
        given(useCase.buildUseCaseCompletable(any())).willReturn(Completable.error(Error.Content(isNetworkError)))
        given(validator.isEmailValid(any())).willReturn(true)
        presenter.forgotPassword(MockInstantiator.DEFAULT_EMAIL)

        argumentCaptor<Error>().apply {
            verify(view).showError(capture())
            verify(view, never()).showContent(any())
            verify(view, never()).showMessage(any<Int>())
            verify(view, never()).showMessage(any<String>())

            assertTrue(firstValue is Error.Content)
            assertTrue((firstValue as Error.Content).isNetworkError)
        }
    }
}