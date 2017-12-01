package com.client.shop.ui.home.contract

import com.client.shop.const.Constant.MAXIMUM_PER_PAGE_COUNT
import com.domain.entity.Category
import com.domain.entity.Shop
import com.domain.interactor.home.HomeUseCase
import com.ui.base.contract.BaseLcePresenter
import com.ui.base.contract.BaseLceView
import javax.inject.Inject

interface HomeView : BaseLceView<Pair<Shop, List<Category>>>

class HomePresenter @Inject constructor(private val homeUseCase: HomeUseCase) :
        BaseLcePresenter<Pair<Shop, List<Category>>, HomeView>(homeUseCase) {

    fun requestData() {

        homeUseCase.execute(
                { view?.showContent(it) },
                { resolveError(it) },
                MAXIMUM_PER_PAGE_COUNT)
    }
}