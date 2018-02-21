package com.client.shop.ui.account.contract

import com.client.RxImmediateSchedulerRule
import com.client.shop.ext.mock
import com.client.shop.gateway.entity.Error
import com.domain.interactor.account.SignUpUseCase
import com.domain.validator.FieldValidator
import com.nhaarman.mockito_kotlin.*
import com.shopapp.ui.account.contract.SignUpPresenter
import com.shopapp.ui.account.contract.SignUpView
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
class SignUpPresenterTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var view: SignUpView

    @Mock
    private lateinit var useCase: SignUpUseCase

    @Mock
    private lateinit var validator: FieldValidator

    private lateinit var presenter: SignUpPresenter

    private val email = "email@some.com"
    private val password = "123456789"
    private val name = "name"
    private val lastName = "lastName"
    private val phone = "0633291677"

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        presenter = SignUpPresenter(validator, useCase)
        presenter.attachView(view)
        useCase.mock()
        validator.mock()
    }

    @After
    fun tearDown() {
        presenter.detachView(false)
    }

    @Test
    fun shouldExecuteUseCaseOnValidData() {
        given(useCase.buildUseCaseCompletable(any())).willReturn(Completable.complete())
        presenter.signUp(name, lastName, email, password, phone)
        verify(useCase).execute(any(), any(), eq(SignUpUseCase.Params(name, lastName, email, password, phone)))
    }

    @Test
    fun shouldShowErrorOnInvalidPass() {
        given(validator.isPasswordValid(any())).willReturn(false)
        presenter.signUp(name, lastName, email, password, phone)
        verify(view).showPasswordError()
        verify(useCase, never()).execute(any(), any(), any())
    }

    @Test
    fun shouldShowErrorOnInvalidEmail() {
        given(validator.isEmailValid(any())).willReturn(false)
        presenter.signUp(name, lastName, email, password, phone)
        verify(view).showEmailError()
        verify(useCase, never()).execute(any(), any(), any())
    }

    @Test
    fun shouldShowContentOnUseCaseComplete() {
        given(useCase.buildUseCaseCompletable(any())).willReturn(Completable.complete())
        presenter.signUp(name, lastName, email, password, phone)
        verify(view).showContent(Unit)
    }

    @Test
    fun shouldNotifyFailureOnUseCaseError() {
        given(useCase.buildUseCaseCompletable(any())).willReturn(Completable.error(Error.NonCritical("ErrorMessage")))
        presenter.signUp(name, lastName, email, password, phone)
        verify(view).onCheckPassed()
    }

    @Test
    fun shouldShowMessageOnUseCaseNonCriticalError() {
        given(useCase.buildUseCaseCompletable(any())).willReturn(Completable.error(Error.NonCritical("ErrorMessage")))
        presenter.signUp(name, lastName, email, password, phone)
        verify(view).showMessage("ErrorMessage")
    }

    @Test
    fun shouldShowErrorOnUseCaseContentError() {
        given(useCase.buildUseCaseCompletable(any())).willReturn(Completable.error(Error.Content(false)))
        presenter.signUp(name, lastName, email, password, phone)
        verify(view).showError(false)
    }

}
