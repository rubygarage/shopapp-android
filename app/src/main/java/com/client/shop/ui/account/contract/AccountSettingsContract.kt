package com.client.shop.ui.account.contract

import com.domain.interactor.account.GetCustomerUseCase
import com.domain.interactor.account.UpdateAccountSettingsUseCase
import com.ui.base.contract.BaseLcePresenter
import com.ui.base.contract.BaseLceView

interface AccountSettingsView : BaseLceView<Boolean>

class AccountSettingsPresenter(
    private val customerUseCase: GetCustomerUseCase,
    private val updateSettingsUseCase: UpdateAccountSettingsUseCase) :
    BaseLcePresenter<Boolean, AccountSettingsView>(customerUseCase, updateSettingsUseCase) {

    fun getCustomer() {
        customerUseCase.execute(
            { view?.showContent(it?.isAcceptsMarketing ?: false) },
            { resolveError(it) },
            Unit
        )
    }

    fun updateSettings(isAcceptMarketing: Boolean) {
        updateSettingsUseCase.execute(
            {},
            { resolveError(it) },
            isAcceptMarketing
        )
    }
}