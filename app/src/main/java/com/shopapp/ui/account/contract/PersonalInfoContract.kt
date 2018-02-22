package com.shopapp.ui.account.contract

import com.shopapp.gateway.entity.Customer
import com.shopapp.domain.interactor.account.EditCustomerUseCase
import com.shopapp.domain.interactor.account.GetCustomerUseCase
import com.shopapp.ui.base.contract.BaseLcePresenter
import com.shopapp.ui.base.contract.BaseLceView
import javax.inject.Inject

interface PersonalInfoView : BaseLceView<Customer> {

    fun onCustomerChanged(customer: Customer)

    fun setupCustomerEmail(email: String)

    fun hideProgress()
}

class PersonalInfoPresenter @Inject constructor(
    private val customerUseCase: GetCustomerUseCase,
    private val editCustomerUseCase: EditCustomerUseCase
) : BaseLcePresenter<Customer, PersonalInfoView>(customerUseCase, editCustomerUseCase) {

    fun getCustomer() {
        customerUseCase.execute(
            {
                it?.let {
                    view?.showContent(it)
                    view?.setupCustomerEmail(it.email)
                }
            },
            { resolveError(it) },
            Unit
        )
    }

    fun editCustomer(name: String, lastName: String, phone: String) {

        editCustomerUseCase.execute(
            {
                view?.onCustomerChanged(it)
            },
            {
                resolveError(it)
                view?.hideProgress()
            },
            EditCustomerUseCase.Params(name, lastName, phone)
        )
    }

}