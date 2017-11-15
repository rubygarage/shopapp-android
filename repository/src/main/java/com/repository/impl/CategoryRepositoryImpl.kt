package com.repository.impl

import com.domain.entity.Category
import com.domain.entity.SortType
import com.repository.CategoryRepository
import com.repository.rx.RxCallback
import com.domain.network.Api
import io.reactivex.Single

class CategoryRepositoryImpl(private val api: Api) : CategoryRepository {

    override fun getCategory(categoryId: String, productPerPage: Int, productPaginationValue: String?,
                             sortBy: SortType?, reverse: Boolean): Single<Category> {
        //val reverse = sortBy == SortType.RECENT //TODO MOVE TO SHOPIFY

        return Single.create<Category> { emitter ->
            api.getCategoryDetails(categoryId, productPerPage, productPaginationValue,
                    sortBy, reverse, RxCallback<Category>(emitter))
        }
    }

    override fun getCategoryList(perPage: Int, paginationValue: String?, sortBy: SortType?,
                                 reverse: Boolean): Single<List<Category>> {
        return Single.create<List<Category>> { emitter ->
            api.getCategoryList(perPage, paginationValue, sortBy, reverse,
                    RxCallback<List<Category>>(emitter))
        }
    }
}