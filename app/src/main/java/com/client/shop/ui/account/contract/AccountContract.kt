package com.client.shop.ui.account.contract

import com.client.shop.gateway.entity.Customer
import com.client.shop.gateway.entity.Shop
import com.domain.interactor.account.GetCustomerUseCase
import com.domain.interactor.account.SessionCheckUseCase
import com.domain.interactor.account.ShopInfoUseCase
import com.domain.interactor.account.SignOutUseCase
import com.client.shop.ui.base.contract.BaseLcePresenter
import com.client.shop.ui.base.contract.BaseLceView
import javax.inject.Inject

interface AccountView : BaseLceView<Boolean> {

    fun shopReceived(shop: Shop)

    fun customerReceived(customer: Customer?)

    fun signedOut()
}

class AccountPresenter @Inject constructor(
    private val sessionCheckUseCase: SessionCheckUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val shopInfoUseCase: ShopInfoUseCase,
    private val getCustomerUseCase: GetCustomerUseCase
) :
    BaseLcePresenter<Boolean, AccountView>(sessionCheckUseCase, signOutUseCase,
        shopInfoUseCase, getCustomerUseCase) {

    fun isAuthorized() {
        sessionCheckUseCase.execute(
            { view?.showContent(it) },
            { resolveError(it) },
            Unit
        )
    }

    fun getShopInfo() {
        shopInfoUseCase.execute(
            { view?.shopReceived(it) },
            { resolveError(it) },
            Unit
        )
    }

    fun getCustomer() {
        getCustomerUseCase.execute(
            { view?.customerReceived(it) },
            { view?.customerReceived(null) },
            Unit
        )
    }

    fun signOut() {
        signOutUseCase.execute(
            { view?.signedOut() },
            { resolveError(it) },
            Unit
        )
    }
}