package com.data.impl

import com.data.rx.RxCallbackSingle
import com.domain.entity.Article
import com.domain.entity.SortType
import com.domain.network.Api
import com.domain.repository.BlogRepository
import io.reactivex.Single

class BlogRepositoryImpl(private val api: Api) : BlogRepository {

    override fun getArticleList(perPage: Int, paginationValue: Any?, sortBy: SortType?,
                                reverse: Boolean): Single<List<Article>> {
        return Single.create<List<Article>> { emitter ->
            api.getArticleList(perPage, paginationValue, sortBy, reverse,
                    RxCallbackSingle<List<Article>>(emitter))
        }
    }

    override fun getArticle(id: String): Single<Article> {
        return Single.create<Article> { emitter ->
            api.getArticle(id, RxCallbackSingle<Article>(emitter))
        }
    }
}