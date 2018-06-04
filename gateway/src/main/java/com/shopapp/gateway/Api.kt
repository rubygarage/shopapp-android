package com.shopapp.gateway

import com.shopapp.gateway.entity.*

interface Api {

    // Config

    fun getConfig(callback: ApiCallback<Config>)

    // Shop

    fun getShop(callback: ApiCallback<Shop>)

    // Products

    fun getProducts(
        perPage: Int, paginationValue: Any? = null, sortBy: SortType? = null,
        keyword: String?, excludeKeyword: String?, callback: ApiCallback<List<Product>>
    )

    fun getProduct(id: String, callback: ApiCallback<Product>)

    fun getProductVariants(ids: List<String>, callback: ApiCallback<List<ProductVariant>>)

    fun searchProducts(
        perPage: Int, paginationValue: Any? = null, query: String,
        callback: ApiCallback<List<Product>>
    )

    // Categories

    fun getCategories(
        perPage: Int, paginationValue: Any?, parentCategoryId: String?,
        callback: ApiCallback<List<Category>>
    )

    fun getCategory(
        id: String, perPage: Int, paginationValue: Any?, sortBy: SortType? = null,
        callback: ApiCallback<Category>
    )

    // Articles

    fun getArticles(
        perPage: Int, paginationValue: Any?, sortBy: SortType? = null,
        reverse: Boolean = false, callback: ApiCallback<List<Article>>
    )

    fun getArticle(id: String, callback: ApiCallback<Pair<Article, String>>)

    // Authentication

    fun signUp(
        firstName: String, lastName: String, email: String, password: String, phone: String,
        callback: ApiCallback<Unit>
    )

    fun signIn(email: String, password: String, callback: ApiCallback<Unit>)

    fun signOut(callback: ApiCallback<Unit>)

    fun isSignedIn(callback: ApiCallback<Boolean>)

    fun resetPassword(email: String, callback: ApiCallback<Unit>)

    // Customer

    fun getCustomer(callback: ApiCallback<Customer?>)

    fun updateCustomer(
        firstName: String, lastName: String, phone: String,
        callback: ApiCallback<Customer>
    )

    fun updateCustomerSettings(isAcceptMarketing: Boolean, callback: ApiCallback<Unit>)

    fun updatePassword(password: String, callback: ApiCallback<Unit>)

    fun addCustomerAddress(address: Address, callback: ApiCallback<String>)

    fun updateCustomerAddress(address: Address, callback: ApiCallback<Unit>)

    fun setDefaultShippingAddress(addressId: String, callback: ApiCallback<Unit>)

    fun deleteCustomerAddress(addressId: String, callback: ApiCallback<Unit>)

    // Payments

    fun createCheckout(cartProducts: List<CartProduct>, callback: ApiCallback<Checkout>)

    fun getCheckout(checkoutId: String, callback: ApiCallback<Checkout>)

    fun setShippingAddress(checkoutId: String, address: Address, callback: ApiCallback<Checkout>)

    fun getShippingRates(checkoutId: String, callback: ApiCallback<List<ShippingRate>>)

    fun setShippingRate(
        checkoutId: String, shippingRate: ShippingRate,
        callback: ApiCallback<Checkout>
    )

    fun completeCheckoutByCard(
        checkout: Checkout, email: String, address: Address,
        creditCardVaultToken: String, callback: ApiCallback<Order>
    )

    fun getAcceptedCardTypes(callback: ApiCallback<List<CardType>>)

    fun getCardToken(card: Card, callback: ApiCallback<String>)

    // Countries

    fun getCountries(callback: ApiCallback<List<Country>>)

    // Orders

    fun getOrders(perPage: Int, paginationValue: Any? = null, callback: ApiCallback<List<Order>>)

    fun getOrder(orderId: String, callback: ApiCallback<Order>)
}
