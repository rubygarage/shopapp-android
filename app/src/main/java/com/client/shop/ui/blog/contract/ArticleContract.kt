package com.client.shop.ui.blog.contract

import com.client.shop.gateway.entity.Article
import com.client.shop.ui.base.contract.BaseLcePresenter
import com.client.shop.ui.base.contract.BaseLceView
import com.domain.interactor.blog.ArticleUseCase
import javax.inject.Inject

interface ArticleView : BaseLceView<Pair<Article, String>>

class ArticlePresenter @Inject constructor(private val articleUseCase: ArticleUseCase) :
    BaseLcePresenter<Pair<Article, String>, ArticleView>(articleUseCase) {

    fun loadArticles(id: String) {
        articleUseCase.execute(
            { view?.showContent(it) },
            { resolveError(it) },
            id
        )
    }
}