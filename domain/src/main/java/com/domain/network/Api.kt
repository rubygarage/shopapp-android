package com.domain.network

import com.domain.entity.*

interface Api {

    fun getProductList(perPage: Int, paginationValue: Any? = null, sortBy: SortType? = null,
                       keyPhrase: String?, callback: ApiCallback<List<Product>>)

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

    fun getCustomer(callback: ApiCallback<Customer>)

    fun setDefaultShippingAddress(addressId: String, callback: ApiCallback<Unit>)

    fun createCustomerAddress(address: Address, callback: ApiCallback<String>)

    fun editCustomerAddress(addressId: String, address: Address, callback: ApiCallback<Unit>)

    fun deleteCustomerAddress(addressId: String, callback: ApiCallback<Unit>)

    fun getArticle(id: String, callback: ApiCallback<Article>)

    fun getOrders(perPage: Int, paginationValue: Any? = null, callback: ApiCallback<List<Order>>)

    fun getOrder(orderId: String, callback: ApiCallback<Order>)

    fun editCustomerInfo(firstName: String, lastName: String, email: String, phone: String, callback: ApiCallback<Customer>)
    
    fun changePassword(password: String, callback: ApiCallback<Unit>)

    fun updateCustomerSettings(isAcceptMarketing: Boolean,  callback: ApiCallback<Unit>)
}
