package com.shopapp.data.impl

import com.shopapp.gateway.Api
import com.shopapp.gateway.entity.Article
import com.shopapp.gateway.entity.SortType
import com.shopapp.domain.repository.BlogRepository
import com.shopapp.data.rx.RxCallbackSingle
import io.reactivex.Single

class BlogRepositoryImpl(private val api: Api) : BlogRepository {

    override fun getArticleList(perPage: Int, paginationValue: Any?, sortBy: SortType?,
                                reverse: Boolean): Single<List<Article>> {
        return Single.create<List<Article>> { emitter ->
            api.getArticleList(perPage, paginationValue, sortBy, reverse,
                RxCallbackSingle<List<Article>>(emitter))
        }
    }

    override fun getArticle(id: String): Single<Pair<Article, String>> {
        return Single.create<Pair<Article, String>> { emitter ->
            api.getArticle(id, RxCallbackSingle<Pair<Article, String>>(emitter))
        }
    }
}