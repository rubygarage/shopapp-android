package com.shopapp.ui.checkout.contract

import com.nhaarman.mockito_kotlin.*
import com.shopapp.domain.interactor.cart.CartRemoveAllUseCase
import com.shopapp.domain.interactor.checkout.*
import com.shopapp.gateway.entity.*
import com.shopapp.test.MockInstantiator
import com.shopapp.test.RxImmediateSchedulerRule
import com.shopapp.test.ext.mock
import com.shopapp.ui.const.PaymentType
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.inOrder
import org.mockito.MockitoAnnotations

class CheckoutPresenterTest {

    companion object {
        private const val CARD_TOKEN = "card_token"
        private const val CHECKOUT_ID = "checkout_id"
    }

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var view: CheckoutView

    @Mock
    private lateinit var cartRemoveAllUseCase: CartRemoveAllUseCase

    @Mock
    private lateinit var setupCheckoutUseCase: SetupCheckoutUseCase

    @Mock
    private lateinit var getCheckoutUseCase: GetCheckoutUseCase

    @Mock
    private lateinit var getShippingRatesUseCase: GetShippingRatesUseCase

    @Mock
    private lateinit var setShippingRateUseCase: SetShippingRateUseCase

    @Mock
    private lateinit var completeCheckoutByCardUseCase: CompleteCheckoutByCardUseCase

    @Mock
    private lateinit var checkoutData: Triple<List<CartProduct>, Checkout, Customer?>

    private lateinit var presenter: CheckoutPresenter
    private lateinit var checkout: Checkout
    private lateinit var address: Address
    private lateinit var card: Card

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        presenter = CheckoutPresenter(setupCheckoutUseCase, cartRemoveAllUseCase, getCheckoutUseCase,
            getShippingRatesUseCase, setShippingRateUseCase, completeCheckoutByCardUseCase)
        presenter.attachView(view)

        setupCheckoutUseCase.mock()
        cartRemoveAllUseCase.mock()
        getCheckoutUseCase.mock()
        getShippingRatesUseCase.mock()
        setShippingRateUseCase.mock()
        completeCheckoutByCardUseCase.mock()

