package com.shopapp.ui.address.checkout.contract

import com.nhaarman.mockito_kotlin.*
import com.shopapp.R
import com.shopapp.domain.interactor.checkout.SetShippingAddressUseCase
import com.shopapp.domain.validator.FieldValidator
import com.shopapp.gateway.entity.Address
import com.shopapp.gateway.entity.Checkout
import com.shopapp.gateway.entity.Error
import com.shopapp.test.RxImmediateSchedulerRule
import com.shopapp.test.ext.mock
import com.shopapp.ui.address.base.contract.AddressView
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class CheckoutUnAuthAddressPresenterTest {

    companion object {
        private const val CHECKOUT_ID = "checkout_id"
    }

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var view: AddressView

    @Mock
    private lateinit var fieldValidator: FieldValidator

    @Mock
    private lateinit var setShippingAddressUseCase: SetShippingAddressUseCase

    @Mock
    private lateinit var checkout: Checkout

    @Mock
    private lateinit var address: Address

    private lateinit var presenter: CheckoutUnAuthAddressPresenter

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        presenter = CheckoutUnAuthAddressPresenter(fieldValidator, mock(), setShippingAddressUseCase,
            mock(), mock())
        presenter.attachView(view)

        setShippingAddressUseCase.mock()
    }

    @Test
    fun submitShippingAddressShouldShowErrorWhenSubmitInvalidAddress() {
        given(fieldValidator.isAddressValid(any())).willReturn(false)
        presenter.submitShippingAddress(CHECKOUT_ID, address)

        val inOrder = inOrder(view)
        inOrder.verify(view).submitAddressError()
        inOrder.verify(view).showMessage(R.string.invalid_address)
    }

    @Test
    fun submitShippingAddressShouldCallAddressChangedWhenOnSingleSuccess() {
        given(setShippingAddressUseCase.buildUseCaseSingle(any())).willReturn(Single.just(checkout))
        given(fieldValidator.isAddressValid(any())).willReturn(true)
        presenter.submitShippingAddress(CHECKOUT_ID, address)

        argumentCaptor<SetShippingAddressUseCase.Params>().apply {
            val inOrder = inOrder(view, setShippingAddressUseCase)
            inOrder.verify(setShippingAddressUseCase).execute(any(), any(), capture())
            inOrder.verify(view).addressChanged(address)

            assertEquals(CHECKOUT_ID, firstValue.checkoutId)
            assertEquals(address, firstValue.address)
        }
    }

    @Test
    fun submitAddressShouldCallAddressChangedWhenOnSingleContentError() {
        val error = Error.Content()
        given(setShippingAddressUseCase.buildUseCaseSingle(any())).willReturn(Single.error(error))
        given(fieldValidator.isAddressValid(any())).willReturn(true)
        presenter.submitShippingAddress(CHECKOUT_ID, address)

        val inOrder = inOrder(view, setShippingAddressUseCase)
        argumentCaptor<SetShippingAddressUseCase.Params>().apply {
            inOrder.verify(setShippingAddressUseCase).execute(any(), any(), capture())
            assertEquals(CHECKOUT_ID, firstValue.checkoutId)
            assertEquals(address, firstValue.address)
        }
        argumentCaptor<Error>().apply {
            inOrder.verify(view).showError(capture())
            assertTrue(firstValue is Error.Content)
        }
    }

    @Test
    fun submitAddressShouldCallAddressChangedWhenOnSingleNonCriticalError() {
        val message = "message"
        val error = Error.NonCritical(message)
        given(setShippingAddressUseCase.buildUseCaseSingle(any())).willReturn(Single.error(error))
        given(fieldValidator.isAddressValid(any())).willReturn(true)
        presenter.submitShippingAddress(CHECKOUT_ID, address)

        argumentCaptor<SetShippingAddressUseCase.Params>().apply {
            val inOrder = inOrder(view, setShippingAddressUseCase)
            inOrder.verify(setShippingAddressUseCase).execute(any(), any(), capture())
            inOrder.verify(view).showMessage(message)

            assertEquals(CHECKOUT_ID, firstValue.checkoutId)
            assertEquals(address, firstValue.address)
        }
    }
}