package com.client.shop.ui.account.contract

import com.client.RxImmediateSchedulerRule
import com.client.shop.ext.mock
import com.client.shop.gateway.entity.Error
import com.domain.interactor.account.ChangePasswordUseCase
import com.domain.validator.FieldValidator
import com.nhaarman.mockito_kotlin.*
import com.shopapp.ui.account.contract.ChangePasswordPresenter
import com.shopapp.ui.account.contract.ChangePasswordView
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

    @After
    fun tearDown() {
        presenter.detachView(false)
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
        given(useCase.buildUseCaseCompletable(any())).willReturn(Completable.error(Error.Content(false)))
        presenter.changePassword("123456789", "123456789")
        verify(view).showError(false)
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

}
