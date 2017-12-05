package com.domain.interactor.blog

import com.domain.entity.Article
import com.domain.interactor.base.SingleUseCase
import com.domain.repository.BlogRepository
import io.reactivex.Single
import javax.inject.Inject

class ArticleUseCase @Inject constructor(private val blogRepository: BlogRepository) :
        SingleUseCase<Article, String>() {

    override fun buildUseCaseSingle(params: String): Single<Article> =
            blogRepository.getArticle(params)
}