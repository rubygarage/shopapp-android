package com.domain.repository

import com.domain.entity.Product
import com.domain.entity.SortType
import io.reactivex.Single

interface ProductRepository {

    fun getProduct(productId: String): Single<Product>

    fun getProductList(perPage: Int, paginationValue: Any?, sortBy: SortType?, reverse: Boolean): Single<List<Product>>

    fun searchProductListByQuery(searchQuery: String, perPage: Int, paginationValue: String?): Single<List<Product>>
}