        checkout = MockInstantiator.newCheckout()
        address = MockInstantiator.newAddress()
        card = MockInstantiator.newCard()
    }

    @Test
    fun getCheckoutDataShouldCallShowContentWhenOnSuccess() {
        given(setupCheckoutUseCase.buildUseCaseSingle(any())).willReturn(Single.just(checkoutData))
        presenter.getCheckoutData()

        val inOrder = inOrder(view, setupCheckoutUseCase)
        inOrder.verify(setupCheckoutUseCase).execute(any(), any(), any())
        inOrder.verify(view).cartProductListReceived(checkoutData.first)
        inOrder.verify(view).showContent(checkoutData.second)
        inOrder.verify(view).customerReceived(checkoutData.third)
    }

    @Test
    fun getCheckoutDataShouldShowMessageAndShowErrorScreenOnUseCaseNonCriticalError() {
        given(setupCheckoutUseCase.buildUseCaseSingle(any())).willReturn(Single.error(Error.NonCritical("ErrorMessage")))
        presenter.getCheckoutData()

        val inOrder = inOrder(view, setupCheckoutUseCase)
        inOrder.verify(setupCheckoutUseCase).execute(any(), any(), any())
        inOrder.verify(view).showMessage("ErrorMessage")
        argumentCaptor<Error>().apply {
            inOrder.verify(view).showError(capture())
            assertTrue(firstValue is Error.Content)
        }
    }

    @Test
    fun getCheckoutDataShouldShowErrorOnUseCaseContentError() {
        given(setupCheckoutUseCase.buildUseCaseSingle(any())).willReturn(Single.error(Error.Content()))
        presenter.getCheckoutData()

        val inOrder = inOrder(view, setupCheckoutUseCase)
        inOrder.verify(setupCheckoutUseCase).execute(any(), any(), any())
        argumentCaptor<Error>().apply {
            inOrder.verify(view).showError(capture())
            assertTrue(firstValue is Error.Content)
        }
    }

    @Test
    fun getCheckoutShouldCallShowContentWhenOnSuccess() {
        given(getCheckoutUseCase.buildUseCaseSingle(any())).willReturn(Single.just(checkout))
        presenter.getCheckout(CHECKOUT_ID)

        val inOrder = inOrder(view, getCheckoutUseCase)
        inOrder.verify(getCheckoutUseCase).execute(any(), any(), eq(CHECKOUT_ID))
        inOrder.verify(view).showContent(checkout)
    }

    @Test
    fun getCheckoutShouldShowMessageOnUseCaseNonCriticalError() {
        given(getCheckoutUseCase.buildUseCaseSingle(any())).willReturn(Single.error(Error.NonCritical("ErrorMessage")))
        presenter.getCheckout(CHECKOUT_ID)

        val inOrder = inOrder(view, getCheckoutUseCase)
        inOrder.verify(getCheckoutUseCase).execute(any(), any(), eq(CHECKOUT_ID))
        inOrder.verify(view).showMessage("ErrorMessage")
    }

    @Test
    fun getCheckoutShouldShowErrorOnUseCaseContentError() {
        given(getCheckoutUseCase.buildUseCaseSingle(any())).willReturn(Single.error(Error.Content()))
        presenter.getCheckout(CHECKOUT_ID)

        val inOrder = inOrder(view, getCheckoutUseCase)
        inOrder.verify(getCheckoutUseCase).execute(any(), any(), eq(CHECKOUT_ID))
        argumentCaptor<Error>().apply {
            inOrder.verify(view).showError(capture())
        }
    }

    @Test
    fun getShippingRatesShouldCallShippingRatesReceivedWhenOnSuccess() {
        val shippingRates = listOf<ShippingRate>()
        given(getShippingRatesUseCase.buildUseCaseSingle(any())).willReturn(Single.just(shippingRates))
        presenter.getShippingRates(CHECKOUT_ID)

        val inOrder = inOrder(view, getShippingRatesUseCase)
        inOrder.verify(getShippingRatesUseCase).execute(any(), any(), eq(CHECKOUT_ID))
        inOrder.verify(view).shippingRatesReceived(shippingRates)
    }

    @Test
    fun setShippingRatesShouldCallShowContentWhenOnSuccess() {
        val shippingRate: ShippingRate = mock()
        given(setShippingRateUseCase.buildUseCaseSingle(any())).willReturn(Single.just(checkout))
        presenter.setShippingRates(checkout, shippingRate)

        argumentCaptor<SetShippingRateUseCase.Params>().apply {
            val inOrder = inOrder(view, setShippingRateUseCase)
            inOrder.verify(setShippingRateUseCase).execute(any(), any(), capture())
            inOrder.verify(view).showContent(checkout)

            assertEquals(checkout.checkoutId, firstValue.checkoutId)
            assertEquals(shippingRate, firstValue.shippingRate)
        }
    }

    @Test
    fun setShippingRatesShouldCallShowContentWhenOnError() {
        val shippingRate: ShippingRate = mock()
        given(setShippingRateUseCase.buildUseCaseSingle(any())).willReturn(Single.error(Error.NonCritical("ErrorMessage")))
        presenter.setShippingRates(checkout, shippingRate)

        argumentCaptor<SetShippingRateUseCase.Params>().apply {
            val inOrder = inOrder(view, setShippingRateUseCase)
            inOrder.verify(setShippingRateUseCase).execute(any(), any(), capture())
            inOrder.verify(view).showContent(checkout)

            assertEquals(checkout.checkoutId, firstValue.checkoutId)
            assertEquals(shippingRate, firstValue.shippingRate)
        }
    }

    @Test
    fun completeCheckoutByCardShouldCallCheckoutCompletedWhenOnSuccess() {
        val resultOrder = MockInstantiator.newOrder()
        given(cartRemoveAllUseCase.buildUseCaseCompletable(any())).willReturn(Completable.complete())
        given(completeCheckoutByCardUseCase.buildUseCaseSingle(any())).willReturn(Single.just(resultOrder))
        presenter.completeCheckoutByCard(checkout, MockInstantiator.DEFAULT_EMAIL, address, CARD_TOKEN)

        argumentCaptor<CompleteCheckoutByCardUseCase.Params>().apply {
            val inOrder = inOrder(view, completeCheckoutByCardUseCase, cartRemoveAllUseCase)
            inOrder.verify(completeCheckoutByCardUseCase).execute(any(), any(), capture())
            inOrder.verify(cartRemoveAllUseCase).execute(any(), any(), any())
            inOrder.verify(view).checkoutCompleted(resultOrder)

            assertEquals(checkout, firstValue.checkout)
            assertEquals(MockInstantiator.DEFAULT_EMAIL, firstValue.email)
            assertEquals(address, firstValue.address)
            assertEquals(CARD_TOKEN, firstValue.creditCardVaultToken)
        }
    }

    @Test
    fun completeCheckoutByCardShouldCallCheckoutErrorWhenOnError() {
        val resultOrder = MockInstantiator.newOrder()
        given(cartRemoveAllUseCase.buildUseCaseCompletable(any())).willReturn(Completable.complete())
        given(completeCheckoutByCardUseCase.buildUseCaseSingle(any())).willReturn(Single.error(Error.Content(false)))
        presenter.completeCheckoutByCard(checkout, MockInstantiator.DEFAULT_EMAIL, address, CARD_TOKEN)

        argumentCaptor<CompleteCheckoutByCardUseCase.Params>().apply {
            val inOrder = inOrder(view, completeCheckoutByCardUseCase, cartRemoveAllUseCase)
            inOrder.verify(completeCheckoutByCardUseCase).execute(any(), any(), capture())
            inOrder.verify(cartRemoveAllUseCase, never()).execute(any(), any(), any())
            inOrder.verify(view, never()).checkoutCompleted(resultOrder)
            inOrder.verify(view).checkoutError()

            assertEquals(checkout, firstValue.checkout)
            assertEquals(MockInstantiator.DEFAULT_EMAIL, firstValue.email)
            assertEquals(address, firstValue.address)
            assertEquals(CARD_TOKEN, firstValue.creditCardVaultToken)
        }
    }

    @Test
    fun verifyCheckoutDataShouldPass() {
        presenter.verifyCheckoutData(checkout, address, MockInstantiator.DEFAULT_EMAIL,
            PaymentType.CARD_PAYMENT, card, CARD_TOKEN, address)

        verify(view).checkoutValidationPassed(true)
    }

    @Test
    fun verifyCheckoutDataShouldPassWhenPaymentTypeIsNotACardAndCardIsNull() {
        presenter.verifyCheckoutData(checkout, address, MockInstantiator.DEFAULT_EMAIL,
            PaymentType.WEB_PAYMENT, null, CARD_TOKEN, address)

        verify(view).checkoutValidationPassed(true)
    }

    @Test
    fun verifyCheckoutDataShouldNotPassWhenCheckoutIsNull() {
        presenter.verifyCheckoutData(null, address, MockInstantiator.DEFAULT_EMAIL,
            PaymentType.CARD_PAYMENT, card, CARD_TOKEN, address)

        verify(view).checkoutValidationPassed(false)
    }

    @Test
    fun verifyCheckoutDataShouldNotPassWhenEmailIsNull() {
        presenter.verifyCheckoutData(checkout, address, null, PaymentType.CARD_PAYMENT, card,
            CARD_TOKEN, address)

        verify(view).checkoutValidationPassed(false)
    }

    @Test
    fun verifyCheckoutDataShouldNotPassWhenShippingAddressIsNull() {
        presenter.verifyCheckoutData(checkout, null, MockInstantiator.DEFAULT_EMAIL,
            PaymentType.CARD_PAYMENT, card, CARD_TOKEN, address)

        verify(view).checkoutValidationPassed(false)
    }

    @Test
    fun verifyCheckoutDataShouldNotPassWhenPaymentTypeIsNull() {
        presenter.verifyCheckoutData(checkout, address, MockInstantiator.DEFAULT_EMAIL,
            null, card, CARD_TOKEN, address)

        verify(view).checkoutValidationPassed(false)
    }

    @Test
    fun verifyCheckoutDataShouldNotPassWhenPaymentTypeIsCardAndCardIsNull() {
        presenter.verifyCheckoutData(checkout, address, MockInstantiator.DEFAULT_EMAIL,
            PaymentType.CARD_PAYMENT, null, CARD_TOKEN, address)

        verify(view).checkoutValidationPassed(false)
    }

    @Test
    fun verifyCheckoutDataShouldNotPassWhenPaymentTypeIsCardAndCardTokenIsNull() {
        presenter.verifyCheckoutData(checkout, address, MockInstantiator.DEFAULT_EMAIL,
            PaymentType.CARD_PAYMENT, card, null, address)

        verify(view).checkoutValidationPassed(false)
    }

    @Test
    fun verifyCheckoutDataShouldNotPassWhenPaymentTypeIsCardAndBillingAddressIsNull() {
        presenter.verifyCheckoutData(checkout, address, MockInstantiator.DEFAULT_EMAIL,
            PaymentType.CARD_PAYMENT, card, CARD_TOKEN, null)

        verify(view).checkoutValidationPassed(false)
    }

    @Test
    fun verifyCheckoutDataShouldNotPassWhenShippingRateIsNull() {
        given(checkout.shippingRate).willReturn(null)
        presenter.verifyCheckoutData(checkout, address, MockInstantiator.DEFAULT_EMAIL,
            PaymentType.CARD_PAYMENT, card, CARD_TOKEN, address)

        verify(view).checkoutValidationPassed(false)
    }
}