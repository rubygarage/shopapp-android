package com.shopapp.gateway

import com.shopapp.gateway.entity.*

interface Api {

    fun getProductList(perPage: Int, paginationValue: Any? = null, sortBy: SortType? = null,
                       keyword: String?, excludeKeyword: String?, callback: ApiCallback<List<Product>>)

    fun getProductVariantList(productVariantIdList: List<String>, callback: ApiCallback<List<ProductVariant>>)

    fun searchProductList(perPage: Int, paginationValue: Any? = null, searchQuery: String,
                          callback: ApiCallback<List<Product>>)

    fun getProduct(id: String, callback: ApiCallback<Product>)

    fun getCategoryList(perPage: Int, paginationValue: Any?, callback: ApiCallback<List<Category>>)

    fun getCategoryDetails(id: String, perPage: Int, paginationValue: Any?, sortBy: SortType? = null,
                           callback: ApiCallback<Category>)

    fun getShopInfo(callback: ApiCallback<Shop>)

    fun getArticleList(perPage: Int, paginationValue: Any?, sortBy: SortType? = null,
                       reverse: Boolean = false, callback: ApiCallback<List<Article>>)

    fun signUp(firstName: String, lastName: String, email: String, password: String, phone: String, callback: ApiCallback<Unit>)

    fun signIn(email: String, password: String, callback: ApiCallback<Unit>)

    fun signOut(callback: ApiCallback<Unit>)

    fun isLoggedIn(callback: ApiCallback<Boolean>)

    fun forgotPassword(email: String, callback: ApiCallback<Unit>)

    fun getCustomer(callback: ApiCallback<Customer?>)

    fun setDefaultShippingAddress(addressId: String, callback: ApiCallback<Unit>)

    fun createCustomerAddress(address: Address, callback: ApiCallback<String>)

    fun editCustomerAddress(addressId: String, address: Address, callback: ApiCallback<Unit>)

    fun deleteCustomerAddress(addressId: String, callback: ApiCallback<Unit>)

    fun getArticle(id: String, callback: ApiCallback<Pair<Article, String>>)

    fun getOrders(perPage: Int, paginationValue: Any? = null, callback: ApiCallback<List<Order>>)

    fun getOrder(orderId: String, callback: ApiCallback<Order>)

    fun editCustomerInfo(firstName: String, lastName: String, phone: String, callback: ApiCallback<Customer>)

    fun changePassword(password: String, callback: ApiCallback<Unit>)

    fun updateCustomerSettings(isAcceptMarketing: Boolean, callback: ApiCallback<Unit>)

    fun getCountries(callback: ApiCallback<List<Country>>)

    fun createCheckout(cartProductList: List<CartProduct>, callback: ApiCallback<Checkout>)

    fun getCheckout(checkoutId: String, callback: ApiCallback<Checkout>)

    fun setShippingAddress(checkoutId: String, address: Address, callback: ApiCallback<Checkout>)

    fun getShippingRates(checkoutId: String, callback: ApiCallback<List<ShippingRate>>)

    fun selectShippingRate(checkoutId: String, shippingRate: ShippingRate, callback: ApiCallback<Checkout>)

    fun getAcceptedCardTypes(callback: ApiCallback<List<CardType>>)

    fun getCardToken(card: Card, callback: ApiCallback<String>)

    fun completeCheckoutByCard(checkout: Checkout, email: String, address: Address, creditCardVaultToken: String, callback: ApiCallback<Order>)

}
