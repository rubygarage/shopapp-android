package com.shopapp.domain.repository

import com.shopapp.gateway.entity.Product
import com.shopapp.gateway.entity.ProductVariant
import com.shopapp.gateway.entity.SortType
import io.reactivex.Single

interface ProductRepository {

    fun getProduct(productId: String): Single<Product>

    fun getProducts(
        perPage: Int, paginationValue: Any?, sortBy: SortType?,
        keyword: String?, excludeKeyword: String?
    ): Single<List<Product>>

    fun searchProducts(query: String, perPage: Int, paginationValue: String?): Single<List<Product>>

    fun getProductVariants(ids: List<String>): Single<List<ProductVariant>>
}