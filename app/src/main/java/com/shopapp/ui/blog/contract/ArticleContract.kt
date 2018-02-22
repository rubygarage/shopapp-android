package com.shopapp.ui.blog.contract

import com.shopapp.gateway.entity.Article
import com.shopapp.domain.interactor.blog.ArticleUseCase
import com.shopapp.ui.base.contract.BaseLcePresenter
import com.shopapp.ui.base.contract.BaseLceView
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