package com.domain.interactor.blog

import com.client.shop.gateway.entity.Article
import com.client.shop.gateway.entity.SortType
import com.domain.interactor.base.SingleUseCase
import com.domain.repository.BlogRepository
import io.reactivex.Single
import javax.inject.Inject

class ArticleListUseCase @Inject constructor(private val blogRepository: BlogRepository) :
    SingleUseCase<List<Article>, ArticleListUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<List<Article>> {
        return with(params) {
            blogRepository.getArticleList(perPage, paginationValue, SortType.RECENT, true)
        }
    }

    data class Params(
        val perPage: Int,
        val paginationValue: String? = null
    )
}