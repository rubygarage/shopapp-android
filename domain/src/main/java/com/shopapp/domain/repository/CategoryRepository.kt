package com.shopapp.domain.repository

import com.shopapp.gateway.entity.Category
import com.shopapp.gateway.entity.SortType
import io.reactivex.Single

interface CategoryRepository {

    fun getCategories(perPage: Int, paginationValue: String? = null): Single<List<Category>>

    fun getCategory(
        categoryId: String, productPerPage: Int, productPaginationValue: String? = null,
        sortBy: SortType? = null
    ): Single<Category>
}