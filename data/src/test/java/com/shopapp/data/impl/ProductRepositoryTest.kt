package com.shopapp.data.impl

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.data.RxImmediateSchedulerRule
import com.shopapp.domain.repository.ProductRepository
import com.shopapp.gateway.Api
import com.shopapp.gateway.ApiCallback
import com.shopapp.gateway.entity.Error
import com.shopapp.gateway.entity.Product
import com.shopapp.gateway.entity.SortType
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ProductRepositoryTest {

    companion object {
        private const val PER_PAGE = 10
        private const val PAGINATION_VALUE = "pagination_value"
        private const val KEYWORD = "keyword"
        private const val EXCLUDE_KEYWORD = "exclude_keyword"
        private const val PRODUCT_ID = "product_id"
        private val sortBy = SortType.RELEVANT
    }

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var api: Api

    @Mock
    private lateinit var productList: List<Product>

    @Mock
    private lateinit var product: Product

    private lateinit var repository: ProductRepository
    private lateinit var productListObserver: TestObserver<List<Product>>
    private lateinit var productObserver: TestObserver<Product>

    @Before
    fun setUpTest() {
        repository = ProductRepositoryImpl(api)
        productListObserver = TestObserver()
        productObserver = TestObserver()
    }

    @Test
    fun getProductListShouldDelegateCallToApi() {
        repository.getProductList(PER_PAGE, PAGINATION_VALUE, sortBy, KEYWORD, EXCLUDE_KEYWORD).subscribe()
        verify(api).getProductList(eq(PER_PAGE), eq(PAGINATION_VALUE), eq(sortBy), eq(KEYWORD), eq(EXCLUDE_KEYWORD), any())
    }

    @Test
    fun getProductListShouldReturnValueWhenOnResult() {
        given(api.getProductList(eq(PER_PAGE), eq(PAGINATION_VALUE), eq(sortBy), eq(KEYWORD),
            eq(EXCLUDE_KEYWORD), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<List<Product>>>(5)
            callback.onResult(productList)
        })
        repository.getProductList(PER_PAGE, PAGINATION_VALUE, sortBy, KEYWORD, EXCLUDE_KEYWORD)
            .subscribe(productListObserver)
        productListObserver.assertValue(productList)
    }

    @Test
    fun getProductListShouldReturnErrorOnFailure() {
        val error = Error.Content()
        given(api.getProductList(eq(PER_PAGE), eq(PAGINATION_VALUE), eq(sortBy), eq(KEYWORD),
            eq(EXCLUDE_KEYWORD), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<List<Product>>>(5)
            callback.onFailure(error)
        })
        repository.getProductList(PER_PAGE, PAGINATION_VALUE, sortBy, KEYWORD, EXCLUDE_KEYWORD)
            .subscribe(productListObserver)
        productListObserver.assertError(error)
    }

    @Test
    fun getProductShouldDelegateCallToApi() {
        repository.getProduct(PRODUCT_ID).subscribe()
        verify(api).getProduct(eq(PRODUCT_ID), any())
    }

    @Test
    fun getProductShouldReturnValueWhenOnResult() {
        given(api.getProduct(eq(PRODUCT_ID), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Product>>(1)
            callback.onResult(product)
        })
        repository.getProduct(PRODUCT_ID).subscribe(productObserver)
        productObserver.assertValue(product)
    }

    @Test
    fun getProductShouldReturnErrorOnFailure() {
        val error = Error.Content()
        given(api.getProduct(eq(PRODUCT_ID), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Product>>(1)
            callback.onFailure(error)
        })
        repository.getProduct(PRODUCT_ID).subscribe(productObserver)
        productObserver.assertError(error)
    }
}