package com.data.impl

import com.client.shop.getaway.Api
import com.client.shop.getaway.entity.Category
import com.client.shop.getaway.entity.SortType
import com.data.rx.RxCallbackSingle
import com.domain.repository.CategoryRepository
import io.reactivex.Single

class CategoryRepositoryImpl(private val api: Api) : CategoryRepository {

    override fun getCategory(categoryId: String, productPerPage: Int, productPaginationValue: String?,
                             sortBy: SortType?): Single<Category> {
        return Single.create<Category> { emitter ->
            api.getCategoryDetails(categoryId, productPerPage, productPaginationValue,
                sortBy, RxCallbackSingle<Category>(emitter))
        }
    }

    override fun getCategoryList(perPage: Int, paginationValue: String?): Single<List<Category>> {
        return Single.create<List<Category>> { emitter ->
            api.getCategoryList(perPage, paginationValue, RxCallbackSingle<List<Category>>(emitter))
        }
    }
}