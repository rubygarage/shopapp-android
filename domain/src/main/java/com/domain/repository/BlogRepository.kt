package com.domain.repository

import com.client.shop.gateway.entity.Article
import com.client.shop.gateway.entity.SortType
import io.reactivex.Single

interface BlogRepository {

    fun getArticleList(perPage: Int, paginationValue: Any?, sortBy: SortType?, reverse: Boolean): Single<List<Article>>

    fun getArticle(id: String): Single<Article>
}