package com.shopapp.ui.blog.contract

import com.shopapp.gateway.entity.Article
import com.shopapp.domain.interactor.blog.ArticleListUseCase
import com.shopapp.ui.base.contract.BaseLcePresenter
import com.shopapp.ui.base.contract.BaseLceView
import javax.inject.Inject

interface BlogView : BaseLceView<List<Article>>

class BlogPresenter @Inject constructor(private val articleListUseCase: ArticleListUseCase) :
    BaseLcePresenter<List<Article>, BlogView>(articleListUseCase) {

    fun loadArticles(perPage: Int, paginationValue: String? = null) {

        articleListUseCase.execute(
            { view?.showContent(it) },
            { resolveError(it) },
            ArticleListUseCase.Params(perPage, paginationValue)
        )
    }
}