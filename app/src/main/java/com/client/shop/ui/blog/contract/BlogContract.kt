package com.client.shop.ui.blog.contract

import com.client.shop.ui.base.contract.BaseMvpViewLce
import com.client.shop.ui.base.contract.BasePresenterLce
import com.domain.entity.Article
import com.domain.entity.SortType
import com.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

interface BlogView : BaseMvpViewLce<List<Article>>

class BlogPresenter @Inject constructor(repository: Repository) : BasePresenterLce<List<Article>, BlogView>(repository) {

    fun loadArticles(perPage: Int, paginationValue: String? = null) {

        disposables.add(repository.getArticleList(perPage, paginationValue, SortType.RECENT, true)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (isViewAttached) {
                        view?.showContent(result)
                    }
                }, { e -> resolveError(e) }))
    }
}