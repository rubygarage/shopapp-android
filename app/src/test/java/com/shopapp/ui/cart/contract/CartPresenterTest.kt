package com.shopapp.ui.cart.contract

import com.nhaarman.mockito_kotlin.*
import com.shopapp.domain.interactor.cart.CartItemsUseCase
import com.shopapp.domain.interactor.cart.CartQuantityUseCase
import com.shopapp.domain.interactor.cart.CartRemoveUseCase
import com.shopapp.gateway.entity.CartProduct
import com.shopapp.test.MockInstantiator
import com.shopapp.test.RxImmediateSchedulerRule
import com.shopapp.test.ext.mock
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class CartPresenterTest {

    companion object {
        private const val SIZE = 5
        private const val PRODUCT_VARIANT_ID = "productVariantId"
        private const val PRODUCT_QUANTITY = 2
    }

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var view: CartView

    @Mock
    private lateinit var cartItemsUseCase: CartItemsUseCase

    @Mock
    private lateinit var cartRemoveUseCase: CartRemoveUseCase

    @Mock
    private lateinit var cartQuantityUseCase: CartQuantityUseCase

    private lateinit var presenter: CartPresenter

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        presenter = CartPresenter(cartItemsUseCase, cartRemoveUseCase, cartQuantityUseCase)
        presenter.attachView(view)
        cartItemsUseCase.mock()
        cartRemoveUseCase.mock()
        cartQuantityUseCase.mock()
    }

    @Test
    fun shouldCallUseCaseOnLoadItems() {
        val products: List<CartProduct> = mock()
        given(cartItemsUseCase.buildUseCaseObservable(any())).willReturn(Observable.just(products))
        presenter.loadCartItems()
        verify(cartItemsUseCase).execute(any(), any(), any(), any())
    }

    @Test
    fun shouldShowContent() {
        val products: List<CartProduct> = mock {
            on { size } doReturn SIZE
        }
        given(cartItemsUseCase.buildUseCaseObservable(any())).willReturn(Observable.just(products))
        presenter.loadCartItems()
        verify(view).showContent(products)
    }

    @Test
    fun shouldShowEmptyState() {
        given(cartItemsUseCase.buildUseCaseObservable(any())).willReturn(Observable.just(listOf()))
        presenter.loadCartItems()
        verify(view).showEmptyState()
    }

    @Test
    fun shouldPrintStackTraceOnLoadItemsError() {
        val error: Throwable = mock()
        given(cartItemsUseCase.buildUseCaseObservable(any())).willReturn(Observable.error(error))
        presenter.loadCartItems()

        val inOrder = inOrder(cartItemsUseCase, error)
        inOrder.verify(cartItemsUseCase).execute(any(), any(), any(), any())
        inOrder.verify(error).printStackTrace()
    }

    @Test
    fun shouldCallUseCaseOnRemoveProduct() {
        given(cartRemoveUseCase.buildUseCaseCompletable(any())).willReturn(Completable.complete())
        presenter.removeProduct(PRODUCT_VARIANT_ID)
        verify(cartRemoveUseCase).execute(any(), any(), eq(PRODUCT_VARIANT_ID))
    }

    @Test
    fun shouldPrintStackTraceOnRemoveProductError() {
        val error: Throwable = mock()
        given(cartRemoveUseCase.buildUseCaseCompletable(any())).willReturn(Completable.error(error))
        presenter.removeProduct(PRODUCT_VARIANT_ID)

        val inOrder = inOrder(cartRemoveUseCase, error)
        inOrder.verify(cartRemoveUseCase).execute(any(), any(), eq(PRODUCT_VARIANT_ID))
        inOrder.verify(error).printStackTrace()
    }

    @Test
    fun shouldCallUseCaseOnChangeQuantity() {
        val product: CartProduct = MockInstantiator.newCartProduct()
        given(cartQuantityUseCase.buildUseCaseSingle(any())).willReturn(Single.just(product))
        presenter.changeProductQuantity(PRODUCT_VARIANT_ID, PRODUCT_QUANTITY)
        verify(cartQuantityUseCase).execute(any(), any(), eq(CartQuantityUseCase.Params(PRODUCT_VARIANT_ID, PRODUCT_QUANTITY)))
    }

    @Test
    fun shouldPrintStackTraceOnChangeQuantityError() {
        val error: Throwable = mock()
        given(cartQuantityUseCase.buildUseCaseSingle(any())).willReturn(Single.error(error))
        presenter.changeProductQuantity(PRODUCT_VARIANT_ID, PRODUCT_QUANTITY)

        val inOrder = inOrder(cartQuantityUseCase, error)
        verify(cartQuantityUseCase).execute(any(), any(), eq(CartQuantityUseCase.Params(PRODUCT_VARIANT_ID, PRODUCT_QUANTITY)))
        inOrder.verify(error).printStackTrace()
    }

}