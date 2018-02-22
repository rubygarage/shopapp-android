package com.shopapp.data.impl

import com.shopapp.gateway.Api
import com.shopapp.gateway.entity.Category
import com.shopapp.gateway.entity.SortType
import com.shopapp.data.rx.RxCallbackSingle
import com.shopapp.domain.repository.CategoryRepository
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