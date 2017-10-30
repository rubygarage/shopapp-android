package com.client.shop.ui.blog.contract

import com.client.shop.ui.base.contract.BaseMvpView
import com.client.shop.ui.base.contract.BasePresenter
import com.domain.entity.Article
import com.domain.entity.SortType
import com.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

interface BlogView : BaseMvpView<List<Article>>

class BlogPresenter @Inject constructor(repository: Repository) : BasePresenter<List<Article>, BlogView>(repository) {

    fun loadArticles(perPage: Int, paginationValue: String? = null) {

        val articlesDisposable = repository.getArticleList(perPage, paginationValue, SortType.RECENT, true)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (isViewAttached) {
                        view?.showContent(result)
                    }
                }, { e -> resolveError(e) })

        disposables.add(articlesDisposable)
    }
}