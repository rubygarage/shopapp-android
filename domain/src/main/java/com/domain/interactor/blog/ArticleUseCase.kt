package com.domain.interactor.blog

import com.client.shop.gateway.entity.Article
import com.domain.interactor.base.SingleUseCase
import com.domain.repository.BlogRepository
import io.reactivex.Single
import javax.inject.Inject

class ArticleUseCase @Inject constructor(private val blogRepository: BlogRepository) :
    SingleUseCase<Pair<Article, String>, String>() {

    override fun buildUseCaseSingle(params: String): Single<Pair<Article, String>> =
        blogRepository.getArticle(params)
}