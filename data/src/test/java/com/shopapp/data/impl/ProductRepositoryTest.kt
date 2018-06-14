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
import com.shopapp.gateway.entity.ProductVariant
import com.shopapp.gateway.entity.SortType
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ProductRepositoryTest {

    companion object {
        private const val PER_PAGE = 10
        private const val PAGINATION_VALUE = "pagination_value"
        private const val KEYWORD = "keyword"
        private const val QUERY = "query"
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
    private lateinit var productVariantList: List<ProductVariant>

    @Mock
    private lateinit var product: Product

    private lateinit var repository: ProductRepository
    private val productListObserver: TestObserver<List<Product>> = TestObserver()
    private val productVariantListObserver: TestObserver<List<ProductVariant>> = TestObserver()
    private val productObserver: TestObserver<Product> = TestObserver()

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        repository = ProductRepositoryImpl(api)
    }

    @Test
    fun getProductsShouldDelegateCallToApi() {
        repository.getProducts(PER_PAGE, PAGINATION_VALUE, sortBy, KEYWORD, EXCLUDE_KEYWORD)
            .subscribe()
        verify(api).getProducts(
            eq(PER_PAGE),
            eq(PAGINATION_VALUE),
            eq(sortBy),
            eq(KEYWORD),
            eq(EXCLUDE_KEYWORD),
            any()
        )
    }

    @Test
    fun getProductsShouldReturnValueWhenOnResult() {
        given(
            api.getProducts(
                eq(PER_PAGE), eq(PAGINATION_VALUE), eq(sortBy), eq(KEYWORD),
                eq(EXCLUDE_KEYWORD), any()
            )
        ).willAnswer({
            val callback = it.getArgument<ApiCallback<List<Product>>>(5)
            callback.onResult(productList)
        })
        repository.getProducts(PER_PAGE, PAGINATION_VALUE, sortBy, KEYWORD, EXCLUDE_KEYWORD)
            .subscribe(productListObserver)
        productListObserver.assertValue(productList)
    }

    @Test
    fun getProductsShouldReturnErrorOnFailure() {
        val error = Error.Content()
        given(
            api.getProducts(
                eq(PER_PAGE), eq(PAGINATION_VALUE), eq(sortBy), eq(KEYWORD),
                eq(EXCLUDE_KEYWORD), any()
            )
        ).willAnswer({
            val callback = it.getArgument<ApiCallback<List<Product>>>(5)
            callback.onFailure(error)
        })
        repository.getProducts(PER_PAGE, PAGINATION_VALUE, sortBy, KEYWORD, EXCLUDE_KEYWORD)
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

    @Test
    fun getProductVariantsShouldDelegateCallToApi() {
        repository.getProductVariants(listOf(PRODUCT_ID)).subscribe()
        verify(api).getProductVariants(eq(listOf(PRODUCT_ID)), any())
    }

    @Test
    fun getProductVariantsShouldReturnValueWhenOnResult() {
        given(api.getProductVariants(eq(listOf(PRODUCT_ID)), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<List<ProductVariant>>>(1)
            callback.onResult(productVariantList)
        })
        repository.getProductVariants(listOf(PRODUCT_ID)).subscribe(productVariantListObserver)
        productVariantListObserver.assertValue(productVariantList)
    }

    @Test
    fun getProductVariantsShouldReturnErrorOnFailure() {
        val error = Error.Content()
        given(api.getProductVariants(eq(listOf(PRODUCT_ID)), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<List<ProductVariant>>>(1)
            callback.onFailure(error)
        })
        repository.getProductVariants(listOf(PRODUCT_ID)).subscribe(productVariantListObserver)
        productVariantListObserver.assertError(error)
    }

    @Test
    fun searchProductsShouldDelegateCallToApi() {
        repository.searchProducts(QUERY, PER_PAGE, PAGINATION_VALUE).subscribe()
        verify(api).searchProducts(eq(PER_PAGE), eq(PAGINATION_VALUE), eq(QUERY), any())
    }

    @Test
    fun searchProductsShouldReturnValueWhenOnResult() {
        given(api.searchProducts(eq(PER_PAGE), eq(PAGINATION_VALUE), eq(QUERY), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<List<Product>>>(3)
            callback.onResult(productList)
        })
        repository.searchProducts(QUERY, PER_PAGE, PAGINATION_VALUE)
            .test()
            .assertValue(productList)
    }

    @Test
    fun searchProductsShouldReturnErrorOnFailure() {
        val error = Error.Content()
        given(api.searchProducts(eq(PER_PAGE), eq(PAGINATION_VALUE), eq(QUERY), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<List<Product>>>(3)
            callback.onFailure(error)
        })
        repository.searchProducts(QUERY, PER_PAGE, PAGINATION_VALUE)
            .test()
            .assertError(error)
    }
}