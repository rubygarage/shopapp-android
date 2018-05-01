package com.shopapp.magento.api

import android.content.Context
import com.shopapp.gateway.Api
import com.shopapp.gateway.ApiCallback
import com.shopapp.gateway.entity.*
import com.shopapp.magento.adapter.ProductVariantAdapter
import com.shopapp.magento.api.Constant.ATTRIBUTE_SET_ID_FIELD
import com.shopapp.magento.api.Constant.CATEGORY_ID_FIELD
import com.shopapp.magento.api.Constant.CREATED_AT_FIELD
import com.shopapp.magento.api.Constant.NAME_FIELD
import com.shopapp.magento.api.Constant.PAGINATION_END_VALUE
import com.shopapp.magento.api.Constant.PAGINATION_START_VALUE
import com.shopapp.magento.api.Constant.PRICE_FIELD
import com.shopapp.magento.api.Constant.PRODUCT_DEFAULT_TYPE_ID
import com.shopapp.magento.api.Constant.SKU_FIELD
import com.shopapp.magento.api.Constant.TYPE_ID_FIELD
import com.shopapp.magento.api.request.ConditionType
import com.shopapp.magento.api.request.PaginationPart
import com.shopapp.magento.api.request.ProductOptionBuilder
import com.shopapp.magento.retrofit.RestClient
import com.shopapp.magento.retrofit.service.CategoryService
import com.shopapp.magento.retrofit.service.ProductService
import com.shopapp.magento.retrofit.service.StoreService
import retrofit2.Retrofit

class MagentoApi(context: Context) : Api {

    companion object {
        const val HOST = "http://10.14.14.174/"
        private const val BASE_PATH = "rest/V1/"
    }

    private val retrofit: Retrofit = RestClient.providesRetrofit(context, HOST + BASE_PATH)

    private val productService by lazy {
        retrofit.create(ProductService::class.java)
    }

    private val storeService by lazy {
        retrofit.create(StoreService::class.java)
    }

    private val categoryService by lazy {
        retrofit.create(CategoryService::class.java)
    }

    //PRODUCT

    override fun getProduct(id: String, callback: ApiCallback<Product>) {

        storeService.getStoreConfigs()
            .flatMap {
                val currency = it.getCurrency()
                productService.getProduct(id).map { it.mapToEntity(currency) }
            }
            .subscribe(
                { callback.onResult(it) },
                { it.printStackTrace() }
            )
    }

    override fun getProductList(perPage: Int, paginationValue: Any?, sortBy: SortType?,
                                keyword: String?, excludeKeyword: String?,
                                callback: ApiCallback<List<Product>>) {

        val optionBuilder = ProductOptionBuilder()
        if (sortBy == SortType.RECENT) {
            optionBuilder.addSortOrder(CREATED_AT_FIELD, true)
        } else if (sortBy == SortType.TYPE && keyword != null) {
            optionBuilder.addFilterGroup(ATTRIBUTE_SET_ID_FIELD, keyword)
            excludeKeyword?.let {
                optionBuilder.addFilterGroup(NAME_FIELD, it, ConditionType.NOT_EQUAL)
            }
        } else if (sortBy == SortType.RELEVANT) {
            callback.onResult(listOf())
            return
        }

        getProductList(perPage, paginationValue, optionBuilder, callback)
    }

    override fun searchProductList(perPage: Int, paginationValue: Any?, searchQuery: String, callback: ApiCallback<List<Product>>) {

        val additionalOptions = ProductOptionBuilder()
            .addFilterGroup(NAME_FIELD, "%$searchQuery%", ConditionType.SEARCH_CONDITION)
            .build()
        getProductList(perPage, paginationValue, additionalOptions, callback)
    }

    override fun getProductVariantList(productVariantIdList: List<String>, callback: ApiCallback<List<ProductVariant>>) {

        val optionBuilder = ProductOptionBuilder()
        optionBuilder.addFilterGroup(TYPE_ID_FIELD, PRODUCT_DEFAULT_TYPE_ID)
        optionBuilder.addFilterGroup {
            val filterBuilder = ProductOptionBuilder.FilterBuilder()
            for (sku in productVariantIdList) {
                filterBuilder.addFilter(it, SKU_FIELD, sku)
            }
            filterBuilder
        }
        val options = optionBuilder.build()

        storeService.getStoreConfigs()
            .flatMap { response ->
                productService.getProductList(options)
                    .map {
                        it.mapToEntityList(response.getCurrency(), 1, 1)
                            .map { ProductVariantAdapter.adapt(it) }
                    }
            }
            .subscribe(
                { callback.onResult(it) },
                { it.printStackTrace() }
            )
    }

    //CATEGORY

