package com.shopapp.domain.repository

import com.shopapp.gateway.entity.Article
import com.shopapp.gateway.entity.SortType
import io.reactivex.Single

interface BlogRepository {

    fun getArticleList(perPage: Int, paginationValue: Any?, sortBy: SortType?, reverse: Boolean): Single<List<Article>>

    fun getArticle(id: String): Single<Pair<Article, String>>
}