package com.client.shop.ui.home.contract

import com.client.shop.const.Constant.MAXIMUM_PER_PAGE_COUNT
import com.domain.entity.Category
import com.domain.entity.Shop
import com.domain.interactor.home.HomeUseCase
import com.ui.base.contract.BasePresenter
import com.ui.base.contract.BaseView
import javax.inject.Inject

interface HomeView : BaseView<Pair<Shop, List<Category>>>

class HomePresenter @Inject constructor(private val homeUseCase: HomeUseCase) :
        BasePresenter<Pair<Shop, List<Category>>, HomeView>(arrayOf(homeUseCase)) {

    fun requestData() {

        homeUseCase.execute(
                { view?.showContent(it) },
                { resolveError(it) },
                MAXIMUM_PER_PAGE_COUNT)
    }
}