    override fun getCategoryDetails(id: String, perPage: Int, paginationValue: Any?, sortBy: SortType?, callback: ApiCallback<Category>) {

        val page = calculatePage(paginationValue)
        val optionsBuilder = ProductOptionBuilder().addFilterGroup(CATEGORY_ID_FIELD, id)
            .addFilterGroup(TYPE_ID_FIELD, PRODUCT_DEFAULT_TYPE_ID)
            .addSearchCriteria(PaginationPart.PAGE_SIZE.value, perPage)
            .addSearchCriteria(PaginationPart.CURRENT_PAGE.value, page)

        when (sortBy) {
            SortType.NAME -> optionsBuilder.addSortOrder(NAME_FIELD)
            SortType.RECENT -> optionsBuilder.addSortOrder(CREATED_AT_FIELD, true)
            SortType.PRICE_HIGH_TO_LOW -> optionsBuilder.addSortOrder(PRICE_FIELD, true)
            SortType.PRICE_LOW_TO_HIGH -> optionsBuilder.addSortOrder(PRICE_FIELD
            )
            else -> {
                /*do nothing*/
            }
        }

        if (sortBy == SortType.RECENT) {
            optionsBuilder.addSortOrder(CREATED_AT_FIELD, true)
        }

        val options = optionsBuilder.build()

        storeService.getStoreConfigs()
            .flatMap { response ->
                productService.getProductList(options)
                    .map { it.mapToEntityList(response.getCurrency(), page, perPage) }
            }
            .flatMap {
                val productList = if (page == PAGINATION_END_VALUE) listOf() else it
                categoryService.getCategoryDetails(id)
                    .map {
                        it.mapToEntity(productList)
                    }
            }
            .subscribe(
                { callback.onResult(it) },
                { it.printStackTrace() }
            )
    }

    override fun getCategoryList(perPage: Int, paginationValue: Any?, rootCategoryId: String?,
                                 callback: ApiCallback<List<Category>>) {

        if (paginationValue == null) {
            categoryService.getCategoryList(rootCategoryId)
                .map { it.mapToEntityList() }
                .subscribe(
                    { callback.onResult(it) },
                    { it.printStackTrace() }
                )
        } else {
            callback.onResult(listOf())
        }
    }

    //OTHER

    override fun changePassword(password: String, callback: ApiCallback<Unit>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun completeCheckoutByCard(checkout: Checkout, email: String, address: Address, creditCardVaultToken: String, callback: ApiCallback<Order>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createCheckout(cartProductList: List<CartProduct>, callback: ApiCallback<Checkout>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createCustomerAddress(address: Address, callback: ApiCallback<String>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteCustomerAddress(addressId: String, callback: ApiCallback<Unit>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun editCustomerAddress(addressId: String, address: Address, callback: ApiCallback<Unit>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun editCustomerInfo(firstName: String, lastName: String, phone: String, callback: ApiCallback<Customer>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun forgotPassword(email: String, callback: ApiCallback<Unit>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAcceptedCardTypes(callback: ApiCallback<List<CardType>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getArticle(id: String, callback: ApiCallback<Pair<Article, String>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getArticleList(perPage: Int, paginationValue: Any?, sortBy: SortType?, reverse: Boolean, callback: ApiCallback<List<Article>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCardToken(card: Card, callback: ApiCallback<String>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCheckout(checkoutId: String, callback: ApiCallback<Checkout>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCountries(callback: ApiCallback<List<Country>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCustomer(callback: ApiCallback<Customer?>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getOrder(orderId: String, callback: ApiCallback<Order>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getOrders(perPage: Int, paginationValue: Any?, callback: ApiCallback<List<Order>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getShippingRates(checkoutId: String, callback: ApiCallback<List<ShippingRate>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getShopInfo(callback: ApiCallback<Shop>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isLoggedIn(callback: ApiCallback<Boolean>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun selectShippingRate(checkoutId: String, shippingRate: ShippingRate, callback: ApiCallback<Checkout>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setDefaultShippingAddress(addressId: String, callback: ApiCallback<Unit>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setShippingAddress(checkoutId: String, address: Address, callback: ApiCallback<Checkout>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun signIn(email: String, password: String, callback: ApiCallback<Unit>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun signOut(callback: ApiCallback<Unit>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun signUp(firstName: String, lastName: String, email: String, password: String, phone: String, callback: ApiCallback<Unit>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateCustomerSettings(isAcceptMarketing: Boolean, callback: ApiCallback<Unit>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun getProductList(perPage: Int, paginationValue: Any?,
                               optionBuilder: ProductOptionBuilder,
                               callback: ApiCallback<List<Product>>) {
        makeRequestWithPagination(
            paginationValue,
            callback,
            { page ->

                val options = optionBuilder.addFilterGroup(TYPE_ID_FIELD, PRODUCT_DEFAULT_TYPE_ID)
                    .addSearchCriteria(PaginationPart.PAGE_SIZE.value, perPage)
                    .addSearchCriteria(PaginationPart.CURRENT_PAGE.value, page)
                    .build()

                storeService.getStoreConfigs()
                    .flatMap { response ->
                        productService.getProductList(options)
                            .map { it.mapToEntityList(response.getCurrency(), page, perPage) }
                    }
                    .subscribe(
                        { callback.onResult(it) },
                        { it.printStackTrace() }
                    )
            })
    }

    private fun <T> makeRequestWithPagination(
        paginationValue: Any?,
        callback: ApiCallback<List<T>>,
        request: (page: Int) -> Unit
    ) {
        val page = calculatePage(paginationValue)
        if (page == PAGINATION_END_VALUE) {
            callback.onResult(listOf())
            return
        } else {
            request(page)
        }
    }

    private fun calculatePage(paginationValue: Any?) =
        paginationValue.toString().toIntOrNull() ?: PAGINATION_START_VALUE
}