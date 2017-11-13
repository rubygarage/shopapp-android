package com.client.shop.ui.blog.contract

import com.client.shop.ui.base.contract.BasePresenter
import com.client.shop.ui.base.contract.BaseView
import com.client.shop.ui.base.contract.SingleUseCase
import com.domain.entity.Article
import com.domain.entity.SortType
import com.repository.BlogRepository
import io.reactivex.Single
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

class ArticleListUseCase @Inject constructor(private val blogRepository: BlogRepository) :
        SingleUseCase<List<Article>, ArticleListUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<List<Article>> {
        return with(params) {
            blogRepository.getArticleList(perPage, paginationValue, SortType.RECENT, true)
        }
    }

    data class Params(
            val perPage: Int,
            val paginationValue: String? = null
    )
}