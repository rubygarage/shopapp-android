package com.shopapp.ui.account.contract

import com.nhaarman.mockito_kotlin.*
import com.shopapp.domain.interactor.account.ChangePasswordUseCase
import com.shopapp.domain.validator.FieldValidator
import com.shopapp.gateway.entity.Error
import com.shopapp.test.RxImmediateSchedulerRule
import com.shopapp.test.ext.mock
import io.reactivex.Completable
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ChangePasswordPresenterTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var view: ChangePasswordView

    @Mock
    private lateinit var useCase: ChangePasswordUseCase

    @Mock
    private lateinit var validator: FieldValidator

    private lateinit var presenter: ChangePasswordPresenter

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        presenter = ChangePasswordPresenter(validator, useCase)
        presenter.attachView(view)
        useCase.mock()
        validator.mock()
    }

    @Test
    fun shouldExecuteUseCaseOnValidData() {
        given(useCase.buildUseCaseCompletable(any())).willReturn(Completable.complete())
        presenter.changePassword("123456789", "123456789")
        verify(useCase).execute(any(), any(), eq("123456789"))
    }

    @Test
    fun shouldShowProgressBeforeUseCaseExecuting() {
        given(useCase.buildUseCaseCompletable(any())).willReturn(Completable.complete())
        presenter.changePassword("123456789", "123456789")
        val inOrder = inOrder(view, useCase)
        inOrder.verify(view).showUpdateProgress()
        inOrder.verify(useCase).execute(any(), any(), eq("123456789"))
    }

    @Test
    fun shouldHideProgressOnUseCaseComplete() {
        given(useCase.buildUseCaseCompletable(any())).willReturn(Completable.complete())
        presenter.changePassword("123456789", "123456789")
        verify(view).hideUpdateProgress()
    }

    @Test
    fun shouldNotifyAboutPasswordChangeOnUseCaseComplete() {
        given(useCase.buildUseCaseCompletable(any())).willReturn(Completable.complete())
        presenter.changePassword("123456789", "123456789")
        verify(view).passwordChanged()
    }

    @Test
    fun shouldHideProgressOnUseCaseError() {
        given(useCase.buildUseCaseCompletable(any())).willReturn(Completable.error(Error.NonCritical("ErrorMessage")))
        presenter.changePassword("123456789", "123456789")
        verify(view).hideUpdateProgress()
    }

    @Test
    fun shouldShowMessageOnUseCaseNonCriticalError() {
        given(useCase.buildUseCaseCompletable(any())).willReturn(Completable.error(Error.NonCritical("ErrorMessage")))
        presenter.changePassword("123456789", "123456789")
        verify(view).showMessage("ErrorMessage")
    }

    @Test
    fun shouldShowErrorOnUseCaseContentError() {
        argumentCaptor<Error>().apply {
            given(useCase.buildUseCaseCompletable(any())).willReturn(Completable.error(Error.Content()))
            presenter.changePassword("123456789", "123456789")
            verify(view).showError(capture())

            assertTrue(firstValue is Error.Content)
        }
    }

    @Test
    fun shouldShowErrorOnInvalidPass() {
        given(validator.isPasswordValid(any())).willReturn(false)
        presenter.changePassword("pass", "pass")
        verify(view).passwordValidError()
        verify(useCase, never()).execute(any(), any(), eq("pass"))
    }

    @Test
    fun shouldShowPasswordMatchErrorOnDifferentPass() {
        presenter.changePassword("12345678", "123456789")
        verify(view).passwordsMatchError()
        verify(useCase, never()).execute(any(), any(), any())
    }

    @After
    fun tearDown() {
        presenter.detachView(false)
    }
}
