package com.shopapp.domain.interactor.checkout

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.domain.repository.AuthRepository
import com.shopapp.domain.repository.CartRepository
import com.shopapp.domain.repository.CheckoutRepository
import com.shopapp.gateway.entity.*
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SetupCheckoutUseCaseTest {

    companion object {
        private const val CHECKOUT_ID = "checkout_id"
    }

    @Mock
    private lateinit var cartRepository: CartRepository

    @Mock
    private lateinit var checkoutRepository: CheckoutRepository

    @Mock
    private lateinit var authRepository: AuthRepository

    @Mock
    private lateinit var productList: List<CartProduct>

    @Mock
    private lateinit var checkout: Checkout

    @Mock
    private lateinit var customer: Customer

    @Mock
    private lateinit var address: Address

    private lateinit var useCase: SetupCheckoutUseCase

    @Before
    fun setUpTest() {
        useCase = SetupCheckoutUseCase(cartRepository, checkoutRepository, authRepository)
        given(cartRepository.getCartProductList()).willReturn(Observable.just(productList))
        given(checkoutRepository.createCheckout(any())).willReturn(Single.just(checkout))
        given(checkoutRepository.setShippingAddress(any(), any())).willReturn(Single.just(checkout))
        given(authRepository.isLoggedIn()).willReturn(Single.just(true))
        given(authRepository.getCustomer()).willReturn(Single.just(customer))

        given(checkout.checkoutId).willReturn(CHECKOUT_ID)
        given(customer.defaultAddress).willReturn(address)
    }

    @Test
    fun shouldReturnFullCheckoutDataAndSuccessfullySetAddress() {
        val testObserver = useCase.buildUseCaseSingle(Unit).test()

        verify(cartRepository).getCartProductList()
        verify(checkoutRepository).createCheckout(productList)
        verify(authRepository).isLoggedIn()
        verify(authRepository).getCustomer()
        verify(checkoutRepository).setShippingAddress(CHECKOUT_ID, address)

        testObserver.assertValue(Triple(productList, checkout, customer))
    }

    @Test
    fun shouldReturnFullCheckoutDataAndSetAddressError() {
        given(checkoutRepository.setShippingAddress(any(), any())).willReturn(Single.error(Error.Content()))
        val testObserver = useCase.buildUseCaseSingle(Unit).test()

        verify(cartRepository).getCartProductList()
        verify(checkoutRepository).createCheckout(productList)
        verify(authRepository).isLoggedIn()
        verify(authRepository).getCustomer()
        verify(checkoutRepository).setShippingAddress(CHECKOUT_ID, address)

        testObserver.assertValue(Triple(productList, checkout, customer))
    }

    @Test
    fun shouldReturnFullCheckoutData() {
        given(customer.defaultAddress).willReturn(null)
        val testObserver = useCase.buildUseCaseSingle(Unit).test()

        verify(cartRepository).getCartProductList()
        verify(checkoutRepository).createCheckout(productList)
        verify(authRepository).isLoggedIn()
        verify(authRepository).getCustomer()
        verify(checkoutRepository, never()).setShippingAddress(CHECKOUT_ID, address)

        testObserver.assertValue(Triple(productList, checkout, customer))
    }

    @Test
    fun shouldReturnCheckoutDataWithoutCustomer() {
        given(authRepository.isLoggedIn()).willReturn(Single.just(false))
        val testObserver = useCase.buildUseCaseSingle(Unit).test()

        verify(cartRepository).getCartProductList()
        verify(checkoutRepository).createCheckout(productList)
        verify(authRepository).isLoggedIn()
        verify(authRepository, never()).getCustomer()
        verify(checkoutRepository, never()).setShippingAddress(CHECKOUT_ID, address)

        testObserver.assertValue(Triple(productList, checkout, null))
    }
}