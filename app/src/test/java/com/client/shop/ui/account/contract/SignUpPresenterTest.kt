package com.client.shop.ui.account.contract

import com.client.RxImmediateSchedulerRule
import com.client.shop.ext.mockUseCase
import com.client.shop.getaway.entity.Error
import com.domain.interactor.account.SignUpUseCase
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
class SignUpPresenterTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var view: SignUpView

    @Mock
    private lateinit var useCase: SignUpUseCase

    private lateinit var presenter: SignUpPresenter

    private val correctEmail = "email@some.com"
    private val incorrectEmail = "email"
    private val correctPass = "123456789"
    private val incorrectPass = "123"

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        presenter = SignUpPresenter(FieldValidator(), useCase)
        presenter.attachView(view)
        useCase.mockUseCase()
    }

    @After
    fun tearDown() {
        presenter.detachView(false)
    }

    @Test
    fun shouldExecuteUseCaseOnValidData() {
        given(useCase.buildUseCaseCompletable(any())).willReturn(Completable.complete())
        presenter.signUp("", "", correctEmail, correctPass, "")
        verify(useCase).execute(any(), any(), eq(SignUpUseCase.Params("", "", correctEmail, correctPass, "")))
    }

    @Test
    fun shouldShowErrorOnInvalidPass() {
        presenter.signUp("", "", "", incorrectPass, "")
        verify(view).showPasswordError()
        verify(useCase, never()).execute(any(), any(), any())
    }

    @Test
    fun shouldShowErrorOnInvalidEmail() {
        presenter.signUp("", "", incorrectEmail, "", "")
        verify(view).showEmailError()
        verify(useCase, never()).execute(any(), any(), any())
    }

    @Test
    fun shouldShowContentOnUseCaseComplete() {
        given(useCase.buildUseCaseCompletable(any())).willReturn(Completable.complete())
        presenter.signUp("", "", correctEmail, correctPass, "")
        verify(view).showContent(Unit)
    }

    @Test
    fun shouldNotifyFailureOnUseCaseError() {
        given(useCase.buildUseCaseCompletable(any())).willReturn(Completable.error(Error.NonCritical("ErrorMessage")))
        presenter.signUp("", "", correctEmail, correctPass, "")
        verify(view).onCheckPassed()
    }

    @Test
    fun shouldShowMessageOnUseCaseNonCriticalError() {
        given(useCase.buildUseCaseCompletable(any())).willReturn(Completable.error(Error.NonCritical("ErrorMessage")))
        presenter.signUp("", "", correctEmail, correctPass, "")
        verify(view).showMessage("ErrorMessage")
    }

    @Test
    fun shouldShowErrorOnUseCaseContentError() {
        given(useCase.buildUseCaseCompletable(any())).willReturn(Completable.error(Error.Content(false)))
        presenter.signUp("", "", correctEmail, correctPass, "")
        verify(view).showError(false)
    }

}
