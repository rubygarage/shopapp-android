package com.domain.repository

import com.client.shop.getaway.entity.Product
import com.client.shop.getaway.entity.SortType
import io.reactivex.Single

interface ProductRepository {

    fun getProduct(productId: String): Single<Product>

    fun getProductList(perPage: Int, paginationValue: Any?, sortBy: SortType?,
                       keyword: String?, excludeKeyword: String?): Single<List<Product>>

    fun searchProductListByQuery(searchQuery: String, perPage: Int, paginationValue: String?): Single<List<Product>>
}