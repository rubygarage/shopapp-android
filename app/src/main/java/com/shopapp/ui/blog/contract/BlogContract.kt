package com.shopapp.ui.blog.contract

import com.shopapp.domain.interactor.blog.GetArticlesUseCase
import com.shopapp.gateway.entity.Article
import com.shopapp.ui.base.contract.BaseLcePresenter
import com.shopapp.ui.base.contract.BaseLceView
import javax.inject.Inject

interface BlogView : BaseLceView<List<Article>>

class BlogPresenter @Inject constructor(private val getArticlesUseCase: GetArticlesUseCase) :
    BaseLcePresenter<List<Article>, BlogView>(getArticlesUseCase) {

    fun loadArticles(perPage: Int, paginationValue: String? = null) {

        getArticlesUseCase.execute(
            { view?.showContent(it) },
            { resolveError(it) },
            GetArticlesUseCase.Params(perPage, paginationValue)
        )
    }
}