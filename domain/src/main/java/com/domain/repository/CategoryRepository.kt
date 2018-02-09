package com.domain.repository

import com.client.shop.getaway.entity.Category
import com.client.shop.getaway.entity.SortType
import io.reactivex.Single

interface CategoryRepository {

    fun getCategory(categoryId: String, productPerPage: Int, productPaginationValue: String? = null,
                    sortBy: SortType? = null): Single<Category>

    fun getCategoryList(perPage: Int, paginationValue: String? = null): Single<List<Category>>
}