package com.client.shop.ui.blog.contract

import com.domain.entity.Article
import com.domain.interactor.blog.ArticleListUseCase
import com.ui.base.contract.BasePresenter
import com.ui.base.contract.BaseView
import javax.inject.Inject

interface BlogView : BaseView<List<Article>>

class BlogPresenter @Inject constructor(private val articleListUseCase: ArticleListUseCase) :
        BasePresenter<List<Article>, BlogView>(arrayOf(articleListUseCase)) {

    fun loadArticles(perPage: Int, paginationValue: String? = null) {

        articleListUseCase.execute(
                { view?.showContent(it) },
                { resolveError(it) },
                ArticleListUseCase.Params(perPage, paginationValue)
        )
    }
}