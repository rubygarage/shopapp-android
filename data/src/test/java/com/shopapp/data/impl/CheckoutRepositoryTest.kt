package com.shopapp.data.impl

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.given
import com.shopapp.data.RxImmediateSchedulerRule
import com.shopapp.domain.repository.CheckoutRepository
import com.shopapp.gateway.Api
import com.shopapp.gateway.ApiCallback
import com.shopapp.gateway.entity.*
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class CheckoutRepositoryTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var api: Api

    @Mock
    private lateinit var cardTypes: List<CardType>

    @Mock
    private lateinit var card: Card

    @Mock
    private lateinit var checkout: Checkout

    @Mock
    private lateinit var address: Address

    @Mock
    private lateinit var order: Order

    @Mock
    private lateinit var shippingRate: ShippingRate

    private lateinit var repository: CheckoutRepository
    private val cardTypesObserver: TestObserver<List<CardType>> = TestObserver()
    private val cardVerificationObserver: TestObserver<String> = TestObserver()
    private val checkoutObserver: TestObserver<Checkout> = TestObserver()
    private val orderObserver: TestObserver<Order> = TestObserver()
    private val shippingRatesObserver: TestObserver<List<ShippingRate>> = TestObserver()

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        repository = CheckoutRepositoryImpl(api)
    }

    @Test
    fun getAcceptedCardTypesShouldDelegateCallToApi() {
        repository.getAcceptedCardTypes().subscribe()
        verify(api).getAcceptedCardTypes(any())
    }

    @Test
    fun getAcceptedCardTypesShouldReturnValueWhenOnResult() {
        given(api.getAcceptedCardTypes(any())).willAnswer({
            val callback = it.getArgument<ApiCallback<List<CardType>>>(0)
            callback.onResult(cardTypes)
        })
        repository.getAcceptedCardTypes().subscribe(cardTypesObserver)
        cardTypesObserver.assertValue(cardTypes)
    }

    @Test
    fun getAcceptedCardTypesShouldReturnErrorOnFailure() {
        val error = Error.Content()
        given(api.getAcceptedCardTypes(any())).willAnswer({
            val callback = it.getArgument<ApiCallback<List<CardType>>>(0)
            callback.onFailure(error)
        })
        repository.getAcceptedCardTypes().subscribe(cardTypesObserver)
        cardTypesObserver.assertError(error)
    }

    @Test
    fun getCardTokenShouldDelegateCallToApi() {
        repository.getCardToken(card).subscribe()
        verify(api).getCardToken(eq(card), any())
    }

    @Test
    fun getCardTokenShouldReturnValueWhenOnResult() {
        val token = "token"
        given(api.getCardToken(eq(card), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<String>>(1)
            callback.onResult(token)
        })
        repository.getCardToken(card).subscribe(cardVerificationObserver)
        cardVerificationObserver.assertValue(token)
    }

    @Test
    fun getCardTokenShouldReturnErrorOnFailure() {
        val error = Error.Content()
        given(api.getCardToken(eq(card), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<String>>(1)
            callback.onFailure(error)
        })
        repository.getCardToken(card).subscribe(cardVerificationObserver)
        cardVerificationObserver.assertError(error)
    }

    @Test
    fun getCheckoutShouldDelegateCallToApi() {
        val checkoutId = "checkoutId"
        repository.getCheckout(checkoutId).subscribe()
        verify(api).getCheckout(eq(checkoutId), any())
    }

    @Test
    fun getCheckoutShouldReturnValueWhenOnResult() {
        val checkoutId = "checkoutId"
        given(api.getCheckout(eq(checkoutId), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Checkout>>(1)
            callback.onResult(checkout)
        })
        repository.getCheckout(checkoutId).subscribe(checkoutObserver)
        checkoutObserver.assertValue(checkout)
    }

    @Test
    fun getCheckoutShouldReturnErrorOnFailure() {
        val checkoutId = "checkoutId"
        val error = Error.Content()
        given(api.getCheckout(eq(checkoutId), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Checkout>>(1)
            callback.onFailure(error)
        })
        repository.getCheckout(checkoutId).subscribe(checkoutObserver)
        checkoutObserver.assertError(error)
    }

    @Test
    fun createCheckoutShouldDelegateCallToApi() {
        val productList = listOf<CartProduct>()
        repository.createCheckout(productList).subscribe()
        verify(api).createCheckout(eq(productList), any())
    }

    @Test
    fun createCheckoutShouldReturnValueWhenOnResult() {
        val productList = listOf<CartProduct>()
        given(api.createCheckout(eq(productList), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Checkout>>(1)
            callback.onResult(checkout)
        })
        repository.createCheckout(productList).subscribe(checkoutObserver)
        checkoutObserver.assertValue(checkout)
    }

    @Test
    fun createCheckoutShouldReturnErrorOnFailure() {
        val productList = listOf<CartProduct>()
        val error = Error.Content()
        given(api.createCheckout(eq(productList), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Checkout>>(1)
            callback.onFailure(error)
        })
        repository.createCheckout(productList).subscribe(checkoutObserver)
        checkoutObserver.assertError(error)
    }

    @Test
    fun completeCheckoutByCardShouldDelegateCallToApi() {
        val email = "email"
        val token = "token"
        repository.completeCheckoutByCard(checkout, email, address, token).subscribe()
        verify(api).completeCheckoutByCard(eq(checkout), eq(email), eq(address), eq(token), any())
    }

    @Test
    fun completeCheckoutByCardShouldReturnValueWhenOnResult() {
        val email = "email"
        val token = "token"
        given(api.completeCheckoutByCard(eq(checkout), eq(email), eq(address), eq(token), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Order>>(4)
            callback.onResult(order)
        })
        repository.completeCheckoutByCard(checkout, email, address, token).subscribe(orderObserver)
        orderObserver.assertValue(order)
    }

    @Test
    fun completeCheckoutByCardShouldReturnErrorOnFailure() {
        val error = Error.Content()
        val email = "email"
        val token = "token"
        given(api.completeCheckoutByCard(eq(checkout), eq(email), eq(address), eq(token), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Order>>(4)
            callback.onFailure(error)
        })
        repository.completeCheckoutByCard(checkout, email, address, token).subscribe(orderObserver)
        orderObserver.assertError(error)
    }

    @Test
    fun getShippingRatesShouldDelegateCallToApi() {
        val checkoutId = "checkoutId"
        repository.getShippingRates(checkoutId).subscribe()
        verify(api).getShippingRates(eq(checkoutId), any())
    }

    @Test
    fun getShippingRatesShouldReturnValueWhenOnResult() {
        val checkoutId = "checkoutId"
        val shippingRates = listOf<ShippingRate>()
        given(api.getShippingRates(eq(checkoutId), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<List<ShippingRate>>>(1)
            callback.onResult(shippingRates)
        })
        repository.getShippingRates(checkoutId).subscribe(shippingRatesObserver)
        shippingRatesObserver.assertValue(shippingRates)
    }

    @Test
    fun getShippingRatesShouldReturnErrorOnFailure() {
        val error = Error.Content()
        val checkoutId = "checkoutId"
        given(api.getShippingRates(eq(checkoutId), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<List<ShippingRate>>>(1)
            callback.onFailure(error)
        })
        repository.getShippingRates(checkoutId).subscribe(shippingRatesObserver)
        shippingRatesObserver.assertError(error)
    }

    @Test
    fun selectShippingRateShouldDelegateCallToApi() {
        val checkoutId = "checkoutId"
        repository.selectShippingRate(checkoutId, shippingRate).subscribe()
        verify(api).selectShippingRate(eq(checkoutId), eq(shippingRate), any())
    }

    @Test
    fun selectShippingRateShouldReturnValueWhenOnResult() {
        val checkoutId = "checkoutId"
        given(api.selectShippingRate(eq(checkoutId), eq(shippingRate), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Checkout>>(2)
            callback.onResult(checkout)
        })
        repository.selectShippingRate(checkoutId, shippingRate).subscribe(checkoutObserver)
        checkoutObserver.assertValue(checkout)
    }

    @Test
    fun selectShippingRateShouldReturnErrorOnFailure() {
        val error = Error.Content()
        val checkoutId = "checkoutId"
        given(api.selectShippingRate(eq(checkoutId), eq(shippingRate), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Checkout>>(2)
            callback.onFailure(error)
        })
        repository.selectShippingRate(checkoutId, shippingRate).subscribe(checkoutObserver)
        checkoutObserver.assertError(error)
    }

    @Test
    fun setShippingAddressShouldDelegateCallToApi() {
        val checkoutId = "checkoutId"
        repository.setShippingAddress(checkoutId, address).subscribe()
        verify(api).setShippingAddress(eq(checkoutId), eq(address), any())
    }

    @Test
    fun setShippingAddressShouldReturnValueWhenOnResult() {
        val checkoutId = "checkoutId"
        given(api.setShippingAddress(eq(checkoutId), eq(address), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Checkout>>(2)
            callback.onResult(checkout)
        })
        repository.setShippingAddress(checkoutId, address).subscribe(checkoutObserver)
        checkoutObserver.assertValue(checkout)
    }

    @Test
    fun setShippingAddressShouldReturnErrorOnFailure() {
        val error = Error.Content()
        val checkoutId = "checkoutId"
        given(api.setShippingAddress(eq(checkoutId), eq(address), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Checkout>>(2)
            callback.onFailure(error)
        })
        repository.setShippingAddress(checkoutId, address).subscribe(checkoutObserver)
        checkoutObserver.assertError(error)
    }
}