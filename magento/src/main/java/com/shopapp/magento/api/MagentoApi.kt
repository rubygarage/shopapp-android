package com.shopapp.magento.api

import android.content.Context
import android.content.SharedPreferences
import com.securepreferences.SecurePreferences
import com.shopapp.gateway.Api
import com.shopapp.gateway.ApiCallback
import com.shopapp.gateway.entity.*
import com.shopapp.magento.adapter.ProductVariantAdapter
import com.shopapp.magento.api.Constant.ACCESS_KEY
import com.shopapp.magento.api.Constant.ACCESS_TOKEN
import com.shopapp.magento.api.Constant.ACCESS_TOKEN_PREFIX
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
import com.shopapp.magento.api.Constant.UNAUTHORIZED_ERROR
import com.shopapp.magento.api.request.*
import com.shopapp.magento.api.response.CustomerResponse
import com.shopapp.magento.api.response.util.NetworkErrorParser
import com.shopapp.magento.retrofit.RestClient
import com.shopapp.magento.retrofit.service.CategoryService
import com.shopapp.magento.retrofit.service.CustomerService
import com.shopapp.magento.retrofit.service.ProductService
import com.shopapp.magento.retrofit.service.StoreService
import retrofit2.HttpException
import retrofit2.Retrofit
import java.net.HttpURLConnection

class MagentoApi : Api {

    companion object {
        private const val BASE_PATH = "rest/V1/"
    }

    private val host: String
    private lateinit var retrofit: Retrofit
    private val preferences: SharedPreferences

    constructor(context: Context, host: String) {
        this.host = host
        retrofit = RestClient.providesRetrofit(
            context,
            host + BASE_PATH,
            StoreService.STORE_CONFIGS_URL,
            CustomerService.COUNTRIES_URL
        )
        preferences = SecurePreferences(context)
    }

    constructor(host: String, retrofit: Retrofit, preferences: SharedPreferences) {
        this.host = host
        this.retrofit = retrofit
        this.preferences = preferences
    }

    private val config by lazy {
        Config(
            isPopularEnabled = false,
            isBlogEnabled = false,
            isCategoryGridEnabled = false,
            isCustomerPhoneEnabled = false
        )
    }

    private val productService by lazy {
        retrofit.create(ProductService::class.java)
    }

    private val storeService by lazy {
        retrofit.create(StoreService::class.java)
    }

    private val categoryService by lazy {
        retrofit.create(CategoryService::class.java)
    }

    private val customerService by lazy {
        retrofit.create(CustomerService::class.java)
    }

    // Config

    override fun getConfig(callback: ApiCallback<Config>) {
        callback.onResult(config)
    }

    // Shop

    override fun getShop(callback: ApiCallback<Shop>) {
        callback.onResult(Shop("", null, null, null, null)) //TODO
        storeService.getStoreConfigs()
            .flatMap {
                val currency = it.getCurrency()
                productService.getProduct(id).map { it.mapToEntity(host, currency) }
            }
            .subscribe(
                { callback.onResult(it) },
                { callback.onFailure(handleError(it)) }
            )
    }

    // Products

