package com.client.shop.ui.blog.contract

import com.domain.entity.Article
import com.domain.interactor.blog.ArticleUseCase
import com.ui.base.contract.BaseLcePresenter
import com.ui.base.contract.BaseLceView
import javax.inject.Inject

interface ArticleView : BaseLceView<Article>

class ArticlePresenter @Inject constructor(private val articleUseCase: ArticleUseCase) :
    BaseLcePresenter<Article, ArticleView>(articleUseCase) {

    fun loadArticles(id: String) {
        articleUseCase.execute(
            { view?.showContent(it) },
            { resolveError(it) },
            id
        )
    }
}