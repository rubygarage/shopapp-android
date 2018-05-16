package com.shopapp.ui.home.contract

import com.shopapp.domain.interactor.shop.ConfigUseCase
import com.shopapp.gateway.entity.Config
import com.shopapp.ui.base.contract.BaseLcePresenter
import com.shopapp.ui.base.contract.BaseLceView
import javax.inject.Inject

interface HomeView : BaseLceView<Unit> {

    fun onConfigReceived(config: Config)
}

class HomePresenter @Inject constructor(private val configUseCase: ConfigUseCase) :
    BaseLcePresenter<Unit, HomeView>(configUseCase) {

    fun getConfig() {
        configUseCase.execute(
            { view.onConfigReceived(it) },
            { resolveError(it) },
            Unit
        )
    }
}