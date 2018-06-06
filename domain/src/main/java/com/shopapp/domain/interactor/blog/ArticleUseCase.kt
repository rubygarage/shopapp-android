package com.shopapp.domain.interactor.blog

import com.shopapp.domain.interactor.base.SingleUseCase
import com.shopapp.domain.repository.BlogRepository
import com.shopapp.gateway.entity.Article
import io.reactivex.Single
import javax.inject.Inject

class ArticleUseCase @Inject constructor(private val blogRepository: BlogRepository) :
    SingleUseCase<Pair<Article, String>, String>() {

    override fun buildUseCaseSingle(params: String): Single<Pair<Article, String>> =
        blogRepository.getArticle(params)
}