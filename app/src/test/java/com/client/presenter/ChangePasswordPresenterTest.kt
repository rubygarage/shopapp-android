package com.client.presenter

import com.client.RxImmediateSchedulerRule
import com.client.presenter.ext.mockUseCase
import com.client.shop.ui.account.contract.ChangePasswordPresenter
import com.client.shop.ui.account.contract.ChangePasswordView
import com.client.shop.getaway.entity.Error
import com.domain.interactor.account.ChangePasswordUseCase
import com.domain.validator.FieldValidator
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Completable
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
class ChangePasswordPresenterTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var view: ChangePasswordView

    @Mock
    private lateinit var useCase: ChangePasswordUseCase

    private lateinit var presenter: ChangePasswordPresenter

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        presenter = ChangePasswordPresenter(FieldValidator(), useCase)
        presenter.attachView(view)
        useCase.mockUseCase()
    }

    @Test
    fun changePasswordTest_HappyCase() {
        given(useCase.buildUseCaseCompletable(any())).willReturn(Completable.complete())
        presenter.changePassword("123456789", "123456789")
        verify(view).showUpdateProgress()
        verify(useCase).execute(any(), any(), eq("123456789"))
        verify(view).hideUpdateProgress()
        verify(view).passwordChanged()
    }

    @Test
    fun changePasswordTest_SadCase_NonCriticalError() {
        given(useCase.buildUseCaseCompletable(any())).willReturn(Completable.error(Error.NonCritical("ErrorMessage")))
        presenter.changePassword("123456789", "123456789")
        verify(view).showUpdateProgress()
        verify(useCase).execute(any(), any(), eq("123456789"))
        verify(view).showMessage("ErrorMessage")
        verify(view).hideUpdateProgress()
    }

    @Test
    fun changePasswordTest_SadCase_ContentError() {
        given(useCase.buildUseCaseCompletable(any())).willReturn(Completable.error(Error.Content(false)))
        presenter.changePassword("123456789", "123456789")
        verify(view).showUpdateProgress()
        verify(useCase).execute(any(), any(), eq("123456789"))
        verify(view).showError(false)
        verify(view).hideUpdateProgress()
    }

    @Test
    fun changePasswordTest_InvalidPassword_EmptyString() {
        presenter.changePassword("", "")
        verify(view).passwordValidError()
        verify(useCase, never()).execute(any(), any(), eq(""))
    }

    @Test
    fun changePasswordTest_InvalidPassword_PasswordsSame() {
        presenter.changePassword("pass", "pass")
        verify(view).passwordValidError()
        verify(useCase, never()).execute(any(), any(), eq("pass"))
    }

    @Test
    fun changePasswordTest_InvalidPassword_PasswordsDifferent() {
        presenter.changePassword("pass", "pass1")
        verify(view).passwordValidError()
        verify(view).passwordsMatchError()
        verify(useCase, never()).execute(any(), any(), eq("pass"))
        verify(useCase, never()).execute(any(), any(), eq("pass1"))
    }

}
