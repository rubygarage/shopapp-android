package com.shopapp.ui.address.checkout.contract

import com.nhaarman.mockito_kotlin.*
import com.shopapp.domain.interactor.checkout.SetShippingAddressUseCase
import com.shopapp.gateway.entity.Address
import com.shopapp.gateway.entity.Checkout
import com.shopapp.gateway.entity.Error
import com.shopapp.test.RxImmediateSchedulerRule
import com.shopapp.test.ext.mock
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class CheckoutAddressListPresenterTest {

    companion object {
        private const val CHECKOUT_ID = "checkout_id"
    }

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var view: CheckoutAddressListView

    @Mock
    private lateinit var setShippingAddressUseCase: SetShippingAddressUseCase

    @Mock
    private lateinit var checkout: Checkout

    @Mock
    private lateinit var address: Address

    private lateinit var presenter: CheckoutAddressListPresenter

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        presenter = CheckoutAddressListPresenter(mock(), mock(), mock(), setShippingAddressUseCase)
        presenter.attachView(view)

        setShippingAddressUseCase.mock()
    }

    @Test
    fun setShippingAddressShouldCallAddressChangedWhenOnSingleSuccess() {
        given(setShippingAddressUseCase.buildUseCaseSingle(any())).willReturn(Single.just(checkout))
        presenter.setShippingAddress(CHECKOUT_ID, address)

        argumentCaptor<SetShippingAddressUseCase.Params>().apply {
            val inOrder = inOrder(view, setShippingAddressUseCase)
            inOrder.verify(setShippingAddressUseCase).execute(any(), any(), capture())
            inOrder.verify(view).selectedAddressChanged(address)

            assertEquals(CHECKOUT_ID, firstValue.checkoutId)
            assertEquals(address, firstValue.address)
        }
    }

    @Test
    fun setShippingAddressShouldCallAddressChangedWhenOnSingleContentError() {
        val error = Error.Content()
        given(setShippingAddressUseCase.buildUseCaseSingle(any())).willReturn(Single.error(error))
        presenter.setShippingAddress(CHECKOUT_ID, address)

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
    fun setShippingAddressShouldCallAddressChangedWhenOnSingleNonCriticalError() {
        val message = "message"
        val error = Error.NonCritical(message)
        given(setShippingAddressUseCase.buildUseCaseSingle(any())).willReturn(Single.error(error))
        presenter.setShippingAddress(CHECKOUT_ID, address)

        argumentCaptor<SetShippingAddressUseCase.Params>().apply {
            val inOrder = inOrder(view, setShippingAddressUseCase)
            inOrder.verify(setShippingAddressUseCase).execute(any(), any(), capture())
            inOrder.verify(view).showMessage(message)

            assertEquals(CHECKOUT_ID, firstValue.checkoutId)
            assertEquals(address, firstValue.address)
        }
    }
}