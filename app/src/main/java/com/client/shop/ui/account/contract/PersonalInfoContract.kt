package com.client.shop.ui.account.contract

import com.domain.entity.Customer
import com.domain.interactor.account.EditCustomerUseCase
import com.domain.interactor.account.GetCustomerUseCase
import com.ui.base.contract.BaseLcePresenter
import com.ui.base.contract.BaseLceView
import javax.inject.Inject

interface PersonalInfoView : BaseLceView<Customer> {
    fun showUpdateProgress()

    fun hideUpdateProgress()

    fun onCustomerChanged(customer: Customer)

    fun setupCustomerEmail(email: String)
}

class PersonalInfoPresenter @Inject constructor(
    private val customerUseCase: GetCustomerUseCase,
    private val editCustomerUseCase: EditCustomerUseCase
) : BaseLcePresenter<Customer, PersonalInfoView>(customerUseCase, editCustomerUseCase) {

    fun getCustomer() {
        customerUseCase.execute(
            {
                view?.showContent(it)
                view?.setupCustomerEmail(it.email)
            },
            { resolveError(it) },
            Unit
        )
    }

    fun editCustomer(name: String, lastName: String, phone: String) {

        view?.showUpdateProgress()
        editCustomerUseCase.execute(
            {
                view?.onCustomerChanged(it)
                view?.hideUpdateProgress()
            },
            {
                view?.hideUpdateProgress()
                resolveError(it)
            },
            EditCustomerUseCase.Params(name, lastName, phone)
        )

    }

}