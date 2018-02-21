package com.client.shop.ui.account.contract

import com.client.RxImmediateSchedulerRule
import com.client.shop.ext.mock
import com.client.shop.gateway.entity.Error
import com.domain.interactor.account.SignInUseCase
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

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class SignInPresenterTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var view: SignInView

    @Mock
    private lateinit var useCase: SignInUseCase

    @Mock
    private lateinit var validator: FieldValidator

    private lateinit var presenter: SignInPresenter

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        presenter = SignInPresenter(validator, useCase)
        presenter.attachView(view)
        useCase.mock()
        validator.mock()
    }

    @After
    fun tearDown() {
        presenter.detachView(false)
    }

    @Test
    fun shouldShowContentOnUseCaseComplete() {
        given(useCase.buildUseCaseCompletable(any())).willReturn(Completable.complete())
        presenter.logIn("email@test.com", "123456789")

        val inOrder = inOrder(view, useCase)
        inOrder.verify(useCase).execute(any(), any(), eq(SignInUseCase.Params("email@test.com", "123456789")))
        inOrder.verify(view).showContent(any())
    }

    @Test
    fun shouldNotifyAboutCheckPass() {
        given(useCase.buildUseCaseCompletable(any())).willReturn(Completable.complete())
        presenter.logIn("email@test.com", "123456789")
        verify(view).onCheckPassed()
    }

    @Test
    fun shouldNotifyViewOnUseCaseError() {
        given(useCase.buildUseCaseCompletable(any())).willReturn(Completable.error(Error.NonCritical("ErrorMessage")))
        presenter.logIn("email@test.com", "123456789")

        val inOrder = inOrder(view, useCase)
        inOrder.verify(useCase).execute(any(), any(), eq(SignInUseCase.Params("email@test.com", "123456789")))
        inOrder.verify(view).onFailure()
    }

    @Test
    fun shouldShowMessageOnUseCaseNonCriticalError() {
        given(useCase.buildUseCaseCompletable(any())).willReturn(Completable.error(Error.NonCritical("ErrorMessage")))
        presenter.logIn("email@test.com", "123456789")

        val inOrder = inOrder(view, useCase)
        inOrder.verify(useCase).execute(any(), any(), eq(SignInUseCase.Params("email@test.com", "123456789")))
        inOrder.verify(view).showMessage("ErrorMessage")
    }

    @Test
    fun shouldShowErrorOnUseCaseContentError() {
        given(useCase.buildUseCaseCompletable(any())).willReturn(Completable.error(Error.Content(false)))
        presenter.logIn("email@test.com", "123456789")

        val inOrder = inOrder(view, useCase)
        inOrder.verify(useCase).execute(any(), any(), eq(SignInUseCase.Params("email@test.com", "123456789")))
        inOrder.verify(view).showError(false)
    }

    @Test
    fun shouldShowErrorOnInvalidPass() {
        given(validator.isPasswordValid(any())).willReturn(false)
        presenter.logIn("email@test.com", "0")
        verify(view).showPasswordError()
        verify(useCase, never()).execute(any(), any(), any())
    }

    @Test
    fun shouldShowEmailErrorOnInvalidEmail() {
        given(validator.isEmailValid(any())).willReturn(false)
        presenter.logIn("12345678", "123456789")
        verify(view).showEmailError()
        verify(useCase, never()).execute(any(), any(), any())
    }

}
