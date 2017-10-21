package com.repository

import com.domain.entity.Article
import com.domain.entity.SortType
import io.reactivex.Single

interface BlogRepository {

    fun getArticleList(perPage: Int, paginationValue: Any?, sortBy: SortType?, reverse: Boolean): Single<List<Article>>
}