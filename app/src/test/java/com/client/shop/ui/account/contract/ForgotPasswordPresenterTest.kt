package com.client.shop.ui.account.contract

import com.client.RxImmediateSchedulerRule
import com.client.shop.ext.mockUseCase
import com.client.shop.gateway.entity.Error
import com.domain.interactor.account.ForgotPasswordUseCase
import com.domain.validator.FieldValidator
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Completable
import org.junit.After
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
class ForgotPasswordPresenterTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var view: ForgotPasswordView

    @Mock
    private lateinit var useCase: ForgotPasswordUseCase

    private lateinit var presenter: ForgotPasswordPresenter

    private val EMAIL = "testEmail@gmail.com"

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        presenter = ForgotPasswordPresenter(FieldValidator(), useCase)
        presenter.attachView(view)
        useCase.mockUseCase()
    }

    @After
    fun tearDown() {
        presenter.detachView(false)
    }

    @Test
    fun shouldDelegateCallToUseCase() {
        given(useCase.buildUseCaseCompletable(any())).willReturn(Completable.complete())
        presenter.resetPassword(EMAIL)
        verify(useCase).execute(any(), any(), eq("123456789"))
    }

    @Test
    fun shouldNotifyViewOnEmailError() {
        presenter.resetPassword("wrongEmail")
        verify(view).showEmailValidError()
    }

    @Test
    fun shouldShowOnUseCaseComplete() {
        given(useCase.buildUseCaseCompletable(any())).willReturn(Completable.complete())
        presenter.resetPassword(EMAIL)
        verify(view).showContent(any())
    }

    @Test
    fun shouldShowMessageOnUseCaseNonCriticalError() {
        given(useCase.buildUseCaseCompletable(any())).willReturn(Completable.error(Error.NonCritical("ErrorMessage")))
        presenter.resetPassword(EMAIL)

        val inOrder = inOrder(view, useCase)
        inOrder.verify(useCase).execute(any(), any(), eq("123456789"))
        inOrder.verify(view).showMessage("ErrorMessage")
    }

    @Test
    fun shouldShowErrorOnUseCaseContentError() {
        given(useCase.buildUseCaseCompletable(any())).willReturn(Completable.error(Error.Content(false)))
        presenter.resetPassword(EMAIL)

        val inOrder = inOrder(view, useCase)
        inOrder.verify(useCase).execute(any(), any(), eq("123456789"))
        inOrder.verify(view).showError(false)
    }

}
