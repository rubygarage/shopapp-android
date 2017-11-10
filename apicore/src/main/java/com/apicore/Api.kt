package com.apicore

import com.domain.entity.*

interface Api {

    fun getProductList(perPage: Int, paginationValue: Any? = null, sortBy: SortType? = null,
                       reverse: Boolean = false, callback: ApiCallback<List<Product>>)

    fun searchProductList(perPage: Int, paginationValue: Any? = null, searchQuery: String,
                          callback: ApiCallback<List<Product>>)

    fun getProduct(id: String, callback: ApiCallback<Product>)

    fun getCategoryList(perPage: Int, paginationValue: Any?, sortBy: SortType? = null,
                        reverse: Boolean = false, callback: ApiCallback<List<Category>>)

    fun getCategoryDetails(id: String, perPage: Int, paginationValue: Any?, sortBy: SortType? = null,
                           reverse: Boolean = false, callback: ApiCallback<Category>)

    fun getShopInfo(callback: ApiCallback<Shop>)

    fun getArticleList(perPage: Int, paginationValue: Any?, sortBy: SortType? = null,
                       reverse: Boolean = false, callback: ApiCallback<List<Article>>)

    fun signUp(firstName: String, lastName: String, email: String, password: String, callback: ApiCallback<Customer>)

    fun signIn(email: String, authToken: String, callback: ApiCallback<Customer>)

    fun requestToken(email: String, password: String, callback: ApiCallback<AccessData>)
}
