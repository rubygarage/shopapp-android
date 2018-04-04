package com.shopapp.domain.interactor.cart

import com.nhaarman.mockito_kotlin.*
import com.shopapp.domain.repository.CartRepository
import com.shopapp.domain.repository.ProductRepository
import com.shopapp.gateway.entity.CartProduct
import com.shopapp.gateway.entity.ProductVariant
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CartValidationUseCaseTest {

    @Mock
    private lateinit var cartRepository: CartRepository

    @Mock
    private lateinit var productRepository: ProductRepository

    private lateinit var useCase: CartValidationUseCase

    @Before
    fun setUpTest() {
        useCase = CartValidationUseCase(cartRepository, productRepository)
    }

    @Test
    fun shouldDoNothingWhenCartIsEmpty() {
        given(cartRepository.getCartProductList()).willReturn(Observable.just(listOf()))
        val testObserver = useCase.buildUseCaseCompletable(Unit).test()

        verify(cartRepository).getCartProductList()
        verify(productRepository, never()).getProductVariantList(any())
        verify(cartRepository, never()).deleteProductListFromCart(any())
        testObserver.assertComplete()
    }

    @Test
    fun shouldGetProductVariantListWhenCartIsNotEmpty() {
        val testId = "test id"
        val productVariant: ProductVariant = mock()
        given(productVariant.id).willReturn(testId)

        val cartProduct: CartProduct = mock()
        given(cartProduct.productVariant).willReturn(productVariant)

        given(cartRepository.getCartProductList()).willReturn(Observable.just(listOf(cartProduct)))
        given(productRepository.getProductVariantList(any())).willReturn(Single.just(listOf(productVariant)))
        val testObserver = useCase.buildUseCaseCompletable(Unit).test()

        verify(cartRepository).getCartProductList()
        argumentCaptor<List<String>>().apply {
            verify(productRepository).getProductVariantList(capture())
            assertEquals(testId, firstValue.first())
        }
        verify(cartRepository, never()).deleteProductListFromCart(any())
        testObserver.assertComplete()
    }

    @Test
    fun shouldDeleteCartProductItemWhenProductVariantListIsEmpty() {
        val testId = "test id"
        val productVariant: ProductVariant = mock()
        given(productVariant.id).willReturn(testId)

        val cartProduct: CartProduct = mock()
        given(cartProduct.productVariant).willReturn(productVariant)

        given(cartRepository.getCartProductList()).willReturn(Observable.just(listOf(cartProduct)))
        given(productRepository.getProductVariantList(any())).willReturn(Single.just(listOf()))
        given(cartRepository.deleteProductListFromCart(any())).willReturn(Completable.complete())
        val testObserver = useCase.buildUseCaseCompletable(Unit).test()

        verify(cartRepository).getCartProductList()
        argumentCaptor<List<String>>().apply {
            verify(productRepository).getProductVariantList(capture())
            assertEquals(testId, firstValue.first())
        }
        argumentCaptor<List<String>>().apply {
            verify(cartRepository).deleteProductListFromCart(capture())
            assertEquals(testId, firstValue.first())
        }
        testObserver.assertComplete()
    }

    @Test
    fun shouldDeleteCartProductItemWhenProductVariantWithDifferentIdFromCartProductId() {
        val testId = "test id"
        val cartProductVariant: ProductVariant = mock()
        given(cartProductVariant.id).willReturn(testId)

        val cartProduct: CartProduct = mock()
        given(cartProduct.productVariant).willReturn(cartProductVariant)

        val anotherTestId = "another test id"
        val productVariant: ProductVariant = mock()
        given(productVariant.id).willReturn(anotherTestId)

        given(cartRepository.getCartProductList()).willReturn(Observable.just(listOf(cartProduct)))
        given(productRepository.getProductVariantList(any())).willReturn(Single.just(listOf(productVariant)))
        given(cartRepository.deleteProductListFromCart(any())).willReturn(Completable.complete())
        val testObserver = useCase.buildUseCaseCompletable(Unit).test()

        verify(cartRepository).getCartProductList()
        argumentCaptor<List<String>>().apply {
            verify(productRepository).getProductVariantList(capture())
            assertEquals(testId, firstValue.first())
        }
        argumentCaptor<List<String>>().apply {
            verify(cartRepository).deleteProductListFromCart(capture())
            assertEquals(testId, firstValue.first())
        }
        testObserver.assertComplete()
    }
}