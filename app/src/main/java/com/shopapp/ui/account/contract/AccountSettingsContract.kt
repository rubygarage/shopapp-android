package com.shopapp.ui.account.contract

import com.shopapp.domain.interactor.account.GetCustomerUseCase
import com.shopapp.domain.interactor.account.UpdateCustomerSettingsUseCase
import com.shopapp.ui.base.contract.BaseLcePresenter
import com.shopapp.ui.base.contract.BaseLceView

interface AccountSettingsView : BaseLceView<Boolean>

class AccountSettingsPresenter(
    private val customerUseCase: GetCustomerUseCase,
    private val updateSettingsUseCase: UpdateCustomerSettingsUseCase) :
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