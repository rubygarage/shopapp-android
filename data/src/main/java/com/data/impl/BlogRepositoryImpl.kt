package com.data.impl

import com.domain.entity.Article
import com.domain.entity.SortType
import com.domain.network.Api
import com.domain.repository.BlogRepository
import com.data.rx.RxCallback
import io.reactivex.Single

class BlogRepositoryImpl(private val api: Api) : BlogRepository {

    override fun getArticleList(perPage: Int, paginationValue: Any?, sortBy: SortType?,
                                reverse: Boolean): Single<List<Article>> {
        return Single.create<List<Article>> { emitter ->
            api.getArticleList(perPage, paginationValue, sortBy, reverse,
                    RxCallback<List<Article>>(emitter))
        }
    }
}