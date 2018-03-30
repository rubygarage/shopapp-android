package com.shopapp.ui.account.contract

import com.nhaarman.mockito_kotlin.*
import com.shopapp.domain.interactor.account.GetCustomerUseCase
import com.shopapp.domain.interactor.account.UpdateAccountSettingsUseCase
import com.shopapp.gateway.entity.Customer
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

class AccountSettingsPresenterTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var view: AccountSettingsView

    @Mock
    private lateinit var getCustomerUseCase: GetCustomerUseCase

    @Mock
    private lateinit var accountSettingsUseCase: UpdateAccountSettingsUseCase

    private lateinit var presenter: AccountSettingsPresenter

    private val customer: Customer = mock()

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        presenter = AccountSettingsPresenter(getCustomerUseCase, accountSettingsUseCase)
        presenter.attachView(view)
        getCustomerUseCase.mock()
        accountSettingsUseCase.mock()
    }

    @Test
    fun shouldExecuteUseCaseOnGetCustomer() {
        given(getCustomerUseCase.buildUseCaseSingle(any())).willReturn(Single.just(customer))
        presenter.getCustomer()
        verify(getCustomerUseCase).execute(any(), any(), any())
    }

    @Test
    fun shouldShowContentOnCustomerReceived() {
        given(customer.isAcceptsMarketing).willReturn(true)
        given(getCustomerUseCase.buildUseCaseSingle(any())).willReturn(Single.just(customer))
        presenter.getCustomer()
        val inOrder = inOrder(getCustomerUseCase, view)
        inOrder.verify(getCustomerUseCase).execute(any(), any(), any())
        inOrder.verify(view).showContent(true)
    }

    @Test
    fun shouldShowMessageOnGetCustomerNonCriticalError() {
        given(getCustomerUseCase.buildUseCaseSingle(any())).willReturn(Single.error(Error.NonCritical("ErrorMessage")))
        presenter.getCustomer()

        val inOrder = inOrder(view, getCustomerUseCase)
        inOrder.verify(getCustomerUseCase).execute(any(), any(), any())
        inOrder.verify(view).showMessage(eq("ErrorMessage"))
    }

    @Test
    fun shouldShowErrorOnGetCustomerContentError() {
        given(getCustomerUseCase.buildUseCaseSingle(any())).willReturn(Single.error(Error.Content()))
        presenter.getCustomer()

        argumentCaptor<Error>().apply {
            val inOrder = inOrder(view, getCustomerUseCase)
            inOrder.verify(getCustomerUseCase).execute(any(), any(), any())
            inOrder.verify(view).showError(capture())

            assertTrue(firstValue is Error.Content)
        }
    }


    @Test
    fun shouldExecuteUseCaseOnUpdateSettings() {
        given(accountSettingsUseCase.buildUseCaseCompletable(any())).willReturn(Completable.complete())
        presenter.updateSettings(true)
        verify(accountSettingsUseCase).execute(any(), any(), eq(true))
    }

    @Test
    fun shouldShowMessageOnUpdateSettingsNonCriticalError() {
        given(accountSettingsUseCase.buildUseCaseCompletable(any())).willReturn(Completable.error(Error.NonCritical("ErrorMessage")))
        presenter.updateSettings(true)

        val inOrder = inOrder(view, getCustomerUseCase)
        verify(accountSettingsUseCase).execute(any(), any(), eq(true))
        inOrder.verify(view).showMessage(eq("ErrorMessage"))
    }

    @Test
    fun shouldShowErrorOnUpdateSettingsContentError() {
        given(accountSettingsUseCase.buildUseCaseCompletable(any())).willReturn(Completable.error(Error.Content()))
        presenter.updateSettings(true)

        argumentCaptor<Error>().apply {
            val inOrder = inOrder(view, getCustomerUseCase)
            verify(accountSettingsUseCase).execute(any(), any(), eq(true))
            inOrder.verify(view).showError(capture())

            assertTrue(firstValue is Error.Content)
        }
    }

    @After
    fun tearDown() {
        presenter.detachView(false)
    }
}
