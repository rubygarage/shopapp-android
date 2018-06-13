package com.shopapp.domain.interactor.blog

import com.shopapp.domain.interactor.base.SingleUseCase
import com.shopapp.domain.repository.BlogRepository
import com.shopapp.gateway.entity.Article
import com.shopapp.gateway.entity.SortType
import io.reactivex.Single
import javax.inject.Inject

class GetArticlesUseCase @Inject constructor(private val blogRepository: BlogRepository) :
    SingleUseCase<List<Article>, GetArticlesUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<List<Article>> {
        return with(params) {
            blogRepository.getArticles(perPage, paginationValue, SortType.RECENT)
        }
    }

    data class Params(
        val perPage: Int,
        val paginationValue: String? = null
    )
}