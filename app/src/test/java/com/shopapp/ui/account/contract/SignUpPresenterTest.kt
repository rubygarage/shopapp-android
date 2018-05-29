package com.shopapp.ui.account.contract

import com.nhaarman.mockito_kotlin.*
import com.shopapp.domain.interactor.account.SignUpUseCase
import com.shopapp.domain.interactor.shop.ConfigUseCase
import com.shopapp.domain.validator.FieldValidator
import com.shopapp.gateway.entity.Config
import com.shopapp.gateway.entity.Error
import com.shopapp.test.RxImmediateSchedulerRule
import com.shopapp.test.ext.mock
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class SignUpPresenterTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var view: SignUpView

    @Mock
    private lateinit var configUseCase: ConfigUseCase

    @Mock
    private lateinit var signUpUseCase: SignUpUseCase

    @Mock
    private lateinit var validator: FieldValidator

    @Mock
    private lateinit var config: Config

    private lateinit var presenter: SignUpPresenter

    private val email = "email@some.com"
    private val password = "123456789"
    private val name = "name"
    private val lastName = "lastName"
    private val phone = "0633291677"

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        presenter = SignUpPresenter(configUseCase, validator, signUpUseCase)
        presenter.attachView(view)
        configUseCase.mock()
        signUpUseCase.mock()
        validator.mock()
    }

    @After
    fun tearDown() {
        presenter.detachView(false)
    }

    @Test
    fun shouldExecuteUseCaseOnValidData() {
        given(signUpUseCase.buildUseCaseCompletable(any())).willReturn(Completable.complete())
        presenter.signUp(name, lastName, email, password, phone)
        verify(signUpUseCase).execute(any(), any(), eq(SignUpUseCase.Params(name, lastName, email, password, phone)))
    }

    @Test
    fun shouldShowErrorOnInvalidPass() {
        given(validator.isPasswordValid(any())).willReturn(false)
        presenter.signUp(name, lastName, email, password, phone)
        verify(view).showPasswordError()
        verify(signUpUseCase, never()).execute(any(), any(), any())
    }

    @Test
    fun shouldShowErrorOnInvalidEmail() {
        given(validator.isEmailValid(any())).willReturn(false)
        presenter.signUp(name, lastName, email, password, phone)
        verify(view).showEmailError()
        verify(signUpUseCase, never()).execute(any(), any(), any())
    }

    @Test
    fun shouldShowContentWhenConfigReceived() {
        given(configUseCase.buildUseCaseSingle(any())).willReturn(Single.just(config))
        presenter.getConfig()
        verify(view).showContent(config)
    }

    @Test
    fun shouldShowErrorWhenConfigReceived() {
        val error = Error.Content()
        given(configUseCase.buildUseCaseSingle(any())).willReturn(Single.error(error))
        presenter.getConfig()
        verify(view).showError(error)
    }

    @Test
    fun shouldCallOnSignUpFinishedWhenOnUseCaseComplete() {
        given(signUpUseCase.buildUseCaseCompletable(any())).willReturn(Completable.complete())
        presenter.signUp(name, lastName, email, password, phone)
        verify(view).onSignUpFinished()
    }

    @Test
    fun shouldNotifyFailureOnUseCaseError() {
        given(signUpUseCase.buildUseCaseCompletable(any())).willReturn(Completable.error(Error.NonCritical("ErrorMessage")))
        presenter.signUp(name, lastName, email, password, phone)
        verify(view).onCheckPassed()
    }

    @Test
    fun shouldShowMessageOnUseCaseNonCriticalError() {
        given(signUpUseCase.buildUseCaseCompletable(any())).willReturn(Completable.error(Error.NonCritical("ErrorMessage")))
        presenter.signUp(name, lastName, email, password, phone)
        verify(view).showMessage("ErrorMessage")
    }

    @Test
    fun shouldShowErrorOnUseCaseContentError() {
        given(signUpUseCase.buildUseCaseCompletable(any())).willReturn(Completable.error(Error.Content()))
        presenter.signUp(name, lastName, email, password, phone)

        argumentCaptor<Error>().apply {
            verify(view).showError(capture())

            assertTrue(firstValue is Error.Content)
        }
    }
}
