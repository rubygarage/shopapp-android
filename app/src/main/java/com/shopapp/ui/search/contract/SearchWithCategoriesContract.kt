package com.shopapp.ui.search.contract

import com.shopapp.domain.interactor.shop.ConfigUseCase
import com.shopapp.gateway.entity.Config
import com.shopapp.ui.base.contract.BaseLcePresenter
import com.shopapp.ui.base.contract.BaseLceView
import javax.inject.Inject

interface SearchWithCategoriesView : BaseLceView<Config>

class SearchWithCategoriesPresenter @Inject constructor(private val configUseCase: ConfigUseCase) :
    BaseLcePresenter<Config, SearchWithCategoriesView>(configUseCase) {

    fun getConfig() {
        configUseCase.execute(
            { view?.showContent(it) },
            { resolveError(it) },
            Unit
        )
    }
}