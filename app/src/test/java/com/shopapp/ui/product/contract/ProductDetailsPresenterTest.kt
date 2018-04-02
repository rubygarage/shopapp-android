package com.shopapp.ui.product.contract

import com.nhaarman.mockito_kotlin.*
import com.shopapp.R
import com.shopapp.domain.interactor.cart.CartAddItemUseCase
import com.shopapp.domain.interactor.product.ProductDetailsUseCase
import com.shopapp.gateway.entity.CartProduct
import com.shopapp.gateway.entity.Error
import com.shopapp.gateway.entity.Product
import com.shopapp.test.MockInstantiator
import com.shopapp.test.RxImmediateSchedulerRule
import com.shopapp.test.ext.mock
import io.reactivex.Single
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner.Silent::class)
class ProductDetailsPresenterTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var view: ProductDetailsView

    @Mock
    private lateinit var productDetailsUseCase: ProductDetailsUseCase

    @Mock
    private lateinit var cartAddItemUseCase: CartAddItemUseCase

    @Mock
    private lateinit var data: Product

    private lateinit var presenter: ProductDetailsPresenter

    @Before
    fun setUpTest() {
        presenter = ProductDetailsPresenter(productDetailsUseCase, cartAddItemUseCase)
        presenter.attachView(view)
        productDetailsUseCase.mock()
        cartAddItemUseCase.mock()
    }

    @Test
    fun shouldShowContentOnSingleSuccess() {
        given(productDetailsUseCase.buildUseCaseSingle(any())).willReturn(Single.just(data))
        presenter.loadProductDetails(MockInstantiator.DEFAULT_ID)

        val inOrder = inOrder(view, productDetailsUseCase)
        inOrder.verify(productDetailsUseCase).execute(any(), any(), eq(MockInstantiator.DEFAULT_ID))
        inOrder.verify(view).showContent(data)
    }

    @Test
    fun shouldShowMessageOnUseCaseNonCriticalError() {
        given(productDetailsUseCase.buildUseCaseSingle(any())).willReturn(Single.error(Error.NonCritical("ErrorMessage")))
        presenter.loadProductDetails(MockInstantiator.DEFAULT_ID)

        val inOrder = inOrder(view, productDetailsUseCase)
        inOrder.verify(productDetailsUseCase).execute(any(), any(), eq(MockInstantiator.DEFAULT_ID))
        inOrder.verify(view).showMessage("ErrorMessage")
    }

    @Test
    fun shouldShowMessageOnInvalidQuantityString() {
        val productVariant = MockInstantiator.newProductVariant()
        presenter.addProductToCart(productVariant, MockInstantiator.DEFAULT_TITLE,
            MockInstantiator.DEFAULT_CURRENCY, "invalidQuantityString")

        verify(view).showMessage(R.string.quantity_warning_message)
    }

    @Test
    fun shouldShowErrorOnUseCaseContentError() {
        given(productDetailsUseCase.buildUseCaseSingle(any())).willReturn(Single.error(Error.Content()))
        presenter.loadProductDetails(MockInstantiator.DEFAULT_ID)

        val inOrder = inOrder(view, productDetailsUseCase)
        inOrder.verify(productDetailsUseCase).execute(any(), any(), eq(MockInstantiator.DEFAULT_ID))
        argumentCaptor<Error>().apply {
            inOrder.verify(view).showError(capture())
            assertTrue(firstValue is Error.Content)
        }
    }

    @Test
    fun shouldShowErrorMessageWhenQuantityLessThanOne() {
        val productVariant = MockInstantiator.newProductVariant()
        presenter.addProductToCart(productVariant, MockInstantiator.DEFAULT_TITLE,
            MockInstantiator.DEFAULT_CURRENCY, "0")

        verify(view).showMessage(R.string.quantity_warning_message)
    }

    @Test
    fun shouldProductBeSuccessfullyAddedToCart() {
        val cartProduct: CartProduct = MockInstantiator.newCartProduct()
        val productVariant = cartProduct.productVariant
        given(cartAddItemUseCase.buildUseCaseSingle(any())).willReturn(Single.just(cartProduct))
        presenter.addProductToCart(productVariant, MockInstantiator.DEFAULT_TITLE,
            MockInstantiator.DEFAULT_CURRENCY, "1")

        argumentCaptor<CartProduct>().apply {
            verify(cartAddItemUseCase).execute(any(), any(), capture())
            assertEquals(1, firstValue.quantity)
            assertEquals(MockInstantiator.DEFAULT_CURRENCY, firstValue.currency)
            assertEquals(MockInstantiator.DEFAULT_TITLE, firstValue.title)
            assertEquals(productVariant, firstValue.productVariant)
        }
        verify(view).productAddedToCart()
    }

    @Test
    fun shouldShowErrorWhenProductAddedToCartFailure() {
        val cartProduct: CartProduct = MockInstantiator.newCartProduct()
        val productVariant = cartProduct.productVariant
        given(cartAddItemUseCase.buildUseCaseSingle(any())).willReturn(Single.error(Error.NonCritical("ErrorMessage")))
        presenter.addProductToCart(productVariant, MockInstantiator.DEFAULT_TITLE,
            MockInstantiator.DEFAULT_CURRENCY, "1")

        argumentCaptor<CartProduct>().apply {
            verify(cartAddItemUseCase).execute(any(), any(), capture())
            assertEquals(1, firstValue.quantity)
            assertEquals(MockInstantiator.DEFAULT_CURRENCY, firstValue.currency)
            assertEquals(MockInstantiator.DEFAULT_TITLE, firstValue.title)
            assertEquals(productVariant, firstValue.productVariant)
        }
        verify(view).showMessage("ErrorMessage")
    }

    @After
    fun tearDown() {
        presenter.detachView(false)
    }
}