    override fun getProducts(
        perPage: Int, paginationValue: Any?, sortBy: SortType?,
        keyword: String?, excludeKeyword: String?,
        callback: ApiCallback<List<Product>>
    ) {

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

    override fun getProduct(id: String, callback: ApiCallback<Product>) {
        storeService.getStoreConfigs()
            .flatMap {
                val currency = it.getCurrency()
                productService.getProduct(id).map { it.mapToEntity(host, currency) }
            }
            .subscribe(
                { callback.onResult(it) },
                { it.printStackTrace() }
            )
    }

    override fun getProductVariants(
        ids: List<String>,
        callback: ApiCallback<List<ProductVariant>>
    ) {

        val optionBuilder = ProductOptionBuilder()
        optionBuilder.addFilterGroup(TYPE_ID_FIELD, PRODUCT_DEFAULT_TYPE_ID)
        optionBuilder.addFilterGroup {
            val filterBuilder = ProductOptionBuilder.FilterBuilder()
            for (sku in ids) {
                filterBuilder.addFilter(it, SKU_FIELD, sku)
            }
            filterBuilder
        }
        val options = optionBuilder.build()

        storeService.getStoreConfigs()
            .flatMap { response ->
                productService.getProductList(options)
                    .map {
                        it.mapToEntityList(host, response.getCurrency(), 1, 1)
                            .map { ProductVariantAdapter.adapt(it) }
                    }
            }
            .subscribe(
                { callback.onResult(it) },
                { callback.onFailure(handleError(it)) }
            )
    }

    override fun searchProducts(
        perPage: Int, paginationValue: Any?, query: String,
        callback: ApiCallback<List<Product>>
    ) {

        val additionalOptions = ProductOptionBuilder()
            .addFilterGroup(NAME_FIELD, "%$query%", ConditionType.SEARCH_CONDITION)
        getProductList(perPage, paginationValue, additionalOptions, callback)
    }

    // Categories

    override fun getCategories(
        perPage: Int, paginationValue: Any?, parentCategoryId: String?,
        callback: ApiCallback<List<Category>>
    ) {

        if (paginationValue == null) {
            categoryService.getCategoryList(parentCategoryId)
                .map { it.mapToEntityList() }
                .subscribe(
                    { callback.onResult(it) },
                    { it.printStackTrace() }
                )
        } else {
            callback.onResult(listOf())
        }
    }

    override fun getCategory(
        id: String, perPage: Int, paginationValue: Any?, sortBy: SortType?,
        callback: ApiCallback<Category>
    ) {

        val page = calculatePage(paginationValue)
        val optionsBuilder = ProductOptionBuilder().addFilterGroup(CATEGORY_ID_FIELD, id)
            .addFilterGroup(TYPE_ID_FIELD, PRODUCT_DEFAULT_TYPE_ID)
            .addSearchCriteria(Pagination.PAGE_SIZE.value, perPage)
            .addSearchCriteria(Pagination.CURRENT_PAGE.value, page)

        when (sortBy) {
            SortType.NAME -> optionsBuilder.addSortOrder(NAME_FIELD)
            SortType.RECENT -> optionsBuilder.addSortOrder(CREATED_AT_FIELD, true)
            SortType.PRICE_HIGH_TO_LOW -> optionsBuilder.addSortOrder(PRICE_FIELD, true)
            SortType.PRICE_LOW_TO_HIGH -> optionsBuilder.addSortOrder(
                PRICE_FIELD
            )
            else -> {
                /*do nothing*/
            }
        }

        val options = optionsBuilder.build()

        storeService.getStoreConfigs()
            .flatMap { response ->
                productService.getProductList(options)
                    .map { it.mapToEntityList(host, response.getCurrency(), page, perPage) }
            }
            .flatMap {
                val productList = if (page == PAGINATION_END_VALUE) listOf() else it
                categoryService.getCategoryDetails(id)
                    .map {
                        it.mapToEntity(host, productList)
                    }
            }
            .subscribe(
                { callback.onResult(it) },
                { callback.onFailure(handleError(it)) }
            )
    }

    // Articles

    override fun getArticles(
        perPage: Int, paginationValue: Any?, sortBy: SortType?,
        reverse: Boolean, callback: ApiCallback<List<Article>>
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getArticle(id: String, callback: ApiCallback<Pair<Article, String>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // Authentication

    override fun signUp(
        firstName: String, lastName: String, email: String, password: String,
        phone: String, callback: ApiCallback<Unit>
    ) {

        val customerData = SignUpRequest.CustomerData(email, firstName, lastName)
        val request = SignUpRequest(customerData, password)
        customerService.signUp(request)
            .subscribe(
                { signIn(email, password, callback) },
                { callback.onFailure(handleError(it)) }
            )
    }

    override fun signIn(email: String, password: String, callback: ApiCallback<Unit>) {
        customerService.signIn(SignInRequest(email, password))
            .subscribe(
                {
                    saveSession(password, "$ACCESS_TOKEN_PREFIX $it")
                    callback.onResult(Unit)
                },
                { callback.onFailure(handleError(it)) }
            )
    }

    override fun signOut(callback: ApiCallback<Unit>) {
        removeSession()
        callback.onResult(Unit)
    }


    override fun isSignedIn(callback: ApiCallback<Boolean>) {
        callback.onResult(getSession() != null)
    }

    override fun resetPassword(email: String, callback: ApiCallback<Unit>) {
        customerService.resetPassword(ResetPasswordRequest(email))
            .subscribe(
                {
                    callback.onResult(Unit)
                },
                { callback.onFailure(handleError(it)) }
            )
    }

    // Customer

    override fun getCustomer(callback: ApiCallback<Customer?>) {
        sendTokenizedRequest({
            getCustomer(it).subscribe(
                {
                    callback.onResult(it)
                },
                { callback.onFailure(handleError(it)) }
            )
        }, callback)
    }

    override fun updateCustomer(
        firstName: String, lastName: String, phone: String,
        callback: ApiCallback<Customer>
    ) {

    override fun changePassword(password: String, callback: ApiCallback<Unit>) {
        sendTokenizedRequest({ token ->
            val accessKey = getAccessKey()
            if (accessKey != null) {
                customerService.changePassword(token, ChangePasswordRequest(accessKey, password))
                    .subscribe(
                        {
                            saveSession(password, token)
                            callback.onResult(Unit)
                        },
                        { callback.onFailure(handleError(it)) }
                    )
            } else {
                callback.onFailure(Error.NonCritical(UNAUTHORIZED_ERROR))
            }
        }, callback)
    }

    override fun forgotPassword(email: String, callback: ApiCallback<Unit>) {
        customerService.forgotPassword(ForgotPasswordRequest(email))
            .subscribe(
                {
                    callback.onResult(Unit)
                },
                { callback.onFailure(handleError(it)) }
            )
    }

    override fun editCustomerInfo(firstName: String, lastName: String, phone: String, callback: ApiCallback<Customer>) {
        sendTokenizedRequest({ token ->
            getCustomer(token).flatMap {
                val editCustomerRequest = UpdateCustomerRequest.CustomerData(
                    it.email,
                    firstName,
                    lastName,
                    0,
                    listOf()
                )
                customerService.updateCustomer(token, UpdateCustomerRequest(editCustomerRequest))
                val editCustomerRequest = CustomerRequest.CustomerData(it)
                customerService.editCustomer(token, CustomerRequest(editCustomerRequest))
                    .flatMap { processCustomerResponse(it) }
            }
                .subscribe(
                    { callback.onResult(it) },
                    { callback.onFailure(handleError(it)) }
                )
        }, callback)
    }

    override fun updateCustomerSettings(isAcceptMarketing: Boolean, callback: ApiCallback<Unit>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updatePassword(password: String, callback: ApiCallback<Unit>) {
        sendTokenizedRequest({ token ->
            val accessKey = getAccessKey()
            if (accessKey != null) {
                customerService.updatePassword(token, UpdatePasswordRequest(accessKey, password))
                    .subscribe(
                        {
                            saveSession(password, token)
                            callback.onResult(Unit)
                        },
                        { callback.onFailure(handleError(it)) }
                    )
            } else {
                callback.onFailure(Error.NonCritical(UNAUTHORIZED_ERROR))
            }
        }, callback)
    }

    override fun addCustomerAddress(address: Address, callback: ApiCallback<String>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateCustomerAddress(address: Address, callback: ApiCallback<Unit>) {
    override fun createCustomerAddress(address: Address, callback: ApiCallback<Unit>) {
        sendTokenizedRequest({ token ->
            getCustomer(token).map {
                val addressList = it.addressList.toMutableList()
                val defaultAddress = if (addressList.isEmpty()) address else it.defaultAddress
                addressList.add(address)
                it.copy(addressList = addressList, defaultAddress = defaultAddress)
            }
                .flatMap {
                    val editCustomerRequest = CustomerRequest.CustomerData(it)
                    customerService.editCustomer(token, CustomerRequest(editCustomerRequest))
                        .flatMap { processCustomerResponse(it) }
                }
                .subscribe(
                    { callback.onResult(Unit) },
                    { callback.onFailure(handleError(it)) }
                )
        }, callback)
    }

    override fun deleteCustomerAddress(addressId: String, callback: ApiCallback<Unit>) {
        sendTokenizedRequest({ token ->
            getCustomer(token).flatMap {
                val addressList = it.addressList.toMutableList()
                addressList.removeAll { it.id == addressId }
                val updatedCustomer = it.copy(addressList = addressList)
                customerService.editCustomer(token, CustomerRequest(CustomerRequest.CustomerData(updatedCustomer)))
            }
                .subscribe(
                    { callback.onResult(Unit) },
                    { callback.onFailure(handleError(it)) }
                )
        }, callback)
    }

    override fun editCustomerAddress(address: Address, callback: ApiCallback<Unit>) {
        sendTokenizedRequest({ token ->
            getCustomer(token).flatMap {
                val addressList = it.addressList.toMutableList()
                val oldAddress = addressList.find { it.id == address.id }
                val oldAddressIndex = addressList.indexOf(oldAddress)
                addressList[oldAddressIndex] = address
                val defaultAddress = if (it.defaultAddress?.id == address.id) address else it.defaultAddress
                val updatedCustomer = it.copy(addressList = addressList, defaultAddress = defaultAddress)
                customerService.editCustomer(token, CustomerRequest(CustomerRequest.CustomerData(updatedCustomer)))
            }
                .subscribe(
                    { callback.onResult(Unit) },
                    { callback.onFailure(handleError(it)) }
                )
        }, callback)
    }

    override fun setDefaultShippingAddress(addressId: String, callback: ApiCallback<Unit>) {
        sendTokenizedRequest({ token ->
            getCustomer(token).flatMap {
                val defaultAddress = it.addressList.find { it.id == addressId }
                val updatedCustomer = it.copy(defaultAddress = defaultAddress)
                customerService.editCustomer(token, CustomerRequest(CustomerRequest.CustomerData(updatedCustomer)))
            }
                .subscribe(
                    { callback.onResult(Unit) },
                    { callback.onFailure(handleError(it)) }
                )
        }, callback)
    }

    override fun getCountries(callback: ApiCallback<List<Country>>) {
        customerService.getCountries()
            .map { it.map { it.mapToEntity() } }
            .subscribe(
                { callback.onResult(it) },
                { callback.onFailure(handleError(it)) }
            )
    }

    //SHOP

    override fun getConfig(callback: ApiCallback<Config>) {
        callback.onResult(config)
    }

    override fun getShopInfo(callback: ApiCallback<Shop>) {
        callback.onResult(Shop("", null, null, null, null)) //TODO
    }

    //OTHER

    override fun completeCheckoutByCard(checkout: Checkout, email: String, address: Address, creditCardVaultToken: String, callback: ApiCallback<Order>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createCheckout(cartProductList: List<CartProduct>, callback: ApiCallback<Checkout>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAcceptedCardTypes(callback: ApiCallback<List<CardType>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setDefaultShippingAddress(addressId: String, callback: ApiCallback<Unit>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteCustomerAddress(addressId: String, callback: ApiCallback<Unit>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // Payment

    override fun createCheckout(
        cartProductList: List<CartProduct>,
        callback: ApiCallback<Checkout>
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCheckout(checkoutId: String, callback: ApiCallback<Checkout>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setShippingAddress(
        checkoutId: String,
        address: Address,
        callback: ApiCallback<Checkout>
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getShippingRates(checkoutId: String, callback: ApiCallback<List<ShippingRate>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setShippingRate(
        checkoutId: String,
        shippingRate: ShippingRate,
        callback: ApiCallback<Checkout>
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun completeCheckoutByCard(
        checkout: Checkout,
        email: String,
        address: Address,
        creditCardVaultToken: String,
        callback: ApiCallback<Order>
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAcceptedCardTypes(callback: ApiCallback<List<CardType>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCardToken(card: Card, callback: ApiCallback<String>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // Countries

    override fun getCountries(callback: ApiCallback<List<Country>>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // Orders

    override fun getOrders(
        perPage: Int,
        paginationValue: Any?,
        callback: ApiCallback<List<Order>>
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getOrder(orderId: String, callback: ApiCallback<Order>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /* Session */

    private fun saveSession(password: String, token: String) {
        preferences.edit()
            .putString(ACCESS_KEY, password)
            .putString(ACCESS_TOKEN, token)
            .apply()
    }

    private fun getSession(): String? = preferences.getString(ACCESS_TOKEN, null)

    private fun getAccessKey(): String? = preferences.getString(ACCESS_KEY, null)

    private fun removeSession() {
        preferences.edit()
            .remove(ACCESS_KEY)
            .remove(ACCESS_TOKEN)
            .apply()
    }

    private fun getCustomer(token: String) = customerService.getCustomer(token)
        .flatMap { processCustomerResponse(it) }

    private fun processCustomerResponse(customerResponse: CustomerResponse) =
        customerService.getCountries()
            .map {
                val countries = it.map { it.mapToEntity() }
                customerResponse.mapToEntity(countries)
            }

    private fun getProductList(
        perPage: Int, paginationValue: Any?,
        optionBuilder: ProductOptionBuilder,
        callback: ApiCallback<List<Product>>
    ) {
        makeRequestWithPagination(
            paginationValue,
            callback,
            { page ->

                val options = optionBuilder.addFilterGroup(TYPE_ID_FIELD, PRODUCT_DEFAULT_TYPE_ID)
                    .addSearchCriteria(Pagination.PAGE_SIZE.value, perPage)
                    .addSearchCriteria(Pagination.CURRENT_PAGE.value, page)
                    .build()

                storeService.getStoreConfigs()
                    .flatMap { response ->
                        productService.getProductList(options)
                            .map { it.mapToEntityList(host, response.getCurrency(), page, perPage) }
                    }
                    .subscribe(
                        { callback.onResult(it) },
                        { callback.onFailure(handleError(it)) }
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

    private fun handleError(throwable: Throwable): Error {
        throwable.printStackTrace()
        return if (throwable is HttpException) {
            if (throwable.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                removeSession()
            }
            val message = NetworkErrorParser.parse(throwable)
            return message?.let {
                Error.NonCritical(it)
            } ?: Error.Content()
        } else {
            Error.Content()
        }
    }

    private inline fun sendTokenizedRequest(
        request: (token: String) -> Unit,
        callback: ApiCallback<*>
    ) {
        getSession()?.let {
            request(it)
        } ?: callback.onFailure(Error.NonCritical(UNAUTHORIZED_ERROR))
    }
}