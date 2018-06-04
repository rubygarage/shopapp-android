package com.shopapp.ui.account.contract

import com.nhaarman.mockito_kotlin.*
import com.shopapp.domain.interactor.account.GetCustomerUseCase
import com.shopapp.domain.interactor.account.UpdateCustomerUseCase
import com.shopapp.domain.interactor.shop.ConfigUseCase
import com.shopapp.gateway.entity.Config
import com.shopapp.gateway.entity.Customer
import com.shopapp.gateway.entity.Error
import com.shopapp.test.RxImmediateSchedulerRule
import com.shopapp.test.ext.mock
import io.reactivex.Single
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class PersonalInfoPresenterTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var view: PersonalInfoView

    @Mock
    private lateinit var config: Config

    private var configUseCase: ConfigUseCase = mock()

    private var getCustomerUseCase: GetCustomerUseCase = mock()

    private var updateCustomerUseCase: UpdateCustomerUseCase = mock()

    private lateinit var presenter: PersonalInfoPresenter

    private val customer: Customer = mock()

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        configUseCase.mock()
        updateCustomerUseCase.mock()
        getCustomerUseCase.mock()
        presenter = PersonalInfoPresenter(configUseCase, getCustomerUseCase, updateCustomerUseCase)
        presenter.attachView(view)
    }

    @After
    fun tearDown() {
        presenter.detachView(false)
    }

    @Test
    fun shouldExecuteUseCaseOnGetConfig() {
        given(configUseCase.buildUseCaseSingle(any())).willReturn(Single.just(config))
        presenter.getConfig()
        verify(configUseCase).execute(any(), any(), any())
    }

    @Test
    fun shouldCallOnConfigReceived() {
        given(configUseCase.buildUseCaseSingle(any())).willReturn(Single.just(config))
        presenter.getConfig()
        val inOrder = inOrder(configUseCase, view)
        inOrder.verify(configUseCase).execute(any(), any(), any())
        inOrder.verify(view).onConfigReceived(config)
    }

    @Test
    fun getConfigShouldShowMessageWhenReceivedNonCriticalError() {
        given(configUseCase.buildUseCaseSingle(any())).willReturn(Single.error(Error.NonCritical("ErrorMessage")))
        presenter.getConfig()

        val inOrder = inOrder(view, configUseCase)
        inOrder.verify(configUseCase).execute(any(), any(), any())
        inOrder.verify(view).showMessage(eq("ErrorMessage"))
    }

    @Test
    fun getConfigShouldShowErrorWhenReceivedContentError() {
        given(configUseCase.buildUseCaseSingle(any())).willReturn(Single.error(Error.Content()))
        presenter.getConfig()

        argumentCaptor<Error>().apply {
            val inOrder = inOrder(view, configUseCase)
            inOrder.verify(configUseCase).execute(any(), any(), any())
            inOrder.verify(view).showError(capture())

            assertTrue(firstValue is Error.Content)
        }
    }

    @Test
    fun shouldExecuteUseCaseOnGetCustomer() {
        given(getCustomerUseCase.buildUseCaseSingle(any())).willReturn(Single.just(customer))
        presenter.getCustomer()
        verify(getCustomerUseCase).execute(any(), any(), any())
    }

    @Test
    fun shouldShowContentIfCustomerExist() {
        given(getCustomerUseCase.buildUseCaseSingle(any())).willReturn(Single.just(customer))
        presenter.getCustomer()
        val inOrder = inOrder(getCustomerUseCase, view)
        inOrder.verify(getCustomerUseCase).execute(any(), any(), any())
        inOrder.verify(view).showContent(customer)
    }

    @Test
    fun shouldSetCustomerEmailIfCustomerExist() {
        given(customer.email).willReturn("testEmail@test.com")
        given(getCustomerUseCase.buildUseCaseSingle(any())).willReturn(Single.just(customer))
        presenter.getCustomer()
        val inOrder = inOrder(getCustomerUseCase, view)
        inOrder.verify(getCustomerUseCase).execute(any(), any(), any())
        inOrder.verify(view).setupCustomerEmail("testEmail@test.com")
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
    fun shouldExecuteUseCaseOnEditCustomer() {
        val name = "name"
        val lastName = "lastName"
        val phone = "06333291677"
        given(updateCustomerUseCase.buildUseCaseSingle(any())).willReturn(Single.just(customer))
        presenter.editCustomer(name, lastName, phone)
        verify(updateCustomerUseCase)
                .execute(any(), any(), eq(UpdateCustomerUseCase.Params(name, lastName, phone)))
    }

    @Test
    fun shouldUpdateCustomerOnEditCustomer() {
        given(updateCustomerUseCase.buildUseCaseSingle(any())).willReturn(Single.just(customer))
        presenter.editCustomer("name", "lastName", "06333291677")
        verify(view).onCustomerChanged(customer)
    }

    @Test
    fun shouldShowMessageOnEditCustomerNonCriticalError() {

        val name = "name"
        val lastName = "lastName"
        val phone = "06333291677"

        given(updateCustomerUseCase.buildUseCaseSingle(any()))
                .willReturn(Single.error(Error.NonCritical("ErrorMessage")))
        presenter.editCustomer(name, lastName, phone)

        val inOrder = inOrder(view, updateCustomerUseCase)
        inOrder.verify(updateCustomerUseCase)
                .execute(any(), any(), eq(UpdateCustomerUseCase.Params(name, lastName, phone)))
        inOrder.verify(view).showMessage(eq("ErrorMessage"))
    }

    @Test
    fun shouldShowErrorOnEditCustomerContentError() {
        val name = "name"
        val lastName = "lastName"
        val phone = "06333291677"

        given(updateCustomerUseCase.buildUseCaseSingle(any()))
                .willReturn(Single.error(Error.Content()))
        presenter.editCustomer(name, lastName, phone)

        argumentCaptor<Error>().apply {
            val inOrder = inOrder(view, updateCustomerUseCase)
            inOrder.verify(updateCustomerUseCase)
                    .execute(any(), any(), eq(UpdateCustomerUseCase.Params(name, lastName, phone)))
            inOrder.verify(view).showError(capture())

            assertTrue(firstValue is Error.Content)
        }
    }

    @Test
    fun shouldExplicitHideProgressOnEditCustomerError() {
        given(updateCustomerUseCase.buildUseCaseSingle(any()))
                .willReturn(Single.error(Error.Content()))
        presenter.editCustomer("name", "lastName", "06333291677")
        verify(view).hideProgress()
    }
}
