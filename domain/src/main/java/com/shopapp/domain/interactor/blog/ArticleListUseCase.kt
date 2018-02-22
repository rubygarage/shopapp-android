package com.shopapp.domain.interactor.blog

import com.shopapp.gateway.entity.Article
import com.shopapp.gateway.entity.SortType
import com.shopapp.domain.interactor.base.SingleUseCase
import com.shopapp.domain.repository.BlogRepository
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