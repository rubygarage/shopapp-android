package com.client.shop.ui.blog.contract

import com.client.shop.ui.base.contract.BaseMvpView
import com.client.shop.ui.base.contract.BasePresenter
import com.client.shop.ui.base.rx.RxCallback
import com.shopapicore.ShopApiCore
import com.shopapicore.entity.Article
import com.shopapicore.entity.SortType
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

interface BlogView : BaseMvpView {

    fun articleListLoaded(articleList: List<Article>)
}

class BlogPresenter @Inject constructor(private val shopApiCore: ShopApiCore) : BasePresenter<BlogView>() {

    fun loadArticles(perPage: Int, paginationValue: String? = null) {

        showProgress()

        val call = Observable.create<List<Article>> { emitter ->
            shopApiCore.getArticleList(perPage, paginationValue, SortType.RECENT, true, RxCallback<List<Article>>(emitter))
        }

        disposables.add(call
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (isViewAttached) {
                        view.articleListLoaded(result)
                    }
                }, { _ -> hideProgress() }, { hideProgress() }))
    }
}