package com.client.shop.ui.blog.contract

import com.domain.entity.Article
import com.domain.interactor.base.SingleUseCase
import com.repository.BlogRepository
import com.ui.contract.BasePresenter
import com.ui.contract.BaseView
import io.reactivex.Single
import javax.inject.Inject

interface ArticleView : BaseView<Article>

class ArticlePresenter @Inject constructor(private val articleUseCase: ArticleUseCase) :
        BasePresenter<Article, ArticleView>(arrayOf(articleUseCase)) {

    fun loadArticles(id: String) {
        articleUseCase.execute(
                { view?.showContent(it) },
                { resolveError(it) },
                id
        )
    }
}

class ArticleUseCase @Inject constructor(private val blogRepository: BlogRepository) :
        SingleUseCase<Article, String>() {

    override fun buildUseCaseSingle(params: String): Single<Article> =
            blogRepository.getArticle(params)
}