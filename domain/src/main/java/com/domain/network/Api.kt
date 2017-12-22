package com.domain.network

import com.domain.entity.*

interface Api {

    fun getProductList(perPage: Int, paginationValue: Any? = null, sortBy: SortType? = null,
                       callback: ApiCallback<List<Product>>)

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

    fun getArticle(id: String, callback: ApiCallback<Article>)
}
